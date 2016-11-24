/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.Emotion;
import masoes.core.EmotionType;
import masoes.env.dummy.behaviour.DummyCognitiveBehaviour;
import masoes.env.dummy.behaviour.DummyImitativeBehaviour;
import masoes.env.dummy.behaviour.DummyReactiveBehaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DummyBehaviourManagerTest {

    private DummyBehaviourManager dummyBehaviourManager;
    private Emotion mockEmotion;

    @Before
    public void setUp() {
        dummyBehaviourManager = new DummyBehaviourManager();
        mockEmotion = mock(Emotion.class);
    }

    @Test
    public void shouldReturnCognitiveBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.NEGATIVE_LOW);
        assertThat(dummyBehaviourManager.calculateBehaviour(mockEmotion), is(instanceOf(DummyCognitiveBehaviour.class)));
    }

    @Test
    public void shouldReturnImitativeBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.POSITIVE);
        assertThat(dummyBehaviourManager.calculateBehaviour(mockEmotion), is(instanceOf(DummyImitativeBehaviour.class)));
    }

    @Test
    public void shouldReturnReactiveBehaviour() {
        when(mockEmotion.getEmotionType()).thenReturn(EmotionType.NEGATIVE_HIGH);
        assertThat(dummyBehaviourManager.calculateBehaviour(mockEmotion), is(instanceOf(DummyReactiveBehaviour.class)));
    }

}