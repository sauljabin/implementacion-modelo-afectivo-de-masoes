/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import masoes.behavioural.BehaviourType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.PowerMockitoTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

public class EmotionalBehaviourTest extends PowerMockitoTest {

    private EmotionalBehaviour emotionalBehaviour;
    private Agent agentMock;
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private AID notifierAid;
    private AID agentAid;

    @Before
    public void setUp() {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
        emotionalBehaviour = createEmotionalBehaviour();
        agentMock = mock(Agent.class);
        emotionalBehaviour.setAgent(agentMock);
        notifierAid = new AID("notifier", AID.ISGUID);
        agentAid = new AID("agent", AID.ISGUID);
        doReturn(notifierAid).when(agentMock).getAID("notifier");
        doReturn(agentAid).when(agentMock).getAID();
    }

    @Test
    public void shouldNotifyAction() {
        String expectedActionName = "expectedActionName";
        emotionalBehaviour.notifyAction(expectedActionName);
        verify(agentMock).send(messageArgumentCaptor.capture());
        ACLMessage message = messageArgumentCaptor.getValue();
        assertThat(message.getContent(), containsString("NotifyAction"));
        assertThat(message.getContent(), containsString(expectedActionName));
        assertThat(message.getContent(), containsString("agent-identifier :name notifier"));
    }

    private EmotionalBehaviour createEmotionalBehaviour() {
        return new EmotionalBehaviour() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public BehaviourType getType() {
                return null;
            }

            @Override
            public void action() {

            }

            @Override
            public boolean done() {
                return false;
            }
        };
    }

}