/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ConfiguringAgentBehaviourTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ConfiguringAgentBehaviour configuringAgentBehaviour;
    private Agent agentMock;
    private Map behavioursMock;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        configuringAgentBehaviour = new ConfiguringAgentBehaviour(agentMock);
        behavioursMock = mock(Map.class);
        setFieldValue(configuringAgentBehaviour, "behaviours", behavioursMock);
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(configuringAgentBehaviour.getOntology(), is(instanceOf(ConfigurableOntology.class)));
        assertReflectionEquals(new MessageTemplate(new ConfiguringAgentMatchExpression()), configuringAgentBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldAddBehavior() throws Exception {
        String behaviourName = "behaviourName";
        Action action = new Action();
        AddBehaviour addBehaviour = new AddBehaviour(behaviourName, SimpleBehaviour.class.getCanonicalName());
        action.setAction(addBehaviour);

        Predicate predicate = configuringAgentBehaviour.performAction(action);

        verify(agentMock).addBehaviour(isA(SimpleBehaviour.class));
        verify(behavioursMock).put(eq(behaviourName), isA(SimpleBehaviour.class));
        assertThat(predicate, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldRemoveBehavior() throws Exception {
        String behaviourName = "behaviourName";
        Behaviour behaviourMock = mock(Behaviour.class);
        doReturn(behaviourMock).when(behavioursMock).get(behaviourName);

        Action action = new Action();
        RemoveBehaviour removeBehaviour = new RemoveBehaviour(behaviourName);
        action.setAction(removeBehaviour);

        Predicate predicate = configuringAgentBehaviour.performAction(action);

        verify(agentMock).removeBehaviour(behaviourMock);
        assertThat(predicate, is(instanceOf(Done.class)));
    }

    @Test
    public void shouldThrowsFailureExceptionInInvalidAction() throws Exception {
        String actionToString = "action";
        expectedException.expectMessage("Unknown action " + actionToString);
        expectedException.expect(FailureException.class);
        Action action = new Action();
        AgentAction agentActionMock = mock(AgentAction.class);
        doReturn(actionToString).when(agentActionMock).toString();
        action.setAction(agentActionMock);
        configuringAgentBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnFalseWhenActionIsInvalid() {
        Action action = new Action();
        action.setAction(new AddBehaviour());
        assertTrue(configuringAgentBehaviour.isValidAction(action));
        action.setAction(new RemoveBehaviour());
        assertTrue(configuringAgentBehaviour.isValidAction(action));
    }

    @Test
    public void shouldReturnFalseWhenActionNameIsNull() throws Exception {
        expectedException.expectMessage("Behaviour name not found");
        expectedException.expect(FailureException.class);
        Action action = new Action();
        action.setAction(new AddBehaviour());
        configuringAgentBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnFalseWhenActionNameIsEmpty() throws Exception {
        expectedException.expectMessage("Behaviour name not found");
        expectedException.expect(FailureException.class);
        Action action = new Action();
        AddBehaviour addBehaviour = new AddBehaviour();
        addBehaviour.setName("");
        action.setAction(addBehaviour);
        configuringAgentBehaviour.performAction(action);
    }

}