/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.protocol;

import jade.content.onto.basic.Action;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.MessageTemplate;
import protocol.OntologyResponderBehaviour;

public class OntologyResponderInvalidActionBehaviour extends OntologyResponderBehaviour {

    public OntologyResponderInvalidActionBehaviour() {
        super(null, MessageTemplate.MatchAll(), JADEManagementOntology.getInstance());
    }

    @Override
    public boolean isValidAction(Action action) {
        return false;
    }

}
