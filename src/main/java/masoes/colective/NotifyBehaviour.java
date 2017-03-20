/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.notifier.NotifyEvent;
import masoes.ontology.notifier.NotifyObject;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.OntologyAssistant;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import util.ServiceBuilder;

import java.util.Arrays;
import java.util.List;

public class NotifyBehaviour extends OntologyResponderBehaviour {

    private final AgentManagementAssistant agentManagementAssistant;
    private final OntologyAssistant ontologyAssistant;
    private Agent agent;

    public NotifyBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        this.agent = agent;
        agentManagementAssistant = new AgentManagementAssistant(agent);
        ontologyAssistant = new OntologyAssistant(agent, MasoesOntology.getInstance());
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(NotifyAction.class, NotifyEvent.class, NotifyObject.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        if (action.getAction() instanceof NotifyAction) {
            notifyAction((NotifyAction) action.getAction());
        } else if (action.getAction() instanceof NotifyEvent) {
            notifyEvent((NotifyEvent) action.getAction());
        } else if (action.getAction() instanceof NotifyObject) {
            notifyObject((NotifyObject) action.getAction());
        }

        return new Done(action);
    }

    private void notifyObject(NotifyObject notifyObject) {
        ServiceDescription serviceDescription = new ServiceBuilder()
                .name(MasoesOntology.ACTION_EVALUATE_STIMULUS)
                .build();

        List<AID> emotionalAgents = agentManagementAssistant.search(serviceDescription);

        emotionalAgents.remove(notifyObject.getObjectStimulus().getCreator());

        emotionalAgents.forEach(aid -> {
            ObjectStimulus objectStimulus = notifyObject.getObjectStimulus();
            EvaluateStimulus evaluateStimulus = new EvaluateStimulus(objectStimulus);
            ACLMessage requestAction = ontologyAssistant.createRequestAction(aid, evaluateStimulus);
            agent.send(requestAction);
        });
    }

    private void notifyEvent(NotifyEvent notifyEvent) {
        ServiceDescription serviceDescription = new ServiceBuilder()
                .name(MasoesOntology.ACTION_EVALUATE_STIMULUS)
                .build();

        List<AID> emotionalAgents = agentManagementAssistant.search(serviceDescription);

        emotionalAgents.remove(notifyEvent.getEventStimulus().getAffected());

        emotionalAgents.forEach(aid -> {
            EventStimulus eventStimulus = notifyEvent.getEventStimulus();
            EvaluateStimulus evaluateStimulus = new EvaluateStimulus(eventStimulus);
            ACLMessage requestAction = ontologyAssistant.createRequestAction(aid, evaluateStimulus);
            agent.send(requestAction);
        });
    }

    private void notifyAction(NotifyAction notifyAction) {
        ServiceDescription serviceDescription = new ServiceBuilder()
                .name(MasoesOntology.ACTION_EVALUATE_STIMULUS)
                .build();

        List<AID> emotionalAgents = agentManagementAssistant.search(serviceDescription);

        emotionalAgents.remove(notifyAction.getActionStimulus().getActor());

        emotionalAgents.forEach(aid -> {
            ActionStimulus actionStimulus = notifyAction.getActionStimulus();
            EvaluateStimulus evaluateStimulus = new EvaluateStimulus(actionStimulus);
            ACLMessage requestAction = ontologyAssistant.createRequestAction(aid, evaluateStimulus);
            agent.send(requestAction);
        });
    }

}
