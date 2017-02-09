/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MasoesOntologyTest {

    private MasoesOntology masoesOntology;

    @Before
    public void setUp() {
        masoesOntology = MasoesOntology.getInstance();
    }

    @Test
    public void shouldReturnCorrectName() {
        assertThat(masoesOntology.getName(), is("masoes"));
    }

    @Test
    public void shouldInitializeElements() {
        assertThat(Arrays.asList(masoesOntology.getPredicateNames().toArray()), hasItems("AgentState"));
        assertThat(Arrays.asList(masoesOntology.getActionNames().toArray()), hasItems("EvaluateStimulus", "GetEmotionalState"));
        assertThat(Arrays.asList(masoesOntology.getConceptNames().toArray()), hasItems("EmotionState", "Stimulus"));
    }

}