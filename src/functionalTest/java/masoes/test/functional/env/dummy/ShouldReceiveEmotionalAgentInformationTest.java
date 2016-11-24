/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional.env.dummy;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.env.dummy.DummyEmotionalAgent;
import masoes.test.functional.FunctionalTest;
import masoes.util.collection.MapParser;
import test.common.TestException;

import java.util.Map;

public class ShouldReceiveEmotionalAgentInformationTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID dummyAgentAID = createAgent(tester, DummyEmotionalAgent.class.getName());

        OneShotBehaviour sendMessageBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                testMessage.addReceiver(dummyAgentAID);
                myAgent.send(testMessage);
            }
        };

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {
            private boolean done = false;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    MapParser mapParser = new MapParser();
                    Map<String, String> stringMap = mapParser.parseMap(msg.getContent());
                    assertEquals("Content [agent]", dummyAgentAID.getName(), stringMap.get("agent"));
                    assertNotNull("Content [emotion]", stringMap.get("emotion"));
                    assertNotNull("Content [behaviour]", stringMap.get("behaviour"));
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