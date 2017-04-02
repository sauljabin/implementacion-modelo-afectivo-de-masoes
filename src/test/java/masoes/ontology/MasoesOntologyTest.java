/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
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
    public void shouldGetAllPredicates() {
        assertThat(masoesOntology.getPredicateNames().toArray(), arrayContainingInAnyOrder("AgentState", "ListObjects", "SocialEmotion"));

    }

    @Test
    public void shouldGetAllActions() {
        assertThat(masoesOntology.getActionNames().toArray(), arrayContainingInAnyOrder("CreateObject", "DeleteObject", "EvaluateStimulus", "GetEmotionalState", "GetObject", "NotifyAction", "NotifyObject", "NotifyEvent", "UpdateObject", "GetSocialEmotion"));
    }

    @Test
    public void shouldGetAllConcepts() {
        List<Object> concepts = new ArrayList<>(Arrays.asList(masoesOntology.getConceptNames().toArray()));
        List<Object> actions = Arrays.asList(masoesOntology.getActionNames().toArray());
        concepts.removeAll(actions);
        assertThat(concepts, containsInAnyOrder("ActionStimulus", "BehaviourState", "EmotionState", "ObjectProperty", "ObjectStimulus", "EventStimulus", "Stimulus", "ObjectEnvironment", "CentralEmotion", "EmotionalDispersion", "MaximumDistances"));
    }

}