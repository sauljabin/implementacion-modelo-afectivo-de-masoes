/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol.ontology;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.ontology.base.ActionResult;
import jade.ontology.base.BaseOntology;
import jade.protocol.OntologyResponderBehaviour;

public class ValidActionResponderBehaviour extends OntologyResponderBehaviour {

    public ValidActionResponderBehaviour() {
        super(null, MessageTemplate.MatchAll(), new BaseOntology());
    }

    @Override
    public boolean isValidAction(Action action) {
        return true;
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        return new ActionResult("OK", (AgentAction) action.getAction());
    }

}
