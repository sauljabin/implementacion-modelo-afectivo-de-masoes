/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import masoes.ontology.MasoesOntology;
import masoes.ontology.stimulus.EvaluateStimulus;
import org.junit.Before;
import org.junit.Test;
import settings.ontology.GetAllSettings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ActionResponderBehaviourTest {

    private ActionResponderBehaviour actionResponderBehaviour;
    private Agent agentMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        actionResponderBehaviour = new ActionResponderBehaviour(agentMock, MasoesOntology.getInstance(), EvaluateStimulus.class);
    }

    @Test
    public void shouldReturnCorrectOntologyTemplateMatch() {
        MessageTemplate expected = new MessageTemplate(new ActionMatchExpression(MasoesOntology.getInstance(), EvaluateStimulus.class));
        assertReflectionEquals(expected, actionResponderBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldReturnActionValid() {
        assertTrue(actionResponderBehaviour.isValidAction(new Action(new AID("NAME", AID.ISGUID), new EvaluateStimulus())));
    }

    @Test
    public void shouldReturnActionInvalid() {
        assertFalse(actionResponderBehaviour.isValidAction(new Action(new AID("NAME", AID.ISGUID), new GetAllSettings())));
    }

}