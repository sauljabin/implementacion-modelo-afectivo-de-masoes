/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import masoes.ontology.MasoesOntology;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.notifier.NotifyEvent;
import masoes.ontology.notifier.NotifyObject;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.Stimulus;
import ontology.OntologyAssistant;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import util.ServiceBuilder;

import java.util.Arrays;
import java.util.List;

public class NotifierBehaviour extends OntologyResponderBehaviour {

    private final AgentManagementAssistant agentManagementAssistant;
    private final OntologyAssistant ontologyAssistant;
    private Agent agent;

    public NotifierBehaviour(Agent agent) {
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
        List<AID> emotionalAgents = searchEmotionalAgents();
        emotionalAgents.remove(notifyObject.getObjectStimulus().getCreator());
        sendStimulus(emotionalAgents, notifyObject.getObjectStimulus());
    }

    private void notifyEvent(NotifyEvent notifyEvent) {
        List<AID> emotionalAgents = searchEmotionalAgents();
        sendStimulus(emotionalAgents, notifyEvent.getEventStimulus());
    }

    private void notifyAction(NotifyAction notifyAction) {
        List<AID> emotionalAgents = searchEmotionalAgents();
        emotionalAgents.remove(notifyAction.getActionStimulus().getActor());
        sendStimulus(emotionalAgents, notifyAction.getActionStimulus());
    }

    private List<AID> searchEmotionalAgents() {
        ServiceDescription serviceDescription = new ServiceBuilder()
                .name(MasoesOntology.ACTION_EVALUATE_STIMULUS)
                .build();

        return agentManagementAssistant.search(serviceDescription);
    }

    private void sendStimulus(List<AID> emotionalAgents, Stimulus stimulus) {
        emotionalAgents.forEach(aid -> {
            agent.send(ontologyAssistant.createRequestAction(aid, new EvaluateStimulus(stimulus)));
        });
    }

}
