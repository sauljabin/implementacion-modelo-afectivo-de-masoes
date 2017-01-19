/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.jade.settings;

import application.settings.ApplicationSettings;
import functional.test.core.FunctionalTest;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.settings.JadeSettings;
import jade.settings.agent.SettingsAgent;
import jade.settings.ontology.GetAllSettings;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import test.common.TestException;

public class ShouldReceiveAllSettingsTest extends FunctionalTest {

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID settingsAgentAID = createAgent(tester, SettingsAgent.class.getName());

        SimpleBehaviour receiveMessageBehaviour = new OneShotBehaviour() {

            @Override
            public void action() {
                try {
                    ACLMessage testMessage = new ACLMessage(ACLMessage.REQUEST);
                    testMessage.addReceiver(settingsAgentAID);
                    testMessage.setOntology(SettingsOntology.ONTOLOGY_NAME);
                    testMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
                    testMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                    myAgent.getContentManager().registerLanguage(new SLCodec());
                    myAgent.getContentManager().registerOntology(new SettingsOntology());

                    myAgent.getContentManager().fillContent(testMessage, new Action(myAgent.getAID(), new GetAllSettings()));

                    myAgent.send(testMessage);

                    ACLMessage msg = myAgent.blockingReceive();
                    getLogger().agentMessage(myAgent, msg);
                    SystemSettings expectedSystemSettings = new SystemSettings(new ArrayList());

                    ApplicationSettings.getInstance().toMap().forEach(
                            (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
                    );

                    JadeSettings.getInstance().toMap().forEach(
                            (key, value) -> expectedSystemSettings.getSettings().add(new Setting(key, value))
                    );

                    SystemSettings systemSettings = (SystemSettings) myAgent.getContentManager().extractContent(msg);

                    assertEquals("Performative", ACLMessage.INFORM, msg.getPerformative());
                    assertReflectionEquals("Content", expectedSystemSettings.getSettings().toArray(), systemSettings.getSettings().toArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

        };

        return receiveMessageBehaviour;
    }

}
