/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.behaviour;

import application.settings.ApplicationSettings;
import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.language.FipaLanguage;
import jade.settings.JadeSettings;
import jade.settings.ontology.GetAllSettings;
import jade.settings.ontology.GetSetting;
import jade.settings.ontology.Setting;
import jade.settings.ontology.SettingsOntology;
import jade.settings.ontology.SystemSettings;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ResponseSettingsBehaviourTest {

    private Agent mockAgent;
    private ResponseSettingsBehaviour spySettingsBehaviour;
    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
    private ContentManager contentManager;

    @Before
    public void setUp() {
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);
        mockAgent = mock(Agent.class);
        spySettingsBehaviour = spy(new ResponseSettingsBehaviour());
        spySettingsBehaviour.setAgent(mockAgent);

        contentManager = new ContentManager();
        contentManager.registerLanguage(FipaLanguage.getInstance());
        contentManager.registerOntology(SettingsOntology.getInstance());
        when(mockAgent.getContentManager()).thenReturn(contentManager);
    }

    @Test
    public void shouldSetCorrectMessageTemplate() {
        MessageTemplate expectedTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchLanguage(FipaLanguage.LANGUAGE_NAME));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchOntology(SettingsOntology.ONTOLOGY_NAME));

        spySettingsBehaviour.onStart();
        verify(spySettingsBehaviour).setMessageTemplate(messageTemplateArgumentCaptor.capture());
        assertReflectionEquals(expectedTemplate, messageTemplateArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnASetting() throws Exception {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FipaLanguage.LANGUAGE_NAME);
        request.setOntology(SettingsOntology.ONTOLOGY_NAME);

        GetSetting content = new GetSetting(ApplicationSettings.OS_VERSION);
        Action action = new Action(new AID(), content);

        contentManager.fillContent(request, action);

        ACLMessage response = spySettingsBehaviour.prepareResponse(request);

        SystemSettings systemSettings = (SystemSettings) contentManager.extractContent(response);

        Setting expectedSetting = new Setting(ApplicationSettings.OS_VERSION, ApplicationSettings.getInstance().get(ApplicationSettings.OS_VERSION));
        assertReflectionEquals(expectedSetting, systemSettings.getSettings().get(0));
    }

    @Test
    public void shouldReturnAllSettings() throws Exception {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(FipaLanguage.LANGUAGE_NAME);
        request.setOntology(SettingsOntology.ONTOLOGY_NAME);

        Action action = new Action(new AID(), new GetAllSettings());

        contentManager.fillContent(request, action);

        ACLMessage response = spySettingsBehaviour.prepareResponse(request);

        SystemSettings systemSettings = (SystemSettings) contentManager.extractContent(response);

        SystemSettings expectedSetting = new SystemSettings();
        List appList = new ArrayList();

        Map<String, String> appSettings = ApplicationSettings.getInstance().toMap();
        appSettings.forEach((key, value) -> appList.add(new Setting(key, value)));

        Map<String, String> jadeSettings = JadeSettings.getInstance().toMap();
        jadeSettings.forEach((key, value) -> appList.add(new Setting(key, value)));

        expectedSetting.setSettings(appList);

        assertReflectionEquals(expectedSetting.getSettings().toArray(), systemSettings.getSettings().toArray());
    }

}