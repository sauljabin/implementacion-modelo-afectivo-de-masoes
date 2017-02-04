package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class SimpleBehaviourTest {

    private SimpleBehaviour simpleBehaviour;
    private Agent agentMock;
    private SimpleBehaviour simpleBehaviourSpy;

    @Before
    public void setUp() throws NoSuchFieldException {
        agentMock = mock(Agent.class);
        simpleBehaviour = new SimpleBehaviour();
        simpleBehaviour.setAgent(agentMock);
        simpleBehaviourSpy = spy(simpleBehaviour);
    }

    @Test
    public void shouldBlockWaitingMessage() {
        simpleBehaviourSpy.action();
        verify(simpleBehaviourSpy).block();
    }

    @Test
    public void shouldSendResponseWhenReceiveAnyMessage() throws Exception {
        ACLMessage messageMock = mock(ACLMessage.class);
        ACLMessage requestMock = mock(ACLMessage.class);
        doReturn(messageMock).when(agentMock).receive();
        doReturn(requestMock).when(messageMock).createReply();
        simpleBehaviour.action();
        verify(requestMock).setPerformative(ACLMessage.CONFIRM);
        verify(agentMock).send(requestMock);
    }

}