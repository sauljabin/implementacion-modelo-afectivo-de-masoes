/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import agent.configurable.ConfigurableAgent;
import jade.content.ContentElement;
import jade.core.AID;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;
import test.FunctionalTest;
import test.RandomTestUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OntologyAssistantFunctionalTest extends FunctionalTest {

    private AID agent;

    @Before
    public void setUp() {
        agent = createAgent(ConfigurableAgent.class, null);
    }

    @After
    public void tearDown() {
        killAgent(agent);
    }

    @Test
    public void shouldReturnCustomSetting() {
        addBehaviour(agent, OntologyAssistantResponderBehaviour.class);
        String randomString = RandomTestUtils.randomString();

        OntologyAssistant ontologyAssistant = createOntologyAssistant(SettingsOntology.getInstance());

        ContentElement contentElement = ontologyAssistant.sendRequestAction(agent, new GetSetting(randomString));
        assertThat(contentElement, is(IsInstanceOf.instanceOf(SystemSettings.class)));

        SystemSettings systemSettings = (SystemSettings) contentElement;
        assertThat(systemSettings.getSettings().size(), is(1));

        Setting setting = (Setting) systemSettings.getSettings().get(0);
        assertThat(setting.getKey(), is(randomString));
    }

}