/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import behaviour.CounterBehaviour;
import environment.wikipedia.chart.AgentsAffectiveModelChartGui;
import environment.wikipedia.chart.AgentsBehaviourModificationChartGui;
import environment.wikipedia.chart.AgentsEmotionModificationChartGui;
import environment.wikipedia.chart.AgentsEmotionalStateChartGui;
import environment.wikipedia.chart.EmotionalDispersionLineChartGui;
import environment.wikipedia.chart.MaximumDistancesLineChartGui;
import gui.state.AffectiveModelChartGuiAgent;
import jade.content.AgentAction;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.MasoesSettings;
import masoes.collective.SocialEmotionCalculator;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfiguratorAgent extends GuiAgent {

    public static final int DIALOG_DISTANCE = 30;
    private static final int FPS = 5;
    private static final String CONTRIBUTOR_KNOWLEDGE = "theories/behavioural/wikipedia/contributorEmotionalAgent.prolog";
    private List<KnowledgeRule> knowledgeRules;
    private AgentLogger logger;
    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;
    private AffectiveModel affectiveModel;
    private Behaviour configuratorBehaviour;
    private SocialEmotionCalculator socialEmotionCalculator;
    private EmotionalDispersionLineChartGui dispersionGraphic;
    private MaximumDistancesLineChartGui maxDistancesGraphic;
    private AgentsAffectiveModelChartGui agentsAffectiveModelChartGui;
    private AgentsBehaviourModificationChartGui agentsBehaviourModificationChartGui;
    private AgentsEmotionalStateChartGui agentsEmotionalStateChartGui;
    private AgentsEmotionModificationChartGui agentsEmotionModificationChartGui;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
        affectiveModel = AffectiveModel.getInstance();
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
                case UPDATE_SATISFACTION_PARAMETER:
                    updateSatisfactionParameter();
                    break;
                case UPDATE_ACTIVATION_PARAMETER:
                    updateActivationParameter();
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
                case SAVE:
                    saveResults();
                    break;
                case DESELECT_ALL_AGENTS_TO_ADD:
                    deselectAllAgentsToAdd();
                    break;
                case SELECT_ALL_AGENTS_TO_ADD:
                    selectAllAgentsToAdd();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

    private void deselectAllAgentsToAdd() {
        configuratorAgentGui.deselectAllAgentsToAdd();
    }

    private void selectAllAgentsToAdd() {
        configuratorAgentGui.selectAllAgentsToAdd();
    }

    private void saveResults() {
        try {
            File folder = new File("output/wikipedia");
            folder.mkdirs();

            File file = new File(folder, "output.txt");

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            out.write("WIKIPEDIA\n");

            out.write("\n");
            Translation translation = Translation.getInstance();

            out.write(String.format("%s:\n", translation.get("gui.global_values").toUpperCase()));

            out.write(String.format("%s: %s\n", translation.get("gui.iterations"), configuratorAgentGui.getIterations()));

            String eventFrequency = configuratorAgentGui.isEventFrequencyRandom() ?
                    translation.get("gui.random")
                    : Integer.toString(configuratorAgentGui.getEventFrequency());

            out.write(String.format("%s: %s\n", translation.get("gui.event_frequency"), eventFrequency));
            out.write(String.format("%s: %s\n", translation.get("gui.activation_parameter"), configuratorAgentGui.getActivationParameter()));
            out.write(String.format("%s: %s\n", translation.get("gui.satisfaction_parameter"), configuratorAgentGui.getSatisfactionParameter()));

            out.write("\n");

            out.write(String.format("%s:\n", translation.get("gui.events").toUpperCase()));

            knowledgeRules.stream()
                    .filter(KnowledgeRule::isSelected)
                    .forEach(knowledgeRule -> {
                        try {
                            out.write(String.format("%s [%s: %s, %s: %s]\n",
                                    translation.get(knowledgeRule.getRule().toLowerCase().trim()),
                                    translation.get("gui.activation"),
                                    translation.get(knowledgeRule.getActivation().toLowerCase().trim()),
                                    translation.get("gui.satisfaction"),
                                    translation.get(knowledgeRule.getSatisfaction().toLowerCase().trim())
                            ));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            out.write("\n");

            out.write(String.format("%s:\n", translation.get("gui.initial_agent_configuration").toUpperCase()));

            configuratorAgentGui.getAgentsToAdd()
                    .forEach(agentToAdd -> {
                        try {
                            out.write(String.format("%s [%s: %s, %s: %s]\n",
                                    agentToAdd.getAgentName(),
                                    translation.get("gui.emotional_state"),
                                    agentToAdd.getEmotionalStateString(),
                                    translation.get("gui.emotion"),
                                    agentToAdd.getEmotionName()
                            ));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            out.write("\n");

            out.write(String.format("%s:\n", translation.get("gui.final_emotional_states").toUpperCase()));

            configuratorAgentGui.getAgentsToAdd()
                    .forEach(agentToAdd -> {
                        AgentAction agentAction = new GetEmotionalState();
                        AID receiver = getAID(agentToAdd.getAgentName());
                        AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(receiver, agentAction);

                        try {
                            out.write(String.format("%s [%s: (%.3f, %.3f), %s: %s - %s, %s: %s]\n",
                                    agentToAdd.getAgentName(),
                                    translation.get("gui.emotional_state"),
                                    agentState.getEmotionState().getActivation(),
                                    agentState.getEmotionState().getSatisfaction(),
                                    translation.get("gui.emotion"),
                                    translation.get(agentState.getEmotionState().getName().toLowerCase()),
                                    translation.get(agentState.getEmotionState().getType().toLowerCase()),
                                    translation.get("gui.behaviour"),
                                    translation.get(agentState.getBehaviourState().getType().toLowerCase())
                            ));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            out.write("\n");

            out.write(String.format("%s:\n", translation.get("gui.social_emotion").toUpperCase()));

            EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
            CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotionalState();
            MaximumDistances maximumDistances = socialEmotionCalculator.getMaximumDistances();

            Emotion emotion = affectiveModel.searchEmotion(centralEmotionalState.toEmotionalState());

            out.write(String.format("%s: (%.3f, %.3f), %s - %s\n",
                    translation.get("gui.central_emotion"),
                    centralEmotionalState.getActivation(),
                    centralEmotionalState.getSatisfaction(),
                    translation.get(emotion.getName().toLowerCase()),
                    translation.get(emotion.getType().toString().toLowerCase())
            ));

            out.write(String.format("%s: (%.3f, %.3f)\n",
                    translation.get("gui.max_distance"),
                    maximumDistances.getActivation(),
                    maximumDistances.getSatisfaction()
            ));

            out.write(String.format("%s: (%.3f, %.3f)\n",
                    translation.get("gui.emotional_dispersion"),
                    emotionalDispersion.getActivation(),
                    emotionalDispersion.getSatisfaction()
            ));

            out.flush();
            out.close();

            int width = 600;
            int height = 400;

            agentsBehaviourModificationChartGui.exportImage(folder, width, height);
            agentsEmotionModificationChartGui.exportImage(folder, width, height);
            maxDistancesGraphic.exportImage(folder, width, height);
            dispersionGraphic.exportImage(folder, width, height);
            agentsEmotionalStateChartGui.exportImage(folder, width, height);
            agentsAffectiveModelChartGui.exportImage(folder, height);

        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

    private void showEmotionalStateGui() {
        AgentState agentState = configuratorAgentGui.getSelectedAgentState();

        agentManagementAssistant.createAgent(
                agentState.getAgent().getLocalName() + "_GUI",
                AffectiveModelChartGuiAgent.class,
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
        configuratorAgentGui.setEmotionToAdd(affectiveModel.searchEmotion(emotionalState));
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

        agentsEmotionalStateChartGui = new AgentsEmotionalStateChartGui(Translation.getInstance().get("gui.emotional_state"));
        agentsEmotionalStateChartGui.setLocation(100, 100);

        agentsAffectiveModelChartGui = new AgentsAffectiveModelChartGui(Translation.getInstance().get("gui.emotional_states"));
        agentsAffectiveModelChartGui.setLocation(100, 100);

        dispersionGraphic = new EmotionalDispersionLineChartGui(Translation.getInstance().get("gui.emotional_dispersion"));
        dispersionGraphic.setLocation(
                agentsAffectiveModelChartGui.getLocation().x + agentsAffectiveModelChartGui.getSize().width,
                agentsAffectiveModelChartGui.getLocation().y
        );

        maxDistancesGraphic = new MaximumDistancesLineChartGui(Translation.getInstance().get("gui.max_distance"));
        maxDistancesGraphic.setLocation(
                agentsAffectiveModelChartGui.getLocation().x + agentsAffectiveModelChartGui.getSize().width + dispersionGraphic.getSize().width,
                agentsAffectiveModelChartGui.getLocation().y
        );

        agentsBehaviourModificationChartGui = new AgentsBehaviourModificationChartGui(Translation.getInstance().get("gui.behaviour_modifications"));
        agentsBehaviourModificationChartGui.setLocation(
                agentsAffectiveModelChartGui.getLocation().x,
                agentsAffectiveModelChartGui.getLocation().y + agentsAffectiveModelChartGui.getSize().height + DIALOG_DISTANCE
        );

        agentsEmotionModificationChartGui = new AgentsEmotionModificationChartGui(Translation.getInstance().get("gui.emotion_modifications"));
        agentsEmotionModificationChartGui.setLocation(
                agentsAffectiveModelChartGui.getLocation().x + agentsAffectiveModelChartGui.getSize().width,
                agentsAffectiveModelChartGui.getLocation().y + agentsAffectiveModelChartGui.getSize().height + DIALOG_DISTANCE
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
            agentsEmotionModificationChartGui.addAgent(agentToAdd.getAgentName());
            agentsAffectiveModelChartGui.addAgent(agentToAdd.getAgentName());
            agentsEmotionalStateChartGui.addAgent(agentToAdd.getAgentName());
        });

        agentsAffectiveModelChartGui.addAgent(centralEmotionName);

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

                            if (i % eventFrequency == 0 && agentToAdd.isReceiveStimulus()) {
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
                            agentsAffectiveModelChartGui.addEmotionalState(agentToAdd.getAgentName(), emotionalState);
                            agentsEmotionalStateChartGui.addEmotionalState(agentToAdd.getAgentName(), i, emotionalState);
                            agentsBehaviourModificationChartGui.addBehaviourType(agentToAdd.getAgentName(), i, agentState);
                            agentsEmotionModificationChartGui.addEmotion(agentToAdd.getAgentName(), i, agentState);
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

                agentsAffectiveModelChartGui.addEmotionalState(centralEmotionName, centralEmotionalState.toEmotionalState());

                if (configuratorAgentGui.getIterations() == i) {
                    configuratorAgentGui.activateSaveButton();
                }

                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        };

        addBehaviour(configuratorBehaviour);

        configuratorAgentGui.simulationMode();

        agentsAffectiveModelChartGui.start();
        dispersionGraphic.start();
        maxDistancesGraphic.start();
        agentsBehaviourModificationChartGui.start();
        agentsEmotionModificationChartGui.start();
        agentsEmotionalStateChartGui.start();
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

        if (agentsAffectiveModelChartGui != null) {
            agentsAffectiveModelChartGui.stop();
        }

        if (agentsBehaviourModificationChartGui != null) {
            agentsBehaviourModificationChartGui.stop();
        }

        if (agentsEmotionModificationChartGui != null) {
            agentsEmotionModificationChartGui.stop();
        }

        if (agentsEmotionalStateChartGui != null) {
            agentsEmotionalStateChartGui.stop();
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
        configuratorAgentGui.configurationMode();
    }

    private void updateActivationParameter() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_ACTIVATION_PARAMETER, String.valueOf(configuratorAgentGui.getActivationParameter()));
    }

    private void updateSatisfactionParameter() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_SATISFACTION_PARAMETER, String.valueOf(configuratorAgentGui.getSatisfactionParameter()));
    }

}
