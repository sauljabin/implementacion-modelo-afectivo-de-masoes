/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.core.emotion.*;
import masoes.util.math.GeometryCreator;
import masoes.util.math.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionalConfiguratorTest {

    public static final int POSITIVE_SIGN = 1;
    public static final int NEGATIVE_SIGN = -1;
    private List<Emotion> emotions;
    private EmotionalConfigurator configurator;
    private RandomGenerator random;
    private GeometryCreator geometryCreator;

    @Before
    public void setUp() throws Exception {
        geometryCreator = new GeometryCreator();
        emotions = new ArrayList<>();
        emotions.add(new Happiness());
        emotions.add(new Joy());
        emotions.add(new Compassion());
        emotions.add(new Admiration());
        emotions.add(new Depression());
        emotions.add(new Sadness());
        emotions.add(new Dissatisfaction());
        emotions.add(new Rejection());
        configurator = createEmotionalConfigurator();
        random = new RandomGenerator();
    }

    @Test
    public void shouldReturnCorrectList() {
        assertReflectionEquals(configurator.getEmotions(), emotions);
    }

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        configurator.updateEmotionalState(null);
        assertThat(configurator.getEmotionalPoint().getX(), not(0.0));
        assertThat(configurator.getEmotionalPoint().getY(), not(0.0));
        assertThat(configurator.getEmotionalPoint().getX(), is(configurator.getEmotionalState().getActivation()));
        assertThat(configurator.getEmotionalPoint().getY(), is(configurator.getEmotionalState().getSatisfaction()));
    }

    @Test
    public void shouldInvokeEvaluateStimulus() {
        EmotionalConfigurator mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        doCallRealMethod().when(mockEmotionalConfigurator).updateEmotionalState(any());
        when(mockEmotionalConfigurator.evaluateStimulus(any())).thenReturn(new EmotionalState(0, 0));
        mockEmotionalConfigurator.updateEmotionalState(null);
        verify(mockEmotionalConfigurator).evaluateStimulus(any());
    }

    @Test
    public void shouldReturnJoyEmotion() {
        testEmotion(Joy.class, getRandomPointForBasicEmotion(0, 0.5, 0, 0.5));
    }

    @Test
    public void shouldReturnDefaultHappinessEmotion() {
        testEmotion(Happiness.class, getRandomPointForBasicEmotion(2, 10, 2, 10));
    }


    @Test
    public void shouldReturnAdmirationEmotion() {
        testEmotion(Admiration.class, getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        testEmotion(Sadness.class, getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        testEmotion(Rejection.class, getRandomPointForBasicEmotion(0, 0.5, -0.5, 0));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        testEmotion(Happiness.class, getRandomPointForExternalEmotion(POSITIVE_SIGN, POSITIVE_SIGN));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        testEmotion(Compassion.class, getRandomPointForExternalEmotion(NEGATIVE_SIGN, POSITIVE_SIGN));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        testEmotion(Depression.class, getRandomPointForExternalEmotion(NEGATIVE_SIGN, NEGATIVE_SIGN));
    }

    @Test
    public void shouldReturnDissatisfactionEmotion() {
        testEmotion(Dissatisfaction.class, getRandomPointForExternalEmotion(POSITIVE_SIGN, NEGATIVE_SIGN));
    }

    private void testEmotion(Class<?> emotionClass, Point randomPoint) {
        EmotionalConfigurator mockEmotionalConfigurator = mock(EmotionalConfigurator.class);

        EmotionalState mockEmotionalState = mock(EmotionalState.class);
        when(mockEmotionalState.toPoint()).thenReturn(randomPoint);

        when(mockEmotionalConfigurator.evaluateStimulus(any())).thenReturn(mockEmotionalState);

        when(mockEmotionalConfigurator.getEmotionalPoint()).thenReturn(randomPoint);
        when(mockEmotionalConfigurator.getEmotion()).thenCallRealMethod();
        when(mockEmotionalConfigurator.getEmotions()).thenReturn(emotions);

        doCallRealMethod().when(mockEmotionalConfigurator).updateEmotionalState(any());
        mockEmotionalConfigurator.updateEmotionalState(null);

        assertThat(mockEmotionalConfigurator.getEmotion(), is(instanceOf(emotionClass)));
    }

    private Point getRandomPointForExternalEmotion(double xSign, double ySign) {
        double x = xSign * random.getDouble(0, 1);
        double y = ySign * random.getDouble(0, 1);
        if (xSign * x < 0.5)
            y = ySign * random.getDouble(0.5, 1);
        return geometryCreator.createPoint(x, y);
    }

    private Point getRandomPointForBasicEmotion(double xMin, double xMax, double yMin, double yMax) {
        double x = random.getDouble(xMin, xMax);
        double y = random.getDouble(yMin, yMax);
        return geometryCreator.createPoint(x, y);
    }

    private EmotionalConfigurator createEmotionalConfigurator() {
        return new EmotionalConfigurator() {

            @Override
            public EmotionalState evaluateStimulus(Stimulus stimulus) {
                return new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
            }

        };
    }

}