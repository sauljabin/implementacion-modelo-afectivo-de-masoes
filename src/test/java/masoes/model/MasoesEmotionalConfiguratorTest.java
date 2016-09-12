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
import masoes.util.RandomGenerator;
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

public class MasoesEmotionalConfiguratorTest {

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
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForBasicEmotion(2, 10, 2, 10));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Happiness.class)));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForBasicEmotion(0, 0.5, 0, 0.5));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Joy.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Admiration.class)));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Sadness.class)));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForBasicEmotion(0, 0.5, -0.5, 0));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Rejection.class)));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForExternalEmotion(1, 1));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Happiness.class)));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForExternalEmotion(-1, 1));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Compassion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForExternalEmotion(-1, -1));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Depression.class)));
    }

    @Test
    public void shouldReturnDissatisfactionEmotion() {
        MasoesEmotionalConfigurator fakeConfigurator = createFakeEmotionalConfigurator(getRandomPointForExternalEmotion(1, -1));
        assertThat(fakeConfigurator.getEmotionalState(), is(instanceOf(Dissatisfaction.class)));
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

    public MasoesEmotionalConfigurator createFakeEmotionalConfigurator(Point randomPoint) {
        MasoesEmotionalConfigurator fakeConfigurator = mock(MasoesEmotionalConfigurator.class);
        when(fakeConfigurator.getEmotionalPoint()).thenReturn(randomPoint);
        when(fakeConfigurator.getEmotionalState()).thenCallRealMethod();
        when(fakeConfigurator.getEmotions()).thenReturn(emotions);
        return fakeConfigurator;
    }

}