/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.MessageTemplate;

public class OntologyResponderValidActionBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new OntologyResponderBehaviour(myAgent, MessageTemplate.MatchAll(), JADEManagementOntology.getInstance()) {
            @Override
            public boolean isValidAction(Action action) {
                return true;
            }

            @Override
            public Predicate performAction(Action action) throws FailureException {
                return new Done(action);
            }
        });
    }

}
