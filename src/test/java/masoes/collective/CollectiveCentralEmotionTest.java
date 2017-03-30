/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import masoes.component.behavioural.EmotionalState;
import org.junit.Before;
import org.junit.Test;
import test.RandomUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CollectiveCentralEmotionTest {

    private CollectiveCentralEmotion collectiveCentralEmotion;
    private List<EmotionalState> emotionalStates;

    @Before
    public void setUp() {
        collectiveCentralEmotion = new CollectiveCentralEmotion();

        emotionalStates = getRandomValues();
        emotionalStates.forEach(emotionalState -> collectiveCentralEmotion.addEmotionalState(emotionalState));
    }

    @Test
    public void shouldGetMeanEmotion() {
        double activationAverage = getActivationAverage();
        double satisfactionAverage = getSatisfactionAverage();

        assertThat(collectiveCentralEmotion.getCentralEmotionalState().getActivation(), is(activationAverage));
        assertThat(collectiveCentralEmotion.getCentralEmotionalState().getSatisfaction(), is(satisfactionAverage));
    }

    public double getSatisfactionAverage() {
        return emotionalStates.stream()
                .mapToDouble(value -> value.getSatisfaction())
                .average()
                .getAsDouble();
    }

    @Test
    public void shouldGetDispersionEmotion() {
        double activationAverage = getActivationAverage();

        double activationVariance = emotionalStates.stream()
                .mapToDouble(value -> Math.pow(value.getActivation() - activationAverage, 2))
                .sum() / emotionalStates.size();

        double activationStandardDeviation = Math.sqrt(activationVariance);


        double satisfactionAverage = getSatisfactionAverage();

        double satisfactionVariance = emotionalStates.stream()
                .mapToDouble(value -> Math.pow(value.getSatisfaction() - satisfactionAverage, 2))
                .sum() / emotionalStates.size();

        double satisfactionStandardDeviation = Math.sqrt(satisfactionVariance);


        assertThat(collectiveCentralEmotion.getEmotionalDispersion().getActivation(), is(activationStandardDeviation));
        assertThat(collectiveCentralEmotion.getEmotionalDispersion().getSatisfaction(), is(satisfactionStandardDeviation));
    }

    public double getActivationAverage() {
        return emotionalStates.stream()
                .mapToDouble(value -> value.getActivation())
                .average()
                .getAsDouble();
    }

    @Test
    public void shouldGetMaximumDistance() {
        double activationAverage = getActivationAverage();

        double maxActivation = emotionalStates.stream()
                .mapToDouble(value -> Math.abs(value.getActivation() - activationAverage))
                .max()
                .getAsDouble();


        double satisfactionAverage = getSatisfactionAverage();

        double maxSatisfaction = emotionalStates.stream()
                .mapToDouble(value -> Math.abs(value.getSatisfaction() - satisfactionAverage))
                .max()
                .getAsDouble();


        assertThat(collectiveCentralEmotion.getMaximumDistance().getActivation(), is(maxActivation));
        assertThat(collectiveCentralEmotion.getMaximumDistance().getSatisfaction(), is(maxSatisfaction));
    }

    private List<EmotionalState> getRandomValues() {
        return IntStream.range(0, 6)
                .mapToObj(value -> new EmotionalState(RandomUtil.randomDouble(), RandomUtil.randomDouble()))
                .collect(Collectors.toList());
    }

}