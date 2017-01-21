/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.ontology.base.ActionResult;
import masoes.model.EmotionalAgent;
import masoes.ontology.EvaluateStimulus;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class StimulusReceiverBehaviourTest {

    private EmotionalAgent emotionalAgentMock;
    private StimulusReceiverBehaviour stimulusReceiverBehaviour;

    @Before
    public void setUp() {
        emotionalAgentMock = mock(EmotionalAgent.class);
        stimulusReceiverBehaviour = new StimulusReceiverBehaviour(emotionalAgentMock);
    }

    @Test
    public void shouldEvaluateStimulus() throws Exception {
        Stimulus stimulus = new Stimulus();

        EvaluateStimulus evaluateStimulus = new EvaluateStimulus(stimulus);
        Action action = new Action(new AID(), evaluateStimulus);

        ActionResult actionResult = (ActionResult) stimulusReceiverBehaviour.performAction(action);
        ActionResult expectedActionResult = new ActionResult("Ok", evaluateStimulus);

        verify(emotionalAgentMock).evaluateStimulus(any());
        assertReflectionEquals(expectedActionResult, actionResult);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action action = new Action(new AID(), new EvaluateStimulus());
        assertTrue(stimulusReceiverBehaviour.isValidAction(action));
    }

}