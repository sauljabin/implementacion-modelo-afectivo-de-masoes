/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import org.junit.Before;
import org.junit.Test;
import util.RandomGenerator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
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
    public void shouldReturnDefaultNullEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("no-emotion"), is(nullValue()));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnHappinessEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("happiness"), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(JoyEmotion.class)));
    }

    @Test
    public void shouldReturnJoyEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("joy"), is(instanceOf(JoyEmotion.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(AdmirationEmotion.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("admiration"), is(instanceOf(AdmirationEmotion.class)));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(SadnessEmotion.class)));
    }

    @Test
    public void shouldReturnSadnessEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("sadness"), is(instanceOf(SadnessEmotion.class)));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, -0.5, 0);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(RejectionEmotion.class)));
    }

    @Test
    public void shouldReturnRejectionEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("rejection"), is(instanceOf(RejectionEmotion.class)));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, POSITIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(CompassionEmotion.class)));
    }

    @Test
    public void shouldReturnCompassionEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("compassion"), is(instanceOf(CompassionEmotion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(DepressionEmotion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("depression"), is(instanceOf(DepressionEmotion.class)));
    }

    @Test
    public void shouldReturnAngerEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, NEGATIVE_SIGN);
        assertThat(emotionalSpace.searchEmotion(randomPoint), is(instanceOf(AngerEmotion.class)));
    }

    @Test
    public void shouldReturnAngerEmotionFromString() {
        assertThat(emotionalSpace.searchEmotion("anger"), is(instanceOf(AngerEmotion.class)));
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