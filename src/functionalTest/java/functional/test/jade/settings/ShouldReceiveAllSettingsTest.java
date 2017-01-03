/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.jade.settings;

import application.settings.ApplicationSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import functional.test.core.FunctionalTest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.settings.JadeSettings;
import jade.settings.agent.SettingsAgent;
import test.common.TestException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShouldReceiveAllSettingsTest extends FunctionalTest {

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
                myAgent.send(testMessage);
            }

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (Optional.ofNullable(msg).isPresent()) {
                    getLogger().agentMessage(myAgent, msg);
                    try {
                        ObjectMapper mapper = new ObjectMapper();

                        Map<String, Object> objectMap = new HashMap<>();

                        objectMap.put("applicationSettings", ApplicationSettings.getInstance().toMap());
                        objectMap.put("jadeSettings", JadeSettings.getInstance().toMap());

                        assertEquals("Content", mapper.writeValueAsString(objectMap), msg.getContent());
                        assertEquals("Performative", ACLMessage.INFORM, msg.getPerformative());
                        done = true;
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
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
