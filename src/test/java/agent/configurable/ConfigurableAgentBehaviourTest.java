/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent.configurable;

import agent.configurable.ontology.AddBehaviour;
import agent.configurable.ontology.ConfigurableOntology;
import agent.configurable.ontology.RemoveBehaviour;
import environment.dummy.DummyBehaviour;
import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import ontology.OntologyMatchExpression;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ConfigurableAgentBehaviourTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ConfigurableAgentBehaviour configurableAgentBehaviour;
    private Agent agentMock;
    private Map<String, Behaviour> behaviourMap;

    @Before
    public void setUp() throws Exception {
        agentMock = mock(Agent.class);
        configurableAgentBehaviour = new ConfigurableAgentBehaviour(agentMock);
        behaviourMap = new HashMap<>();
        setFieldValue(configurableAgentBehaviour, "behaviours", behaviourMap);
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(configurableAgentBehaviour.getOntology(), is(instanceOf(ConfigurableOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(ConfigurableOntology.getInstance())), configurableAgentBehaviour.getMessageTemplate());
    }

    @Test
    public void shouldAddBehavior() throws Exception {
        String behaviourName = "behaviourName";
        Action action = new Action();
        AddBehaviour addBehaviour = new AddBehaviour(behaviourName, DummyBehaviour.class.getCanonicalName());
        action.setAction(addBehaviour);

        Predicate predicate = configurableAgentBehaviour.performAction(action);

        verify(agentMock).addBehaviour(isA(DummyBehaviour.class));
        assertThat(predicate, is(instanceOf(Done.class)));
        assertThat(behaviourMap.get(behaviourName), is(instanceOf(DummyBehaviour.class)));
    }

    @Test
    public void shouldRemoveBehavior() throws Exception {
        String behaviourName = "behaviourName";
        Behaviour behaviourMock = mock(Behaviour.class);
        behaviourMap.put(behaviourName, behaviourMock);

        Action action = new Action();
        RemoveBehaviour removeBehaviour = new RemoveBehaviour(behaviourName);
        action.setAction(removeBehaviour);

        Predicate predicate = configurableAgentBehaviour.performAction(action);

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
        configurableAgentBehaviour.performAction(action);
    }

    @Test
    public void shouldReturnFalseWhenActionIsInvalid() {
        Action action = new Action();
        action.setAction(new AddBehaviour());
        assertTrue(configurableAgentBehaviour.isValidAction(action));
        action.setAction(new RemoveBehaviour());
        assertTrue(configurableAgentBehaviour.isValidAction(action));
    }

    @Test
    public void shouldThrowExceptionWhenActionNameIsNull() throws Exception {
        expectedException.expectMessage("Behaviour name not found");
        expectedException.expect(FailureException.class);
        Action action = new Action();
        action.setAction(new AddBehaviour());
        configurableAgentBehaviour.performAction(action);
    }

    @Test
    public void shouldThrowExceptionWhenActionNameIsEmpty() throws Exception {
        expectedException.expectMessage("Behaviour name not found");
        expectedException.expect(FailureException.class);
        Action action = new Action();
        AddBehaviour addBehaviour = new AddBehaviour();
        addBehaviour.setName("");
        action.setAction(addBehaviour);
        configurableAgentBehaviour.performAction(action);
    }

}