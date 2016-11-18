/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional.setting;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.jade.setting.SettingsAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;

public class ShouldReceiveNotUnderstoodTest extends FunctionalTest {

    public static final int TIMEOUT = 6000;

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT);

        AID settingsAgent = createAgent(tester, SettingsAgent.class.getName());

        OneShotBehaviour sendMessageBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(settingsAgent);
                testMessage.setContent("no_key");
                myAgent.send(testMessage);
            }
        };

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private boolean done = false;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    assertEquals("Performative", msg.getPerformative(), ACLMessage.NOT_UNDERSTOOD);
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

        SequentialBehaviour testerBehaviour = new SequentialBehaviour();
        testerBehaviour.addSubBehaviour(sendMessageBehaviour);
        testerBehaviour.addSubBehaviour(receiveMessageBehaviour);

        return testerBehaviour;
    }

}
