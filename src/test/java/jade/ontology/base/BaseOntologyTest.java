/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BaseOntologyTest {

    private BaseOntology baseOntology;

    @Before
    public void setUp() throws Exception {
        baseOntology = BaseOntology.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(baseOntology, "INSTANCE", null);
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(baseOntology.getName(), is("base"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(baseOntology.getPredicateNames().toArray()), hasItems("FailedAction", "Done", "UnexpectedContent"));
    }

}