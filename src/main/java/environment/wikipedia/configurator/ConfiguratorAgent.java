/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import behaviour.CounterBehaviour;
import environment.wikipedia.chart.AgentsBehaviourModificationChartGui;
import environment.wikipedia.chart.AgentsEmotionalSpaceChartGui;
import environment.wikipedia.chart.EmotionalDispersionLineChartGui;
import environment.wikipedia.chart.MaximumDistancesLineChartGui;
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
import masoes.ontology.state.collective.CentralEmotion;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.MaximumDistances;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import ontology.OntologyAssistant;
import translate.Translation;
import util.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class ConfiguratorAgent extends GuiAgent {

    private static final int FPS = 10;
    private static final String CONTRIBUTOR_KNOWLEDGE = "theories/behavioural/wikipedia/contributorEmotionalAgent.prolog";
    public static final int DIALOG_DISTANCE = 30;
    private List<KnowledgeRule> knowledgeRules;
    private AgentLogger logger;
    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;
    private EmotionalSpace emotionalSpace;
    private Behaviour configuratorBehaviour;
    private SocialEmotionCalculator socialEmotionCalculator;
    private EmotionalDispersionLineChartGui dispersionGraphic;
    private MaximumDistancesLineChartGui maxDistancesGraphic;
    private AgentsEmotionalSpaceChartGui emotionalSpaceGraphic;
    private AgentsBehaviourModificationChartGui agentsBehaviourModificationChartGui;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
        emotionalSpace = new EmotionalSpace();
        socialEmotionCalculator = new SocialEmotionCalculator();

        knowledgeRules = Arrays.asList(
                new KnowledgeRule(true, "highReputationIncrease", "positive", "positive"),
                new KnowledgeRule(true, "neutralReputationIncrease", "neutral", "positive"),
                new KnowledgeRule(true, "lowReputationIncrease", "negative", "positive"),
                new KnowledgeRule(true, "highReputationDecrease", "negative", "negative"),
                new KnowledgeRule(true, "neutralReputationDecrease", "neutral", "negative"),
                new KnowledgeRule(true, "lowReputationDecrease", "positive", "negative")
        );

        configuratorAgentGui.setKnowledgeRules(knowledgeRules);
    }

    @Override
    protected void setup() {
        configuratorAgentGui.showGui();
    }

    @Override
    protected void takeDown() {
        cleanSimulation();
        configuratorAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    cleanSimulation();
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
        String centralEmotionName = Translation.getInstance().get("gui.central_emotion");

        emotionalSpaceGraphic = new AgentsEmotionalSpaceChartGui(Translation.getInstance().get("gui.emotional_states"));
        emotionalSpaceGraphic.setLocation(100,100);

        agentsBehaviourModificationChartGui = new AgentsBehaviourModificationChartGui(Translation.getInstance().get("gui.behaviour_modifications"));

        agentsBehaviourModificationChartGui.setLocation(
                emotionalSpaceGraphic.getLocation().x + emotionalSpaceGraphic.getSize().width,
                emotionalSpaceGraphic.getLocation().y
        );

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
            agentsBehaviourModificationChartGui.addAgent(agentToAdd.getAgentName());
            emotionalSpaceGraphic.addAgent(agentToAdd.getAgentName());
        });

        emotionalSpaceGraphic.addAgent(centralEmotionName);

        dispersionGraphic = new EmotionalDispersionLineChartGui(Translation.getInstance().get("gui.emotional_dispersion"));
        dispersionGraphic.setLocation(
                emotionalSpaceGraphic.getLocation().x,
                emotionalSpaceGraphic.getLocation().y + emotionalSpaceGraphic.getSize().height + DIALOG_DISTANCE
        );

        maxDistancesGraphic = new MaximumDistancesLineChartGui(Translation.getInstance().get("gui.max_distance"));
        maxDistancesGraphic.setLocation(
                emotionalSpaceGraphic.getLocation().x + emotionalSpaceGraphic.getSize().width,
                emotionalSpaceGraphic.getLocation().y + emotionalSpaceGraphic.getSize().height + DIALOG_DISTANCE
        );

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
                                List<KnowledgeRule> filterKnowledgeRules = knowledgeRules.stream().filter(KnowledgeRule::isSelected).collect(Collectors.toList());

                                String rule = "";

                                KnowledgeRule randomItem = RandomGenerator.getRandomItem(filterKnowledgeRules);
                                if (randomItem != null) {
                                    rule = randomItem.getRule();
                                }

                                EventStimulus stimulus = new EventStimulus(receiver, rule);
                                agentAction = new EvaluateStimulus(stimulus);
                            }

                            AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(receiver, agentAction);

                            EmotionalState emotionalState = agentState.getEmotionState().toEmotionalState();

                            socialEmotionCalculator.addEmotionalState(emotionalState);
                            emotionalSpaceGraphic.addEmotion(agentToAdd.getAgentName(), emotionalState);
                            agentsBehaviourModificationChartGui.addBehaviourType(i, agentToAdd.getAgentName(), agentState);
                            return agentState;
                        })
                        .collect(Collectors.toList());


                EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
                CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotionalState();
                MaximumDistances maximumDistances = socialEmotionCalculator.getMaximumDistances();

                dispersionGraphic.addDispersion(i, emotionalDispersion);
                maxDistancesGraphic.addMaximumDistances(i, maximumDistances);

                configuratorAgentGui.setCentralEmotion(centralEmotionalState.toEmotionalState());
                configuratorAgentGui.setEmotionalDispersion(emotionalDispersion);
                configuratorAgentGui.setMaximumDistance(maximumDistances);
                configuratorAgentGui.setAgentStates(agentStates);
                configuratorAgentGui.setActualIteration(i);

                emotionalSpaceGraphic.addEmotion(centralEmotionName, centralEmotionalState.toEmotionalState());

                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        };

        addBehaviour(configuratorBehaviour);

        configuratorAgentGui.modeSimulation();

        emotionalSpaceGraphic.start();
        dispersionGraphic.start();
        maxDistancesGraphic.start();
        agentsBehaviourModificationChartGui.start();
    }

    private void cleanSimulation() {
        if (configuratorBehaviour != null) {
            removeBehaviour(configuratorBehaviour);
        }

        if (dispersionGraphic != null) {
            dispersionGraphic.stop();
        }

        if (maxDistancesGraphic != null) {
            maxDistancesGraphic.stop();
        }

        if (emotionalSpaceGraphic != null) {
            emotionalSpaceGraphic.stop();
        }

        if (agentsBehaviourModificationChartGui != null) {
            agentsBehaviourModificationChartGui.stop();
        }

        List<AgentToAdd> agentsToAdd = configuratorAgentGui.getAgentsToAdd();

        List<AID> agents = agentManagementAssistant.agents();

        agentsToAdd.forEach(agentToAdd -> {
            try {
                AID gui = getAID(agentToAdd.getAgentName() + "_GUI");
                if (agents.contains(gui)) {
                    agentManagementAssistant.killAgent(gui);
                }

                AID aid = getAID(agentToAdd.getAgentName());
                if (agents.contains(aid)) {
                    agentManagementAssistant.killAgent(aid);
                }
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
