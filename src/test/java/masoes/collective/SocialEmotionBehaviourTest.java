/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.EmotionState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.RandomUtil;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class SocialEmotionBehaviourTest {

    private Agent agentMock;
    private SocialEmotionBehaviour socialEmotionBehaviour;
    private OntologyAssistant ontologyAssistantMock;
    private AgentManagementAssistant agentManagementAssistantMock;
    private ArgumentCaptor<ServiceDescription> serviceDescriptionArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        serviceDescriptionArgumentCaptor = ArgumentCaptor.forClass(ServiceDescription.class);

        agentMock = mock(Agent.class);
        agentManagementAssistantMock = mock(AgentManagementAssistant.class);
        ontologyAssistantMock = mock(OntologyAssistant.class);

        socialEmotionBehaviour = new SocialEmotionBehaviour(agentMock);
        setFieldValue(socialEmotionBehaviour, "masoesOntologyAssistant", ontologyAssistantMock);
        setFieldValue(socialEmotionBehaviour, "agentManagementAssistant", agentManagementAssistantMock);
    }

    @Test
    public void shouldReturnValidNotifyAction() {
        Action action = new Action(new AID(), new GetSocialEmotion());
        assertTrue(socialEmotionBehaviour.isValidAction(action));
    }

    @Test
    public void shouldReceiveEmotionalState() throws FailureException {
        SocialEmotionCalculator socialEmotionCalculator = new SocialEmotionCalculator();

        AID firstAID = new AID(RandomUtil.randomString(), AID.ISGUID);
        AgentState firstAgentState = new AgentState();
        firstAgentState.setEmotionState(new EmotionState(null, null, null, new EmotionalState()));
        socialEmotionCalculator.addEmotionalState(firstAgentState.getEmotionState().toEmotionalState());

        AID secondAID = new AID(RandomUtil.randomString(), AID.ISGUID);
        AgentState secondAgentState = new AgentState();
        secondAgentState.setEmotionState(new EmotionState(null, null, null, new EmotionalState()));
        socialEmotionCalculator.addEmotionalState(secondAgentState.getEmotionState().toEmotionalState());

        ArrayList<AID> aids = new ArrayList<>();
        aids.add(firstAID);
        aids.add(secondAID);

        doReturn(aids).when(agentManagementAssistantMock).search(any(ServiceDescription.class));
        doReturn(firstAgentState).when(ontologyAssistantMock).sendRequestAction(eq(firstAID), any(GetEmotionalState.class));
        doReturn(secondAgentState).when(ontologyAssistantMock).sendRequestAction(eq(secondAID), any(GetEmotionalState.class));

        Predicate actualPredicate = socialEmotionBehaviour.performAction(new Action(new AID("any", AID.ISGUID), new GetSocialEmotion()));

        verify(agentManagementAssistantMock).search(serviceDescriptionArgumentCaptor.capture());
        verify(ontologyAssistantMock).sendRequestAction(eq(firstAID), any(GetEmotionalState.class));
        verify(ontologyAssistantMock).sendRequestAction(eq(secondAID), any(GetEmotionalState.class));

        assertThat(serviceDescriptionArgumentCaptor.getValue().getName(), is(MasoesOntology.ACTION_GET_EMOTIONAL_STATE));

        assertThat(actualPredicate, is(instanceOf(SocialEmotion.class)));

        SocialEmotion socialEmotion = (SocialEmotion) actualPredicate;
        assertThat(socialEmotion.getCentralEmotion().getActivation(), is(socialEmotionCalculator.getCentralEmotionalState().getActivation()));
        assertThat(socialEmotion.getCentralEmotion().getSatisfaction(), is(socialEmotionCalculator.getCentralEmotionalState().getSatisfaction()));

        assertThat(socialEmotion.getEmotionalDispersion().getActivation(), is(socialEmotionCalculator.getEmotionalDispersion().getActivation()));
        assertThat(socialEmotion.getEmotionalDispersion().getSatisfaction(), is(socialEmotionCalculator.getEmotionalDispersion().getSatisfaction()));

        assertThat(socialEmotion.getMaximumDistance().getActivation(), is(socialEmotionCalculator.getMaximumDistance().getActivation()));
        assertThat(socialEmotion.getMaximumDistance().getSatisfaction(), is(socialEmotionCalculator.getMaximumDistance().getSatisfaction()));
    }

}