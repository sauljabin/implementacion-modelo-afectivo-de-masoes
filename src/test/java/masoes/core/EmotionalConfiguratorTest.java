/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmotionalConfiguratorTest {

    private EmotionalConfigurator spyEmotionalConfigurator;
    private EmotionalSpace emotionalSpace;

    @Before
    public void setUp() {
        emotionalSpace = new EmotionalSpace();
        spyEmotionalConfigurator = spy(new EmotionalConfigurator());
    }

    @Test
    public void shouldUpdateEmotionalState() {
        EmotionalState mockEmotionalState = mock(EmotionalState.class);
        Stimulus mockStimulus = mock(Stimulus.class);
        when(spyEmotionalConfigurator.calculateEmotionalState(mockStimulus)).thenReturn(mockEmotionalState);
        spyEmotionalConfigurator.updateEmotion(mockStimulus);
        verify(spyEmotionalConfigurator).calculateEmotionalState(mockStimulus);
        assertThat(spyEmotionalConfigurator.getEmotionalState(), is(mockEmotionalState));
    }

    @Test
    public void shouldReturnCorrectEmotion() {
        spyEmotionalConfigurator.updateEmotion(any());
        Emotion expectedEmotion = emotionalSpace.searchEmotion(spyEmotionalConfigurator.getEmotionalState());
        assertThat(spyEmotionalConfigurator.getEmotion(), is(instanceOf(expectedEmotion.getClass())));
    }

}