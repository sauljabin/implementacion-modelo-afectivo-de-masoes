/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import masoes.core.Emotion;
import masoes.emotion.*;
import masoes.stimulus.InitialStimulus;
import masoes.util.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MasoesEmotionalConfiguratorTest {

    public static final int POSITIVE_SIGN = 1;
    public static final int NEGATIVE_SIGN = -1;
    private List<Emotion> emotions;
    private GeometryFactory geometryFactory;
    private RandomGenerator random;

    @Before
    public void setUp() throws Exception {
        emotions = new ArrayList<Emotion>();
        emotions.add(new Happiness());
        emotions.add(new Joy());
        emotions.add(new Compassion());
        emotions.add(new Admiration());
        emotions.add(new Depression());
        emotions.add(new Sadness());
        emotions.add(new Dissatisfaction());
        emotions.add(new Rejection());
        geometryFactory = new GeometryFactory();
        random = new RandomGenerator();
    }

    @Test
    public void shouldReturnCorrectList() {
        MasoesEmotionalConfigurator configurator = new MasoesEmotionalConfigurator();
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

    public void testEmotion(Class<?> emotionClass, Point randomPoint) {
        MasoesEmotionalConfigurator fakeConfigurator = mock(MasoesEmotionalConfigurator.class);
        when(fakeConfigurator.getEmotionalPoint()).thenReturn(randomPoint);
        when(fakeConfigurator.getEmotionalState()).thenCallRealMethod();
        when(fakeConfigurator.getEmotions()).thenReturn(emotions);
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(emotionClass)));
    }

    private Point getRandomPointForBasicEmotion(double xMin, double xMax, double yMin, double yMax) {
        double x = random.random(xMin, xMax);
        double y = random.random(yMin, yMax);
        return createPoint(x, y);
    }

    private Point getRandomPointForExternalEmotion(double xSign, double ySign) {
        double x = xSign * random.random(0, 1);
        double y = ySign * random.random(0, 1);
        if (xSign * x < 0.5)
            y = ySign * random.random(0.5, 1);
        return createPoint(x, y);
    }

    private Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    @Test
    public void shouldInitRandomValues() {
        MasoesEmotionalConfigurator configurator = new MasoesEmotionalConfigurator();
        evaluateRandom(configurator);
    }

    public void evaluateRandom(MasoesEmotionalConfigurator configurator) {
        assertThat(configurator.getEmotionalPoint().getX(), is(greaterThanOrEqualTo(-1.)));
        assertThat(configurator.getEmotionalPoint().getX(), is(lessThanOrEqualTo(1.)));
        assertThat(configurator.getEmotionalPoint().getX(), is(not(0.0)));

        assertThat(configurator.getEmotionalPoint().getY(), is(greaterThanOrEqualTo(-1.)));
        assertThat(configurator.getEmotionalPoint().getY(), is(lessThanOrEqualTo(1.)));
        assertThat(configurator.getEmotionalPoint().getY(), is(not(0.0)));
    }

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        MasoesEmotionalConfigurator configurator = new MasoesEmotionalConfigurator();
        assertThat(configurator.getEmotionalPoint().getX(), is(configurator.getActivation()));
        assertThat(configurator.getEmotionalPoint().getY(), is(configurator.getSatisfaction()));
    }

    @Test
    public void shouldGenerateRandomStimulus() {
        MasoesEmotionalConfigurator configurator = new MasoesEmotionalConfigurator();
        configurator.evaluateStimulus(new InitialStimulus());
        evaluateRandom(configurator);
    }

}