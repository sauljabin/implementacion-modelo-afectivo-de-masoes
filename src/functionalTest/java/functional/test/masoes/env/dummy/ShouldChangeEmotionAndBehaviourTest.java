/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.masoes.env.dummy;

import functional.test.core.FunctionalTest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import masoes.env.dummy.agent.DummyEmotionalAgent;
import test.common.TestException;

import java.util.Optional;

public class ShouldChangeEmotionAndBehaviourTest extends FunctionalTest {

    private String firstEmotionalResponse;

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID dummyAgentAID = createAgent(tester, DummyEmotionalAgent.class.getName());

        SimpleBehaviour receiveFirstMessageBehaviour = new SimpleBehaviour() {
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
                if (Optional.ofNullable(msg).isPresent()) {
                    firstEmotionalResponse = msg.getContent();
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

        OneShotBehaviour sendStimulusBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage testMessage = new ACLMessage(ACLMessage.INFORM);
                testMessage.addReceiver(dummyAgentAID);
                myAgent.send(testMessage);
            }
        };

        SimpleBehaviour receiveSecondMessageBehaviour = new SimpleBehaviour() {
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
                if (Optional.ofNullable(msg).isPresent()) {
                    String secondEmotionalResponse = msg.getContent();
                    assertNotEquals("Content", firstEmotionalResponse, secondEmotionalResponse);
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
        testerBehaviour.addSubBehaviour(receiveFirstMessageBehaviour);
        testerBehaviour.addSubBehaviour(sendStimulusBehaviour);
        testerBehaviour.addSubBehaviour(receiveSecondMessageBehaviour);

        return testerBehaviour;
    }

}
