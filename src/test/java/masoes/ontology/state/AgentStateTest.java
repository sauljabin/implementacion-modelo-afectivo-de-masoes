/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state;

import jade.core.AID;
import masoes.component.behavioural.BehaviourType;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.component.behavioural.EmotionalState;
import masoes.component.behavioural.emotion.HappinessEmotion;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

public class AgentStateTest {

    private AID agentAID;
    private HappinessEmotion emotion;
    private EmotionalState emotionalState;
    private BehaviouralComponent behaviouralComponentMock;

    @Before
    public void setUp() throws Exception {
        behaviouralComponentMock = mock(BehaviouralComponent.class);
        agentAID = new AID("agentName", AID.ISGUID);

        emotion = new HappinessEmotion();
        doReturn(emotion).when(behaviouralComponentMock).getCurrentEmotion();

        emotionalState = emotion.getRandomEmotionalState();
        doReturn(emotionalState).when(behaviouralComponentMock).getCurrentEmotionalState();

        doReturn(BehaviourType.IMITATIVE).when(behaviouralComponentMock).getCurrentBehaviourType();
    }

    @Test
    public void shouldReturnAgentStatus() throws Exception {
        AgentState agentState = new AgentState(agentAID, behaviouralComponentMock);

        assertThat(agentState.getAgent(), is(agentAID));
        assertThat(agentState.getBehaviourState().getType(), is(BehaviourType.IMITATIVE.toString()));
        assertThat(agentState.getEmotionState().getName(), is(emotion.getName()));
        assertThat(agentState.getEmotionState().getType(), is(emotion.getType().toString()));
        assertThat(agentState.getEmotionState().getActivation(), is(emotionalState.getActivation()));
        assertThat(agentState.getEmotionState().getSatisfaction(), is(emotionalState.getSatisfaction()));
        assertThat(agentState.getEmotionState().getClassName(), is(emotion.getClass().getSimpleName()));
    }

}