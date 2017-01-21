/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.ontology.Stimulus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class EmotionalConfiguratorTest {

    private EmotionalConfigurator emotionalConfiguratorSpy;
    private EmotionalSpace emotionalSpace;

    @Before
    public void setUp() {
        emotionalSpace = new EmotionalSpace();
        emotionalConfiguratorSpy = spy(new EmotionalConfigurator());
    }

    @Test
    public void shouldUpdateEmotionalState() {
        EmotionalState emotionalStateMock = mock(EmotionalState.class);
        Stimulus stimulusMock = mock(Stimulus.class);
        doReturn(emotionalStateMock).when(emotionalConfiguratorSpy).calculateEmotionalState(stimulusMock);
        emotionalConfiguratorSpy.updateEmotion(stimulusMock);
        verify(emotionalConfiguratorSpy).calculateEmotionalState(stimulusMock);
        assertThat(emotionalConfiguratorSpy.getEmotionalState(), is(emotionalStateMock));
    }

    @Test
    public void shouldReturnCorrectEmotion() {
        emotionalConfiguratorSpy.updateEmotion(any());
        Emotion expectedEmotion = emotionalSpace.searchEmotion(emotionalConfiguratorSpy.getEmotionalState());
        assertThat(emotionalConfiguratorSpy.getEmotion(), is(instanceOf(expectedEmotion.getClass())));
    }

}