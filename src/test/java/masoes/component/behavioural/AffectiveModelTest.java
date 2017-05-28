/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import masoes.component.behavioural.emotion.AdmirationEmotion;
import masoes.component.behavioural.emotion.AngerEmotion;
import masoes.component.behavioural.emotion.CompassionEmotion;
import masoes.component.behavioural.emotion.DepressionEmotion;
import masoes.component.behavioural.emotion.HappinessEmotion;
import masoes.component.behavioural.emotion.JoyEmotion;
import masoes.component.behavioural.emotion.RejectionEmotion;
import masoes.component.behavioural.emotion.SadnessEmotion;
import org.junit.Before;
import org.junit.Test;
import util.RandomGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class AffectiveModelTest {

    private static final int POSITIVE_SIGN = 1;
    private static final int NEGATIVE_SIGN = -1;

    private AffectiveModel affectiveModel;

    @Before
    public void setUp() {
        affectiveModel = AffectiveModel.getInstance();
    }

    @Test
    public void shouldReturnSameInstance() {
        assertThat(affectiveModel, is(AffectiveModel.getInstance()));
    }

    @Test
    public void shouldReturnDefaultNullEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("no-emotion"), is(nullValue()));
    }

    @Test
    public void shouldReturnHappinessEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, POSITIVE_SIGN);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnHappinessEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("happiness"), is(instanceOf(HappinessEmotion.class)));
    }

    @Test
    public void shouldReturnJoyEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, 0, 0.5);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(JoyEmotion.class)));
    }

    @Test
    public void shouldReturnJoyEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("joy"), is(instanceOf(JoyEmotion.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, 0, 0.5);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(AdmirationEmotion.class)));
    }

    @Test
    public void shouldReturnAdmirationEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("admiration"), is(instanceOf(AdmirationEmotion.class)));
    }

    @Test
    public void shouldReturnSadnessEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(-0.5, 0, -0.5, 0);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(SadnessEmotion.class)));
    }

    @Test
    public void shouldReturnSadnessEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("sadness"), is(instanceOf(SadnessEmotion.class)));
    }

    @Test
    public void shouldReturnRejectionEmotion() {
        EmotionalState randomPoint = getRandomPointForBasicEmotion(0, 0.5, -0.5, 0);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(RejectionEmotion.class)));
    }

    @Test
    public void shouldReturnRejectionEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("rejection"), is(instanceOf(RejectionEmotion.class)));
    }

    @Test
    public void shouldReturnCompassionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, POSITIVE_SIGN);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(CompassionEmotion.class)));
    }

    @Test
    public void shouldReturnCompassionEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("compassion"), is(instanceOf(CompassionEmotion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(NEGATIVE_SIGN, NEGATIVE_SIGN);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(DepressionEmotion.class)));
    }

    @Test
    public void shouldReturnDepressionEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("depression"), is(instanceOf(DepressionEmotion.class)));
    }

    @Test
    public void shouldReturnAngerEmotion() {
        EmotionalState randomPoint = getRandomPointForExternalEmotion(POSITIVE_SIGN, NEGATIVE_SIGN);
        assertThat(affectiveModel.searchEmotion(randomPoint), is(instanceOf(AngerEmotion.class)));
    }

    @Test
    public void shouldReturnAngerEmotionFromString() {
        assertThat(affectiveModel.searchEmotion("anger"), is(instanceOf(AngerEmotion.class)));
    }

    private EmotionalState getRandomPointForExternalEmotion(double xSign, double ySign) {
        double x = xSign * RandomGenerator.getDouble(0, 1);
        double y = ySign * RandomGenerator.getDouble(0, 1);
        if (xSign * x < 0.5) {
            y = ySign * RandomGenerator.getDouble(0.5, 1);
        }
        return new EmotionalState(x, y);
    }

    private EmotionalState getRandomPointForBasicEmotion(double xMin, double xMax, double yMin, double yMax) {
        double x = RandomGenerator.getDouble(xMin, xMax);
        double y = RandomGenerator.getDouble(yMin, yMax);
        return new EmotionalState(x, y);
    }

    @Test
    public void shouldGetRandomEmotion() {
        Emotion randomEmotion = affectiveModel.getRandomEmotion();

        List<Emotion> occurrences = IntStream.range(0, 5)
                .mapToObj(i -> affectiveModel.getRandomEmotion())
                .filter(emotion -> !emotion.getName().equals(randomEmotion.getName()))
                .collect(Collectors.toList());

        assertThat(occurrences, is(not(empty())));
    }

}