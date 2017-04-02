/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import com.vividsolutions.jts.geom.Point;
import org.junit.Test;
import util.RandomGenerator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static test.ReflectionTestUtils.setFieldValue;

public class EmotionalStateTest {

    @Test
    public void shouldCreatePoint() {
        testEmotionalState(new EmotionalState(RandomGenerator.getDouble(0, 1), RandomGenerator.getDouble(0, 1)));
    }

    @Test
    public void shouldConstructEmotionalStateWithRandomValues() {
        testEmotionalState(new EmotionalState());
    }

    private void testEmotionalState(EmotionalState emotionalState) {
        Point actualPoint = emotionalState.toPoint();
        assertThat(actualPoint.getX(), is(emotionalState.getActivation()));
        assertThat(actualPoint.getY(), is(emotionalState.getSatisfaction()));
        assertThat(actualPoint.getX(), is(not(0.0)));
        assertThat(actualPoint.getY(), is(not(0.0)));
    }

    @Test
    public void shouldSetMaxWhenActivationOrSatisfactionIsGreaterThan1() {
        EmotionalState emotionalState = new EmotionalState(2, 2);
        assertThat(emotionalState.getActivation(), is(1.0));
        assertThat(emotionalState.getSatisfaction(), is(1.0));
        testEmotionalState(emotionalState);
    }

    @Test
    public void shouldSetMaxWhenActivationOrSatisfactionIsLessThanMinus1() {
        EmotionalState emotionalState = new EmotionalState(-2, -2);
        assertThat(emotionalState.getActivation(), is(-1.0));
        assertThat(emotionalState.getSatisfaction(), is(-1.0));
        testEmotionalState(emotionalState);
    }

    @Test
    public void shouldGetEmotionForEmotionalState() throws Exception {
        EmotionalState emotionalState = new EmotionalState();
        EmotionalSpace emotionalSpaceMock = mock(EmotionalSpace.class);
        setFieldValue(emotionalState, "emotionalSpace", emotionalSpaceMock);

        emotionalState.toEmotion();
        verify(emotionalSpaceMock).searchEmotion(emotionalState);
    }

}