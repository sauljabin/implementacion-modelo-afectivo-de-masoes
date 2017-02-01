/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import jade.content.Predicate;
import jade.content.onto.BasicOntology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import protocol.OntologyResponderBehaviour;

public class ValidActionResponderBehaviour extends OntologyResponderBehaviour {

    public ValidActionResponderBehaviour() {
        super(null, MessageTemplate.MatchAll(), BasicOntology.getInstance());
    }

    @Override
    public boolean isValidAction(Action action) {
        return true;
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        return new Done();
    }

}
