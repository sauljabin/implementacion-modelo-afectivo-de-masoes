/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import behaviour.CounterBehaviour;
import environment.wikipedia.configurator.stimulus.StimulusModel;
import jade.content.AgentAction;
import jade.core.AID;
import masoes.MasoesSettings;
import masoes.collective.SocialEmotionCalculator;
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

public class ConfiguratorGuiAgentBehaviour extends CounterBehaviour {

    private OntologyAssistant assistant;
    private ConfiguratorGuiAgent configuratorAgent;
    private SocialEmotionCalculator socialEmotionCalculator;

    public ConfiguratorGuiAgentBehaviour(ConfiguratorGuiAgent configuratorAgent, int maxCount) {
        super(configuratorAgent, maxCount);
        this.configuratorAgent = configuratorAgent;
        assistant = new OntologyAssistant(configuratorAgent, MasoesOntology.getInstance());
        socialEmotionCalculator = new SocialEmotionCalculator();
    }

    @Override
    public void count(int i) {
        setIteration(i);
        updateStates();
        sleep();
    }

    private void updateStates() {
        socialEmotionCalculator.clear();

        configuratorAgent.getAgentTableModel().getAgents().forEach(agent -> {
            AID receiver = myAgent.getAID(agent.getName());

            AgentAction agentAction = new GetEmotionalState();

            if (!agent.getStimuli().isEmpty()) {
                StimulusModel randomItem = RandomGenerator.getRandomItem(agent.getStimuli());
                EventStimulus stimulus = new EventStimulus(receiver, randomItem.getValue());
                agentAction = new EvaluateStimulus(stimulus);
            }

            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);

            configuratorAgent.getAgentStateTableModel().addAgent(agentState);

            socialEmotionCalculator.addEmotionalState(agentState.getEmotionState().toEmotionalState());
        });

        setGuiSocialEmotion();
    }

    private void setGuiSocialEmotion() {
        EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotionalState();
        MaximumDistances maximumDistances = socialEmotionCalculator.getMaximumDistances();

        configuratorAgent.getConfiguratorGui().getCollectiveCentralEmotionalStateLabel()
                .setText(centralEmotionalState.toStringPoint());

        configuratorAgent.getConfiguratorGui().getCollectiveCentralEmotionLabel()
                .setText(
                        translate(centralEmotionalState.getEmotion().getName()) +
                                " - " +
                                translate(centralEmotionalState.getEmotion().getType().toString())
                );

        configuratorAgent.getConfiguratorGui().getEmotionalDispersionValueLabel()
                .setText(emotionalDispersion.toStringPoint());

        configuratorAgent.getConfiguratorGui().getMaxDistanceEmotionValueLabel()
                .setText(maximumDistances.toStringPoint());
    }

    private String translate(String key) {
        return Translation.getInstance().get(key.toLowerCase());
    }

    private void setIteration(int i) {
        configuratorAgent.getConfiguratorGui().getIterationLabel().setText(String.valueOf(i));
    }

    private void sleep() {
        myAgent.doWait(1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS)));
    }

}
