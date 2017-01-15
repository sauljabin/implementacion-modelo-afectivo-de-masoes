/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.jade.settings;

import functional.test.core.FunctionalTest;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.language.FipaLanguage;
import jade.ontology.base.UnexpectedContent;
import jade.settings.agent.SettingsAgent;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.SettingsOntology;
import test.common.TestException;

public class ShouldReceiveNotUnderstoodTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        SimpleBehaviour receiveMessageBehaviour = new SimpleBehaviour() {

            @Override
            public void action() {
                try {
                    ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                    testMessage.addReceiver(settingsAgentAID);
                    testMessage.setOntology(SettingsOntology.ONTOLOGY_NAME);
                    testMessage.setLanguage(FipaLanguage.LANGUAGE_NAME);
                    testMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                    myAgent.getContentManager().registerLanguage(FipaLanguage.getInstance());
                    myAgent.getContentManager().registerOntology(SettingsOntology.getInstance());

                    String keyForTest = "no-key";
                    GetSetting getSetting = new GetSetting(keyForTest);
                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), getSetting));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().agentMessage(myAgent, msg);

                    UnexpectedContent expectedContent = new UnexpectedContent("Setting not found", keyForTest);

                    UnexpectedContent content = (UnexpectedContent) myAgent.getContentManager().extractContent(msg);

                    assertEquals("Performative", ACLMessage.NOT_UNDERSTOOD, msg.getPerformative());
                    assertReflectionEquals("Content", expectedContent, content);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            @Override
            public boolean done() {
                return true;
            }
        };

        return receiveMessageBehaviour;
    }

}
