/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.behaviour;

import jade.content.Concept;
import jade.content.onto.basic.Action;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.logger.JadeLogger;
import jade.ontology.base.UnexpectedContent;
import jade.protocol.ProtocolRequestResponderBehaviour;
import masoes.core.EmotionalAgent;
import masoes.core.ontology.AgentStatus;
import masoes.core.ontology.EmotionStatus;
import masoes.core.ontology.GetAgentStatus;
import masoes.core.ontology.MasoesOntology;
import org.slf4j.LoggerFactory;

public class ResponseAgentStatusBehaviour extends ProtocolRequestResponderBehaviour {

    private EmotionalAgent emotionalAgent;
    private MessageTemplate template;
    private JadeLogger logger;

    public ResponseAgentStatusBehaviour(EmotionalAgent agent) {
        super(agent);
        logger = new JadeLogger(LoggerFactory.getLogger(ResponseAgentStatusBehaviour.class));
        emotionalAgent = agent;
    }

    @Override
    public void onStart() {
        template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        template = MessageTemplate.and(template, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        template = MessageTemplate.and(template, MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL));
        template = MessageTemplate.and(template, MessageTemplate.MatchOntology(MasoesOntology.ONTOLOGY_NAME));
        setMessageTemplate(template);
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage request) {
        ACLMessage response = request.createReply();
        try {
            Action action = (Action) myAgent.getContentManager().extractContent(request);
            Concept agentAction = action.getAction();

            if (agentAction instanceof GetAgentStatus) {
                getAgentStatusResponse(response);
            } else {
                invalidActionResponse(request, response);
            }

        } catch (Exception e) {
            failureResponse(response, e);
        }
        return response;
    }

    private void getAgentStatusResponse(ACLMessage response) throws Exception {
        response.setPerformative(ACLMessage.INFORM);
        AgentStatus agentStatus = new AgentStatus();
        agentStatus.setEmotionName(emotionalAgent.getCurrentEmotion().getEmotionName());
        agentStatus.setBehaviourName(emotionalAgent.getCurrentEmotionalBehaviour().getBehaviourName());
        agentStatus.setAgent(emotionalAgent.getAID());
        agentStatus.setEmotionStatus(new EmotionStatus(emotionalAgent.getEmotionalState().getActivation(), emotionalAgent.getEmotionalState().getSatisfaction()));
        myAgent.getContentManager().fillContent(response, agentStatus);
    }

    private void invalidActionResponse(ACLMessage request, ACLMessage response) throws Exception {
        response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        UnexpectedContent unexpectedContent = new UnexpectedContent("Invalid agent action", request.getContent());
        myAgent.getContentManager().fillContent(response, unexpectedContent);
    }

    private void failureResponse(ACLMessage response, Exception e) {
        response.setPerformative(ACLMessage.FAILURE);
        response.setContent(e.getMessage());
        logger.agentException(myAgent, e);
    }

}
