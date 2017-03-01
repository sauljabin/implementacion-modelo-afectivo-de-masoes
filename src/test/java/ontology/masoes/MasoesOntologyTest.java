/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
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
        List<Object> predicatesList = new ArrayList();
        predicatesList.addAll(masoesOntology.getPredicateNames());
        assertThat(predicatesList, containsInAnyOrder("AgentState", "ListObjects"));

        List<Object> actionList = new ArrayList();
        actionList.addAll(masoesOntology.getActionNames());
        assertThat(actionList, containsInAnyOrder("CreateObject", "DeleteObject", "EvaluateStimulus", "GetEmotionalState", "GetObject", "NotifyAction", "UpdateObject"));

        List<Object> conceptList = new ArrayList();
        conceptList.addAll(masoesOntology.getConceptNames());
        conceptList.removeAll(actionList);
        assertThat(conceptList, containsInAnyOrder("ActionStimulus", "BehaviourState", "EmotionState", "ObjectProperty", "ObjectStimulus", "Stimulus"));
    }

}