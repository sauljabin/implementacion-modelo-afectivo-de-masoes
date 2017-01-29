/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.ontology;

import jade.content.onto.basic.Action;
import jade.lang.acl.MessageTemplate;
import jade.ontology.BaseOntology;
import jade.protocol.OntologyResponderBehaviour;

public class NotUnderstoodBehaviourResponderBehaviour extends OntologyResponderBehaviour {

    public NotUnderstoodBehaviourResponderBehaviour() {
        super(null, MessageTemplate.MatchAll(), new BaseOntology());
    }

    @Override
    public boolean isValidAction(Action action) {
        return false;
    }

}
