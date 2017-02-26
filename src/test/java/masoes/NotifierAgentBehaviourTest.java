/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import ontology.masoes.NotifyAction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class NotifierAgentBehaviourTest {

    private Agent agentMock;
    private NotifierAgentBehaviour notifierAgentBehaviour;

    @Before
    public void setUp() {
        agentMock = mock(Agent.class);
        notifierAgentBehaviour = new NotifierAgentBehaviour(agentMock);
    }

    @Test
    public void shouldReturnValidAgentAction() {
        Action action = new Action(new AID(), new NotifyAction());
        assertTrue(notifierAgentBehaviour.isValidAction(action));
    }

}