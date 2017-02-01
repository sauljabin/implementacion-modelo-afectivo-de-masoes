/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.Stimulus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StimulusReceiverBehaviourTest {

    private EmotionalAgent emotionalAgentMock;
    private StimulusReceiverBehaviour stimulusReceiverBehaviour;

    @Before
    public void setUp() {
        emotionalAgentMock = mock(EmotionalAgent.class);
        stimulusReceiverBehaviour = new StimulusReceiverBehaviour(emotionalAgentMock);
    }

    @Test
    public void shouldEvaluateStimulus() {
        Stimulus stimulus = new Stimulus();

        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(new AID(), evaluateStimulus);

        Predicate predicate = stimulusReceiverBehaviour.performAction(action);
        verify(emotionalAgentMock).evaluateStimulus(any());
        assertThat(predicate, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action action = new Action(new AID(), new EvaluateStimulus());
        assertTrue(stimulusReceiverBehaviour.isValidAction(action));
    }

}