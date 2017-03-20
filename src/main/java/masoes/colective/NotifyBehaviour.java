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
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
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
        return Arrays.asList(NotifyAction.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        try {
            NotifyAction notifyAction = (NotifyAction) action.getAction();

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
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
        return new Done(action);
    }

}
