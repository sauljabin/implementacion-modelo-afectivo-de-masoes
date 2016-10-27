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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
    public void shouldReturnDefaultHappinessEmotion() {
        testEmotion(Happiness.class, getRandomPointForBasicEmotion(2, 10, 2, 10));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        testEmotion(Joy.class, getRandomPointForBasicEmotion(0, 0.5, 0, 0.5));
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

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        configurator.evaluateStimulus(null);
        assertThat(configurator.getEmotionalPoint().getX(), is(configurator.getEmotionalState().getActivation()));
        assertThat(configurator.getEmotionalPoint().getY(), is(configurator.getEmotionalState().getSatisfaction()));
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

    public void testEmotion(Class<?> emotionClass, Point randomPoint) {
        EmotionalConfigurator fakeConfigurator = mock(EmotionalConfigurator.class);
        when(fakeConfigurator.getEmotionalPoint()).thenReturn(randomPoint);
        when(fakeConfigurator.getEmotion()).thenCallRealMethod();
        when(fakeConfigurator.getEmotions()).thenReturn(emotions);
        assertThat(fakeConfigurator.getEmotion(), is(instanceOf(emotionClass)));
    }

    private EmotionalConfigurator createEmotionalConfigurator() {
        return new EmotionalConfigurator() {

            @Override
            public EmotionalState updateEmotionalState(Stimulus stimulus) {
                return new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
            }

        };
    }

}