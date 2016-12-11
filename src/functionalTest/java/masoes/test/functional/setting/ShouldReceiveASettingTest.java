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
import masoes.app.settings.ApplicationSettings;
import masoes.jade.agent.SettingsAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;

import java.util.Optional;

public class ShouldReceiveASettingTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
            private boolean done = false;

            @Override
            public void onStart() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(settingsAgentAID);
                testMessage.setContent(ApplicationSettings.APP_NAME);
                myAgent.send(testMessage);
            }

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (Optional.ofNullable(msg).isPresent()) {
                    assertEquals("Content", applicationSettings.get(ApplicationSettings.APP_NAME), msg.getContent());
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

        return receiveMessageBehaviour;
    }

}
