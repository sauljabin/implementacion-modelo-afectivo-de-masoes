/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.basic.Action;
import jade.lang.acl.MessageTemplate;
import protocol.OntologyResponderBehaviour;

public class NotUnderstoodBehaviourResponderBehaviour extends OntologyResponderBehaviour {

    public NotUnderstoodBehaviourResponderBehaviour() {
        super(null, MessageTemplate.MatchAll(), BasicOntology.getInstance());
    }

    @Override
    public boolean isValidAction(Action action) {
        return false;
    }

}
