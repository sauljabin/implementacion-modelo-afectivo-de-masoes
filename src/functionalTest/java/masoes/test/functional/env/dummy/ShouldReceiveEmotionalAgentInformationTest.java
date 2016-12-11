/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional.env.dummy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.env.dummy.agent.DummyEmotionalAgent;
import masoes.test.functional.FunctionalTest;
import test.common.TestException;

import java.util.Map;

public class ShouldReceiveEmotionalAgentInformationTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID dummyAgentAID = createAgent(tester, DummyEmotionalAgent.class.getName());

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private boolean done = false;

            @Override
            public void onStart() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(dummyAgentAID);
                myAgent.send(testMessage);
            }

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();

                        Map<String, Object> stringMap = mapper.readValue(msg.getContent(), new TypeReference<Map<String, Object>>() {
                        });

                        assertEquals("Content [agent]", dummyAgentAID.getName(), stringMap.get("agent"));
                        assertNotNull("Content [emotion]", stringMap.get("emotion"));
                        assertNotNull("Content [behaviour]", stringMap.get("behaviour"));
                        assertNotNull("Content [state]", stringMap.get("state"));
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
