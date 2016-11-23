/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.core.emotion.Admiration;
import masoes.core.emotion.Compassion;
import masoes.core.emotion.Depression;
import masoes.core.emotion.Dissatisfaction;
import masoes.core.emotion.Happiness;
import masoes.core.emotion.Joy;
import masoes.core.emotion.Rejection;
import masoes.core.emotion.Sadness;
import masoes.util.math.GeometryCreator;
import masoes.util.math.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class EmotionalConfiguratorTest {

    public static final int POSITIVE_SIGN = 1;
    public static final int NEGATIVE_SIGN = -1;
    private List<Emotion> emotions;
    private EmotionalConfigurator mockEmotionalConfigurator;
    private RandomGenerator random;
    private GeometryCreator geometryCreator;

    @Before
    public void setUp() {
        random = new RandomGenerator();
        geometryCreator = new GeometryCreator();
        emotions = Arrays.asList(
                new Happiness(),
                new Joy(),
                new Compassion(),
                new Admiration(),
                new Depression(),
                new Sadness(),
                new Dissatisfaction(),
                new Rejection());

        mockEmotionalConfigurator = mock(EmotionalConfigurator.class);
        when(mockEmotionalConfigurator.calculateEmotionalState(any())).thenReturn(new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1)));
        when(mockEmotionalConfigurator.getEmotions()).thenCallRealMethod();
        when(mockEmotionalConfigurator.getEmotionalPoint()).thenCallRealMethod();
        when(mockEmotionalConfigurator.getEmotionalState()).thenCallRealMethod();
        doCallRealMethod().when(mockEmotionalConfigurator).updateEmotionalState(any());
    }

    @Test
    public void shouldReturnCorrectList() {
        assertReflectionEquals(mockEmotionalConfigurator.getEmotions(), emotions);
    }

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        mockEmotionalConfigurator.updateEmotionalState(null);
        assertThat(mockEmotionalConfigurator.getEmotionalPoint().getX(), not(0.0));
        assertThat(mockEmotionalConfigurator.getEmotionalPoint().getY(), not(0.0));
        assertThat(mockEmotionalConfigurator.getEmotionalPoint().getX(), is(mockEmotionalConfigurator.getEmotionalState().getActivation()));
        assertThat(mockEmotionalConfigurator.getEmotionalPoint().getY(), is(mockEmotionalConfigurator.getEmotionalState().getSatisfaction()));
    }

    @Test
    public void shouldInvokeEvaluateStimulus() {
        mockEmotionalConfigurator.updateEmotionalState(null);
        verify(mockEmotionalConfigurator).calculateEmotionalState(any());
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
        EmotionalState mockEmotionalState = mock(EmotionalState.class);
        when(mockEmotionalState.toPoint()).thenReturn(randomPoint);
        when(mockEmotionalConfigurator.calculateEmotionalState(any())).thenReturn(mockEmotionalState);
        when(mockEmotionalConfigurator.getEmotionalPoint()).thenReturn(randomPoint);
        when(mockEmotionalConfigurator.getEmotion()).thenCallRealMethod();
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

}