/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class SettingsOntologyTest {

    private SettingsOntology settingsOntology;

    @Before
    public void setUp() {
        settingsOntology = SettingsOntology.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(settingsOntology, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(SettingsOntology.getInstance(), is(settingsOntology));
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(settingsOntology.getName(), is("settings"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(settingsOntology.getPredicateNames().toArray()), hasItems("FailedAction", "Done", "UnexpectedContent", "SystemSettings"));
        assertThat(Arrays.asList(settingsOntology.getConceptNames().toArray()), hasItems("Setting"));
        assertThat(Arrays.asList(settingsOntology.getActionNames().toArray()), hasItems("GetAllSettings", "GetSetting"));
    }

}