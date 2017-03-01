/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import ontology.OntologyMatchExpression;
import ontology.masoes.CreateObject;
import ontology.masoes.DeleteObject;
import ontology.masoes.GetObject;
import ontology.masoes.MasoesOntology;
import ontology.masoes.UpdateObject;
import org.junit.Before;
import org.junit.Test;
import test.PowerMockitoTest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class DataPersistenceBehaviourTest extends PowerMockitoTest {

    private DataPersistenceBehaviour dataPersistenceBehaviour;
    private Agent agentMock;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        dataPersistenceBehaviour = new DataPersistenceBehaviour(agentMock);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action getObject = new Action(new AID(), new GetObject());
        Action createObject = new Action(new AID(), new CreateObject());
        Action deleteObject = new Action(new AID(), new DeleteObject());
        Action updateObject = new Action(new AID(), new UpdateObject());
        assertTrue(dataPersistenceBehaviour.isValidAction(getObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(createObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(deleteObject));
        assertTrue(dataPersistenceBehaviour.isValidAction(updateObject));
    }

    @Test
    public void shouldGetCorrectOntologyAndMessageTemplate() {
        assertThat(dataPersistenceBehaviour.getOntology(), is(instanceOf(MasoesOntology.class)));
        assertReflectionEquals(new MessageTemplate(new OntologyMatchExpression(MasoesOntology.getInstance())), dataPersistenceBehaviour.getMessageTemplate());
    }

}