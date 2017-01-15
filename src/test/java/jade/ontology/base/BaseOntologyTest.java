/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BaseOntologyTest {

    private BaseOntology baseOntology;

    @Before
    public void setUp() {
        baseOntology = new BaseOntology();
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(baseOntology.getName(), is("base"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(baseOntology.getPredicateNames().toArray()), hasItems("Done", "UnexpectedContent"));
    }

}