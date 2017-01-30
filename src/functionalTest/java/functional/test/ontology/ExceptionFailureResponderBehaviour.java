/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.protocol.OntologyResponderBehaviour;
import ontology.BaseOntology;

public class ExceptionFailureResponderBehaviour extends OntologyResponderBehaviour {

    public ExceptionFailureResponderBehaviour() {
        super(null, MessageTemplate.MatchAll(), new BaseOntology());
    }

    @Override
    public boolean isValidAction(Action action) {
        return true;
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        throw new FailureException("MESSAGE FAILURE");
    }

}
