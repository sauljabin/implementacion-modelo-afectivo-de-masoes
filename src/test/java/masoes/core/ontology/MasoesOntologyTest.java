/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.ontology;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class MasoesOntologyTest {

    private MasoesOntology masoesOntology;

    @Before
    public void setUp() {
        masoesOntology = MasoesOntology.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(masoesOntology, "INSTANCE", null);
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(masoesOntology.getName(), is("masoes"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(masoesOntology.getPredicateNames().toArray()), hasItems("Done", "UnexpectedContent"));
    }

}