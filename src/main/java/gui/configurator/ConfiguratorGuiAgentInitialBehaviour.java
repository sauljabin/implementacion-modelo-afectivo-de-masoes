/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator;

import environment.Environment;
import gui.configurator.stimulus.StimulusModel;
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

import static java.util.stream.Collectors.joining;

public class ConfiguratorGuiAgentInitialBehaviour extends OneShotBehaviour {

    private Translation translation = Translation.getInstance();
    private ConfiguratorGuiAgent configuratorAgent;
    private OntologyAssistant assistant;

    public ConfiguratorGuiAgentInitialBehaviour(ConfiguratorGuiAgent configuratorAgent) {
        this.configuratorAgent = configuratorAgent;
        assistant = new OntologyAssistant(configuratorAgent, MasoesOntology.getInstance());
    }

    @Override
    public void action() {
        TextFileWriter writer = new TextFileWriter("output/wikipedia", "initial.txt");

        writer.append("%s: %s", translation.get("gui.iterations").toUpperCase(), configuratorAgent.getConfiguratorGui().getIterationsSpinner().getValue());

        writer.newLine(2);

        writer.append(translation.get("gui.stimuli_configuration").toUpperCase());

        String stimuliHeaderFormat = "%40s %40s %7s %7s %15s";

        writer.append(stimuliHeaderFormat,
                translation.get("gui.stimulus"),
                translation.get("gui.value"),
                translation.get("gui.pa"),
                translation.get("gui.ps"),
                translation.get("gui.condition")
        );

        configuratorAgent.getStimulusTableModel().getStimuli().forEach(stimulus ->
                writer.append(stimuliHeaderFormat,
                        stimulus.getName(),
                        stimulus.getValue(),
                        StringFormatter.toString(stimulus.getActivation()),
                        StringFormatter.toString(stimulus.getSatisfaction()),
                        stimulus.isSelf() ? translation.get("gui.self") : translation.get("gui.others"))
        );

        writer.newLine(2);

        writer.append(translation.get("gui.initial_agent_configuration").toUpperCase());

        String agentsHeaderFormat = "%15s %15s %25s %7s %7s %15s| %s";

        writer.append(agentsHeaderFormat,
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour"),
                translation.get("gui.stimuli")
        );

        configuratorAgent.getAgentTableModel().getAgents().forEach(agent -> {
            AID receiver = myAgent.getAID(agent.getName());

            AgentAction agentAction = new GetEmotionalState();

            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);


            writer.append(agentsHeaderFormat,
                    agent.getName(),
                    translation.get(agentState.getEmotionState().getName().toLowerCase()),
                    translation.get(agentState.getEmotionState().getType().toLowerCase()),
                    StringFormatter.toString(agentState.getEmotionState().getActivation()),
                    StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                    translation.get(agentState.getBehaviourState().getType().toLowerCase()),
                    agent.getStimuli().stream().map(StimulusModel::getName).collect(joining(", "))
            );

        });

        SocialEmotion socialEmotion = (SocialEmotion) assistant.sendRequestAction(
                myAgent.getAID(Environment.SOCIAL_EMOTION_AGENT),
                new GetSocialEmotion()
        );

        writer.newLine(2);

        writer.append(translation.get("gui.social_emotion").toUpperCase());

        EmotionalDispersion emotionalDispersion = socialEmotion.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotion.getCentralEmotion();
        MaximumDistance maximumDistances = socialEmotion.getMaximumDistance();

        Emotion emotion = AffectiveModel.getInstance().searchEmotion(centralEmotionalState.toEmotionalState());

        writer.append("%30s: %s %s - %s",
                translation.get("gui.central_emotion"),
                StringFormatter.toStringPoint(centralEmotionalState.getActivation(),
                        centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                translation.get(emotion.getType().toString().toLowerCase())
        );

        writer.append("%30s: %s",
                translation.get("gui.maximum_distance"),
                StringFormatter.toStringPoint(maximumDistances.getActivation(),
                        maximumDistances.getSatisfaction())
        );

        writer.append("%30s: %s",
                translation.get("gui.emotional_dispersion"),
                StringFormatter.toStringPoint(emotionalDispersion.getActivation(),
                        emotionalDispersion.getSatisfaction())
        );

        writer.close();
    }

}
