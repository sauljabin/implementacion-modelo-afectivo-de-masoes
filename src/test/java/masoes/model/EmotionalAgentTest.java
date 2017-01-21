/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.core.behaviours.Behaviour;
import logger.jade.JadeLogger;
import masoes.behaviour.ResponseAgentStatusBehaviour;
import masoes.behaviour.StimulusReceiverBehaviour;
import masoes.ontology.MasoesOntology;
import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EmotionalAgentTest {

    private EmotionalState mockEmotionalState;
    private EmotionalAgent spyEmotionalAgent;
    private BehaviourManager mockBehaviourManager;
    private Behaviour mockBehaviour;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private Emotion mockEmotion;
    private Stimulus mockStimulus;
    private JadeLogger mockLogger;
    private ArgumentCaptor<MasoesOntology> ontologyArgumentCaptor;
    private ArgumentCaptor<Codec> codecArgumentCaptor;
    private ArgumentCaptor<Behaviour> behaviourArgumentCaptor;
    private ContentManager mockContentManager;

    @Before
    public void setUp() throws Exception {
        ontologyArgumentCaptor = ArgumentCaptor.forClass(MasoesOntology.class);
        codecArgumentCaptor = ArgumentCaptor.forClass(Codec.class);
        behaviourArgumentCaptor = ArgumentCaptor.forClass(Behaviour.class);

        mockBehaviourManager = mock(BehaviourManager.class);
        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        mockLogger = mock(JadeLogger.class);
        mockContentManager = mock(ContentManager.class);
        mockEmotionalState = mock(EmotionalState.class);
        mockBehaviour = mock(Behaviour.class);
        mockEmotion = mock(Emotion.class);
        mockStimulus = mock(Stimulus.class);

        spyEmotionalAgent = createAgent();

        doReturn(mockEmotion).when(mockEmotionalConfigurator).getEmotion();
        doReturn(mockEmotionalState).when(mockEmotionalConfigurator).getEmotionalState();
        doReturn(mockBehaviour).when(mockBehaviourManager).calculateBehaviour(any());
        doReturn(mockBehaviour).when(mockBehaviourManager).getBehaviour();
        doNothing().when(mockBehaviourManager).updateBehaviour(any());
        doReturn(mockContentManager).when(spyEmotionalAgent).getContentManager();

        spyEmotionalAgent.setup();
    }

    @Test
    public void shouldAddBasicBehaviors() {
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(behaviourArgumentCaptor.capture());
        verify(spyEmotionalAgent, atLeastOnce()).addBehaviour(behaviourArgumentCaptor.capture());
        assertThat(spyEmotionalAgent.getCurrentEmotionalBehaviour(), is(mockBehaviour));
        assertThat(spyEmotionalAgent.getCurrentEmotion(), is(mockEmotion));
        assertThat(behaviourArgumentCaptor.getAllValues(), hasItems(is(instanceOf(ResponseAgentStatusBehaviour.class)), is(instanceOf(StimulusReceiverBehaviour.class))));

        InOrder inOrder = inOrder(spyEmotionalAgent, mockBehaviourManager);
        inOrder.verify(spyEmotionalAgent).setUp();
        inOrder.verify(mockBehaviourManager).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldAddOntologyAndLanguage() throws Exception {
        verify(mockContentManager).registerLanguage(codecArgumentCaptor.capture());
        verify(mockContentManager).registerOntology(ontologyArgumentCaptor.capture());
        assertThat(codecArgumentCaptor.getValue(), is(instanceOf(SLCodec.class)));
        assertThat(ontologyArgumentCaptor.getValue(), is(instanceOf(MasoesOntology.class)));
    }

    @Test
    public void shouldUpdateBehaviour() throws Exception {
        spyEmotionalAgent = createAgent();
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockBehaviourManager).updateBehaviour(spyEmotionalAgent);
    }

    @Test
    public void shouldUpdateEmotion() {
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockEmotionalConfigurator).updateEmotion(mockStimulus);
    }

    @Test
    public void shouldLogEmotionChange() {
        spyEmotionalAgent.evaluateStimulus(mockStimulus);
        verify(mockLogger).agentEmotionalState(spyEmotionalAgent);
        verify(mockLogger).agentEmotionalStateChanged(spyEmotionalAgent, mockStimulus);
    }

    private EmotionalAgent createAgent() throws Exception {
        EmotionalAgent emotionalAgent = new EmotionalAgent();
        setFieldValue(emotionalAgent, "behaviourManager", mockBehaviourManager);
        setFieldValue(emotionalAgent, "emotionalConfigurator", mockEmotionalConfigurator);
        setFieldValue(emotionalAgent, "logger", mockLogger);
        return spy(emotionalAgent);
    }

}