/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional.setting;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.app.setting.Setting;
import masoes.jade.setting.SettingsAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;
import test.common.TestUtility;

public class ShouldReceiveAllSettingsTest extends FunctionalTest {

    public static final int TIMEOUT = 6000;
    public static final String AID_SETTINGS = "settings";

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT);

        TestUtility.createAgent(tester, AID_SETTINGS, SettingsAgent.class.getName(), null);

        OneShotBehaviour sendMessageBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(new AID(AID_SETTINGS, AID.ISLOCALNAME));
                myAgent.send(testMessage);
            }
        };

        CyclicBehaviour receiveMessageBehaviour = new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    assertEquals(msg.getContent(), Setting.allToString());
                } else {
                    block();
                }
            }
        };

        SequentialBehaviour testerBehaviour = new SequentialBehaviour();
        testerBehaviour.addSubBehaviour(sendMessageBehaviour);
        testerBehaviour.addSubBehaviour(receiveMessageBehaviour);

        return testerBehaviour;
    }

}
