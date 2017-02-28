/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
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
import ontology.OntologyAssistant;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.ActionStimulus;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.MasoesOntology;
import ontology.masoes.NotifyAction;
import ontology.masoes.Stimulus;
import util.ServiceBuilder;

import java.util.Arrays;
import java.util.List;

public class NotifierAgentBehaviour extends OntologyResponderBehaviour {

    private final AgentManagementAssistant agentManagementAssistant;
    private final OntologyAssistant ontologyAssistant;
    private Agent agent;

    public NotifierAgentBehaviour(Agent agent) {
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

            emotionalAgents.forEach(aid -> {

                Stimulus stimulus = new ActionStimulus(notifyAction.getActor(), notifyAction.getActionName());
                EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);

                ACLMessage requestAction = ontologyAssistant.createRequestAction(aid, evaluateStimulus);
                agent.send(requestAction);
            });
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }
        return new Done(action);
    }

}
