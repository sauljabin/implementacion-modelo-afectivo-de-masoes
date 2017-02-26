/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import ontology.OntologyMatchExpression;
import ontology.OntologyResponderBehaviour;
import ontology.masoes.MasoesOntology;
import ontology.masoes.NotifyAction;

import java.util.Arrays;

public class NotifierAgentBehaviour extends OntologyResponderBehaviour {

    private Agent agent;

    public NotifierAgentBehaviour(Agent agent) {
        super(agent, new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), MasoesOntology.getInstance());
        this.agent = agent;
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(NotifyAction.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        return super.performAction(action);
    }

}
