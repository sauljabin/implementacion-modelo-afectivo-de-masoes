/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.MessageTemplate;

public class OntologyResponderInvalidActionBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new OntologyResponderBehaviour(myAgent, MessageTemplate.MatchAll(), JADEManagementOntology.getInstance()) {
            @Override
            public boolean isValidAction(Action action) {
                return false;
            }
        });
    }

}
