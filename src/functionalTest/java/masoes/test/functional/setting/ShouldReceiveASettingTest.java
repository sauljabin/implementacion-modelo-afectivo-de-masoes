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
import masoes.app.setting.Setting;
import masoes.jade.setting.SettingsAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;

public class ShouldReceiveASettingTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        OneShotBehaviour sendMessageBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(settingsAgentAID);
                testMessage.setContent(Setting.APP_NAME.getKey());
                myAgent.send(testMessage);
            }
        };

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private boolean done = false;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    assertEquals("Content", Setting.APP_NAME.getValue(), msg.getContent());
                    assertEquals("Performative", ACLMessage.INFORM, msg.getPerformative());
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