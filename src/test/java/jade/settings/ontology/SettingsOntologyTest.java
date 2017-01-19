/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SettingsOntologyTest {

    private SettingsOntology settingsOntology;

    @Before
    public void setUp() {
        settingsOntology = new SettingsOntology();
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(settingsOntology.getName(), is("settings"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(settingsOntology.getPredicateNames().toArray()), hasItems("ActionResult", "UnexpectedContent", "SystemSettings"));
        assertThat(Arrays.asList(settingsOntology.getConceptNames().toArray()), hasItems("Setting"));
        assertThat(Arrays.asList(settingsOntology.getActionNames().toArray()), hasItems("GetAllSettings", "GetSetting"));
    }

}