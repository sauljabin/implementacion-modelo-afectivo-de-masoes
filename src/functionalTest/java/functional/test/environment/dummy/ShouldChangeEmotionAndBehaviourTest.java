/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.environment.dummy;

import functional.test.core.FunctionalTest;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.ontology.base.ActionResult;
import masoes.environment.dummy.DummyEmotionalAgent;
import masoes.ontology.AgentStatus;
import masoes.ontology.EvaluateStimulus;
import masoes.ontology.GetAgentStatus;
import masoes.ontology.MasoesOntology;
import masoes.ontology.Stimulus;
import settings.ontology.SettingsOntology;
import test.common.TestException;

public class ShouldChangeEmotionAndBehaviourTest extends FunctionalTest {

    private AgentStatus firstEmotionalResponse;

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID dummyAgentAID = createAgent(tester, DummyEmotionalAgent.class.getName());

        SimpleBehaviour receiveFirstMessageBehaviour = new OneShotBehaviour() {

            @Override
            public void action() {
                try {
                    ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                    testMessage.addReceiver(dummyAgentAID);
                    testMessage.setOntology(MasoesOntology.ONTOLOGY_NAME);
                    testMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
                    testMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                    myAgent.getContentManager().registerLanguage(new SLCodec());
                    myAgent.getContentManager().registerOntology(new MasoesOntology());

                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), new GetAgentStatus()));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().messageRequest(myAgent, msg);

                    firstEmotionalResponse = (AgentStatus) myAgent.getContentManager().extractContent(msg);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

        };

        OneShotBehaviour sendStimulusBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                    testMessage.addReceiver(dummyAgentAID);
                    testMessage.setOntology(MasoesOntology.ONTOLOGY_NAME);
                    testMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
                    testMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                    myAgent.getContentManager().registerLanguage(new SLCodec());
                    myAgent.getContentManager().registerOntology(new SettingsOntology());

                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), new EvaluateStimulus(new Stimulus())));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().messageRequest(myAgent, msg);

                    ActionResult actionResult = (ActionResult) myAgent.getContentManager().extractContent(msg);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        };

        SimpleBehaviour receiveSecondMessageBehaviour = new OneShotBehaviour() {

            @Override
            public void action() {
                try {
                    ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                    testMessage.addReceiver(dummyAgentAID);
                    testMessage.setOntology(MasoesOntology.ONTOLOGY_NAME);
                    testMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
                    testMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                    myAgent.getContentManager().registerLanguage(new SLCodec());
                    myAgent.getContentManager().registerOntology(new SettingsOntology());

                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), new GetAgentStatus()));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().messageRequest(myAgent, msg);

                    AgentStatus agentStatus = (AgentStatus) myAgent.getContentManager().extractContent(msg);
                    assertReflectionNotEquals("Agent Status", firstEmotionalResponse, agentStatus);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

        };

        SequentialBehaviour testerBehaviour = new SequentialBehaviour();
        testerBehaviour.addSubBehaviour(receiveFirstMessageBehaviour);
        testerBehaviour.addSubBehaviour(sendStimulusBehaviour);
        testerBehaviour.addSubBehaviour(receiveSecondMessageBehaviour);

        return testerBehaviour;
    }

}
