/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.protocol.OntologyResponderBehaviour;
import logger.jade.JadeLogger;
import masoes.message.MasoesMessageTemplate;
import masoes.model.EmotionalAgent;
import masoes.ontology.AgentStatus;
import masoes.ontology.EmotionStatus;
import masoes.ontology.GetAgentStatus;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ResponseAgentStatusBehaviour extends OntologyResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private JadeLogger logger;

    public ResponseAgentStatusBehaviour(EmotionalAgent agent) {
        super(agent, new MasoesMessageTemplate());
        logger = new JadeLogger(LoggerFactory.getLogger(ResponseAgentStatusBehaviour.class));
        emotionalAgent = agent;
    }

    @Override
    public Predicate performAction(Action action) {
        AgentStatus agentStatus = new AgentStatus();
        agentStatus.setEmotionName(emotionalAgent.getCurrentEmotion().getEmotionName());
        agentStatus.setBehaviourName(emotionalAgent.getCurrentEmotionalBehaviour().getBehaviourName());
        agentStatus.setAgent(emotionalAgent.getAID());
        agentStatus.setEmotionStatus(new EmotionStatus(emotionalAgent.getEmotionalState().getActivation(), emotionalAgent.getEmotionalState().getSatisfaction()));
        return agentStatus;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetAgentStatus.class)
                .contains(action.getAction().getClass());
    }

}
