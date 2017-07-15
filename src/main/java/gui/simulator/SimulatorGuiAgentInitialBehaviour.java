/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import environment.Environment;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import jade.content.AgentAction;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.CentralEmotion;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.MaximumDistance;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import translate.Translation;
import util.StringFormatter;
import util.TextFileWriter;
import util.ToStringBuilder;

import static java.util.stream.Collectors.joining;

public class SimulatorGuiAgentInitialBehaviour extends OneShotBehaviour {

    public static final String FILE_RESULT_OUTPUT = "results.txt";
    public static final String FOLDER_RESULT_OUTPUT = "output/simulation";

    private Translation translation = Translation.getInstance();
    private SimulatorGuiAgent simulatorGuiAgent;
    private OntologyAssistant assistant;

    public SimulatorGuiAgentInitialBehaviour(SimulatorGuiAgent simulatorGuiAgent) {
        this.simulatorGuiAgent = simulatorGuiAgent;
        assistant = new OntologyAssistant(simulatorGuiAgent, MasoesOntology.getInstance());
    }

    @Override
    public void action() {
        TextFileWriter writerResults = new TextFileWriter(FOLDER_RESULT_OUTPUT, FILE_RESULT_OUTPUT);

        writerResults.append("%s: %s", translation.get("gui.iterations").toUpperCase(), simulatorGuiAgent.getSimulatorGui().getIterationsSpinner().getValue());

        writerResults.newLine(2);

        writerResults.append(translation.get("gui.stimuli_configuration").toUpperCase());

        String stimuliHeaderFormat = "%40s %40s %7s %7s %15s";

        writerResults.append(stimuliHeaderFormat,
                translation.get("gui.stimulus"),
                translation.get("gui.value"),
                translation.get("gui.pa"),
                translation.get("gui.ps"),
                translation.get("gui.condition")
        );

        simulatorGuiAgent.getStimulusDefinitionModels().forEach(stimulus ->
                writerResults.append(stimuliHeaderFormat,
                        stimulus.getName(),
                        stimulus.getValue(),
                        StringFormatter.toString(stimulus.getActivation()),
                        StringFormatter.toString(stimulus.getSatisfaction()),
                        stimulus.isSelf() ? translation.get("gui.self") : translation.get("gui.others"))
        );

        writerResults.newLine(2);

        writerResults.append(translation.get("gui.initial_agent_configuration").toUpperCase());

        String agentsHeaderFormat = "%25s %20s %25s %7s %7s %15s| %s";

        writerResults.append(agentsHeaderFormat,
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour"),
                translation.get("gui.stimuli")
        );

        simulatorGuiAgent.getAgentConfigurationModels().forEach(agent -> {
            AID receiver = myAgent.getAID(agent.getName());

            AgentAction agentAction = new GetEmotionalState();

            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);

            writerResults.append(agentsHeaderFormat,
                    agent.getName(),
                    translation.get(agentState.getEmotionState().getName().toLowerCase()),
                    translation.get(agentState.getEmotionState().getType().toLowerCase()),
                    StringFormatter.toString(agentState.getEmotionState().getActivation()),
                    StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                    translation.get(agentState.getBehaviourState().getType().toLowerCase()),
                    agent.getStimulusConfigurations()
                            .stream()
                            .filter(StimulusConfigurationModel::isSelected)
                            .map(stimulusConfiguration ->
                                    new ToStringBuilder()
                                            .append(translation.get("gui.stimulus"), stimulusConfiguration.getStimulusDefinition().getName())
                                            .append(translation.get("gui.pa"), StringFormatter.toString(stimulusConfiguration.getActivation()))
                                            .append(translation.get("gui.ps"), StringFormatter.toString(stimulusConfiguration.getSatisfaction()))
                                            .append(translation.get("gui.condition"), stimulusConfiguration.isSelf() ? translation.get("gui.self") : translation.get("gui.others"))
                                            .toString()
                            )
                            .collect(joining(", "))
            );

        });

        SocialEmotion socialEmotion = (SocialEmotion) assistant.sendRequestAction(
                myAgent.getAID(Environment.SOCIAL_EMOTION_AGENT),
                new GetSocialEmotion()
        );

        writerResults.newLine(2);

        writerResults.append(translation.get("gui.social_emotion").toUpperCase());

        EmotionalDispersion emotionalDispersion = socialEmotion.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotion.getCentralEmotion();
        MaximumDistance maximumDistances = socialEmotion.getMaximumDistance();

        Emotion emotion = AffectiveModel.getInstance().searchEmotion(centralEmotionalState.toEmotionalState());

        writerResults.append("%30s: %s %s - %s",
                translation.get("gui.central_emotion"),
                StringFormatter.toStringPoint(centralEmotionalState.getActivation(),
                        centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                translation.get(emotion.getType().toString().toLowerCase())
        );

        writerResults.append("%30s: %s",
                translation.get("gui.maximum_distance"),
                StringFormatter.toStringPoint(maximumDistances.getActivation(),
                        maximumDistances.getSatisfaction())
        );

        writerResults.append("%30s: %s",
                translation.get("gui.emotional_dispersion"),
                StringFormatter.toStringPoint(emotionalDispersion.getActivation(),
                        emotionalDispersion.getSatisfaction())
        );

        writerResults.close();
    }

}
