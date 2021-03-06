/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent.configurable.ontology;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurableOntologyTest {

    private ConfigurableOntology configurableOntology;

    @Before
    public void setUp() {
        configurableOntology = ConfigurableOntology.getInstance();
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(configurableOntology.getName(), is("configurable"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(configurableOntology.getActionNames().toArray()), hasItems("AddBehaviour", "RemoveBehaviour"));
    }

}