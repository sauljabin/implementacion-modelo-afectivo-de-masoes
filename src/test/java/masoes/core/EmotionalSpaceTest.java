/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.core.emotion.Admiration;
import masoes.core.emotion.Compassion;
import masoes.core.emotion.Depression;
import masoes.core.emotion.Dissatisfaction;
import masoes.core.emotion.Happiness;
import masoes.core.emotion.Joy;
import masoes.core.emotion.Rejection;
import masoes.core.emotion.Sadness;
import masoes.util.math.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class EmotionalSpaceTest {

    private static final int POSITIVE_SIGN = 1;
    private static final int NEGATIVE_SIGN = -1;

    private EmotionalSpace emotionalSpace;
    private RandomGenerator random;

    @Before
    public void setUp() {
        random = new RandomGenerator();
        emotionalSpace = new EmotionalSpace();
    }

    @Test
    public void shouldReturnDefaultHappinessEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(2, 10, 2, 10);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Happiness.class)));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Joy.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Admiration.class)));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Sadness.class)));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Rejection.class)));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Happiness.class)));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Compassion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Depression.class)));
    }

    @Test
    public void shouldReturnDissatisfactionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(Dissatisfaction.class)));
    }

    private EmotionalState getRandomPointForExternalEmotion(double xSign, double ySign) {
        double x = xSign * random.getDouble(0, 1);
        double y = ySign * random.getDouble(0, 1);
        if (xSign * x < 0.5)
            y = ySign * random.getDouble(0.5, 1);
        return new EmotionalState(x, y);
    }

    private EmotionalState getRandomPointForBasicEmotion(double xMin, double xMax, double yMin, double yMax) {
        double x = random.getDouble(xMin, xMax);
        double y = random.getDouble(yMin, yMax);
        return new EmotionalState(x, y);
    }

}