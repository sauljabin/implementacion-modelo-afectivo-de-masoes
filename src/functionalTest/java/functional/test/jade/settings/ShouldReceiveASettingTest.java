/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.jade.settings;

import application.settings.ApplicationSettings;
import functional.test.core.FunctionalTest;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.language.FipaLanguage;
import jade.settings.agent.SettingsAgent;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import test.common.TestException;

public class ShouldReceiveASettingTest extends FunctionalTest {

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

                    GetSetting getSetting = new GetSetting(ApplicationSettings.APP_NAME);
                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), getSetting));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().agentMessage(myAgent, msg);
                    SystemSettings expectedSystemSettings = new SystemSettings(new ArrayList());
                    expectedSystemSettings.getSettings().add(new Setting(ApplicationSettings.APP_NAME, ApplicationSettings.getInstance().get(ApplicationSettings.APP_NAME)));

                    SystemSettings systemSettings = (SystemSettings) myAgent.getContentManager().extractContent(msg);

                    assertEquals("Performative", ACLMessage.INFORM, msg.getPerformative());
                    assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
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
