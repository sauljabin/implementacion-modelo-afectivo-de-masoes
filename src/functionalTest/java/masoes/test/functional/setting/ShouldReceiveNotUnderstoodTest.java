/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional.setting;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.jade.agent.SettingsAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;

public class ShouldReceiveNotUnderstoodTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private boolean done = false;

            @Override
            public void onStart() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(settingsAgentAID);
                testMessage.setContent("no_key");
                myAgent.send(testMessage);
            }

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    assertEquals("Performative", ACLMessage.NOT_UNDERSTOOD, msg.getPerformative());
                    done = true;
                } else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return done;
            }
        };

        return receiveMessageBehaviour;
    }

}
