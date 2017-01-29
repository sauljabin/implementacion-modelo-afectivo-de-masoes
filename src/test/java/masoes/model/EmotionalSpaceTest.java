/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.model.emotion.AdmirationEmotion;
import masoes.model.emotion.AngerEmotion;
import masoes.model.emotion.CompassionEmotion;
import masoes.model.emotion.DepressionEmotion;
import masoes.model.emotion.HappinessEmotion;
import masoes.model.emotion.JoyEmotion;
import masoes.model.emotion.RejectionEmotion;
import masoes.model.emotion.SadnessEmotion;
import math.random.RandomGenerator;
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
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(JoyEmotion.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(AdmirationEmotion.class)));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(SadnessEmotion.class)));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(RejectionEmotion.class)));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(CompassionEmotion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(DepressionEmotion.class)));
    }

    @Test
    public void shouldReturnDissatisfactionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(AngerEmotion.class)));
    }

    private EmotionalState getRandomPointForExternalEmotion(double xSign, double ySign) {
        double x = xSign * random.getDouble(0, 1);
        double y = ySign * random.getDouble(0, 1);
        if (xSign * x < 0.5) {
            y = ySign * random.getDouble(0.5, 1);
        }
        return new EmotionalState(x, y);
    }

    private EmotionalState getRandomPointForBasicEmotion(double xMin, double xMax, double yMin, double yMax) {
        double x = random.getDouble(xMin, xMax);
        double y = random.getDouble(yMin, yMax);
        return new EmotionalState(x, y);
    }

}