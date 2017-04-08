/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import behaviour.CounterBehaviour;
import environment.wikipedia.chart.EmotionalStateLineChartGui;
import environment.wikipedia.state.EmotionalStateAgent;
import jade.content.AgentAction;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.MasoesSettings;
import masoes.collective.SocialEmotionCalculator;
import masoes.component.behavioural.EmotionalSpace;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import ontology.OntologyAssistant;
import translate.Translation;
import util.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfiguratorAgent extends GuiAgent {

    private static final int FPS = 10;
    private static final String CONTRIBUTOR_KNOWLEDGE = "theories/behavioural/wikipedia/contributorEmotionalAgent.prolog";
    private static List<String> events = Arrays.asList("reputationIncrease", "reputationDecrease", "r1", "r2");
    private AgentLogger logger;
    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;
    private EmotionalSpace emotionalSpace;
    private Behaviour configuratorBehaviour;
    private SocialEmotionCalculator socialEmotionCalculator;
    private EmotionalStateLineChartGui dispersionGraphic;
    private EmotionalStateLineChartGui maxDistancesGraphic;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
        emotionalSpace = new EmotionalSpace();
        socialEmotionCalculator = new SocialEmotionCalculator();
    }

    @Override
    protected void setup() {
        configuratorAgentGui.showGui();
    }

    @Override
    protected void takeDown() {
        configuratorAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
                case UPDATE_SATISFACTION_INCREASE:
                    updateSatisfactionIncrease();
                    break;
                case UPDATE_ACTIVATION_INCREASE:
                    updateActivationIncrease();
                    break;
                case CLEAN:
                    cleanSimulation();
                    break;
                case START:
                    startSimulation();
                    break;
                case ADD_AGENT:
                    addAgent();
                    break;
                case UPDATE_ACTIVATION_TO_ADD:
                case UPDATE_SATISFACTION_TO_ADD:
                    updateEmotionToAdd();
                    break;
                case REMOVE_AGENTS:
                    removeAgents();
                    break;
                case SHOW_EMOTIONAL_STATE_GUI:
                    showEmotionalStateGui();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

    private void showEmotionalStateGui() {
        AgentState agentState = configuratorAgentGui.getSelectedAgentState();

        agentManagementAssistant.createAgent(
                agentState.getAgent().getLocalName() + "_GUI",
                EmotionalStateAgent.class,
                Arrays.asList(agentState.getAgent().getLocalName())
        );
    }

    private void removeAgents() {
        configuratorAgentGui.getSelectedAgentToAdd()
                .forEach(agentToAdd ->
                        configuratorAgentGui.removeAgentToAdd(agentToAdd));
        if (configuratorAgentGui.getAgentsToAdd().size() == 0) {
            configuratorAgentGui.deactivateStartButton();
        }
    }

    private void updateEmotionToAdd() {
        EmotionalState emotionalState = new EmotionalState(
                configuratorAgentGui.getActivationToAdd(),
                configuratorAgentGui.getSatisfactionToAdd()
        );
        configuratorAgentGui.setEmotionToAdd(emotionalSpace.searchEmotion(emotionalState));
    }

    private void addAgent() {
        Optional<AgentToAdd> max = configuratorAgentGui.getAgentsToAdd()
                .stream()
                .filter(agentToAdd ->
                        agentToAdd.getType().equals(configuratorAgentGui.getAgentTypeToAdd()))
                .max(Comparator.comparingInt(AgentToAdd::getSequence));

        int nextSequence = 1;

        if (max.isPresent()) {
            nextSequence = max.get().getSequence() + 1;
        }

        EmotionalState emotionalState = new EmotionalState(
                configuratorAgentGui.getActivationToAdd(),
                configuratorAgentGui.getSatisfactionToAdd()
        );

        AgentToAdd agentToAdd = new AgentToAdd(
                nextSequence,
                configuratorAgentGui.getAgentTypeToAdd(),
                emotionalState
        );
        configuratorAgentGui.addAgentToAdd(agentToAdd);
        configuratorAgentGui.activateStartButton();
    }

    private void startSimulation() {
        configuratorAgentGui.getAgentsToAdd().forEach(agentToAdd -> {
            agentManagementAssistant.createAgent(
                    agentToAdd.getAgentName(),
                    agentToAdd.getType().getAgentCLass(),
                    Arrays.asList(
                            "--activation=" + String.valueOf(agentToAdd.getEmotionalState().getActivation()),
                            "--satisfaction=" + String.valueOf(agentToAdd.getEmotionalState().getSatisfaction()),
                            "--knowledge=" + CONTRIBUTOR_KNOWLEDGE
                    )
            );
        });

        dispersionGraphic = new EmotionalStateLineChartGui(Translation.getInstance().get("gui.emotional_dispersion"));
        dispersionGraphic.setRange(0,1.2);
        maxDistancesGraphic = new EmotionalStateLineChartGui(Translation.getInstance().get("gui.max_distance"));
        maxDistancesGraphic.setRange(0,1.2);

        configuratorBehaviour = new CounterBehaviour(configuratorAgentGui.getIterations()) {
            @Override
            public void count(int i) {
                socialEmotionCalculator.clear();

                List<AgentState> agentStates = configuratorAgentGui.getAgentsToAdd()
                        .stream()
                        .map(agentToAdd -> {
                            AgentAction agentAction = new GetEmotionalState();

                            AID receiver = getAID(agentToAdd.getAgentName());

                            int eventFrequency = configuratorAgentGui.getEventFrequency();

                            if (configuratorAgentGui.isEventFrequencyRandom()) {
                                eventFrequency = RandomGenerator.getInteger(1, 9);
                            }

                            if (i % eventFrequency == 0) {
                                EventStimulus stimulus = new EventStimulus(receiver, RandomGenerator.getRandomItem(events));
                                agentAction = new EvaluateStimulus(stimulus);
                            }

                            AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(receiver, agentAction);

                            socialEmotionCalculator.addEmotionalState(agentState.getEmotionState().toEmotionalState());

                            return agentState;
                        })
                        .collect(Collectors.toList());


                EmotionalState emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
                EmotionalState centralEmotionalState = socialEmotionCalculator.getCentralEmotionalState();
                EmotionalState maximumDistances = socialEmotionCalculator.getMaximumDistances();

                dispersionGraphic.addDispersion(i, emotionalDispersion);
                maxDistancesGraphic.addDispersion(i, maximumDistances);

                configuratorAgentGui.setCentralEmotion(centralEmotionalState);
                configuratorAgentGui.setEmotionalDispersion(emotionalDispersion);
                configuratorAgentGui.setMaximumDistance(maximumDistances);
                configuratorAgentGui.setAgentStates(agentStates);
                configuratorAgentGui.setActualIteration(i);

                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        };

        addBehaviour(configuratorBehaviour);

        configuratorAgentGui.modeSimulation();
    }

    private void cleanSimulation() {
        if (configuratorBehaviour != null) {
            removeBehaviour(configuratorBehaviour);
        }

        if (dispersionGraphic != null) {
            dispersionGraphic.dispose();
        }

        if (maxDistancesGraphic != null) {
            maxDistancesGraphic.dispose();
        }

        List<AgentToAdd> agentsToAdd = configuratorAgentGui.getAgentsToAdd();

        agentsToAdd.forEach(agentToAdd -> {
            try {
                AID gui = getAID(agentToAdd.getAgentName() + "_GUI");
                if (agentManagementAssistant.agents().contains(gui)) {
                    agentManagementAssistant.killAgent(gui);
                }

                agentManagementAssistant.killAgent(getAID(agentToAdd.getAgentName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            configuratorAgentGui.setAgentStates(new ArrayList<>());
            configuratorAgentGui.setAgentsToAdd(new ArrayList<>());
        });
        configuratorAgentGui.modeConfiguration();
    }

    private void updateActivationIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_ACTIVATION_INCREASE, String.valueOf(configuratorAgentGui.getActivationIncrease()));
    }

    private void updateSatisfactionIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_SATISFACTION_INCREASE, String.valueOf(configuratorAgentGui.getSatisfactionIncrease()));
    }

}
