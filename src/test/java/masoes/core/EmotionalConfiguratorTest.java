/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.util.math.RandomGenerator;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class EmotionalConfiguratorTest {

    private EmotionalConfigurator mockEmotionalConfigurator;
    private EmotionalSpace emotionalSpace;

    @Before
    public void setUp() {
        emotionalSpace = new EmotionalSpace();
        mockEmotionalConfigurator = spy(createDummyEmotionalConfigurator());
    }

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        EmotionalState mockEmotionalState = mock(EmotionalState.class);
        when(mockEmotionalConfigurator.calculateEmotionalState(any())).thenReturn(mockEmotionalState);
        mockEmotionalConfigurator.updateEmotionalState(null);
        EmotionalState emotionalState = mockEmotionalConfigurator.getEmotionalState();
        assertThat(emotionalState, is(mockEmotionalState));
    }

    @Test
    public void shouldReturnCorrectEmotion() {
        mockEmotionalConfigurator.updateEmotionalState(null);
        Emotion expectedEmotion = emotionalSpace.searchEmotion(mockEmotionalConfigurator.getEmotionalState());
        assertThat(mockEmotionalConfigurator.getEmotion(), is(IsInstanceOf.instanceOf(expectedEmotion.getClass())));
    }

    private EmotionalConfigurator createDummyEmotionalConfigurator() {
        return new EmotionalConfigurator() {
            @Override
            protected EmotionalState calculateEmotionalState(Stimulus stimulus) {
                RandomGenerator random = new RandomGenerator();
                return new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
            }
        };
    }

}