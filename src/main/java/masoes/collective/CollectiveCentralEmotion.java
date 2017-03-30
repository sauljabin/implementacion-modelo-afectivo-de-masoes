/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import masoes.component.behavioural.EmotionalState;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Arrays;

public class CollectiveCentralEmotion {

    private DescriptiveStatistics satisfactionStatistics;
    private DescriptiveStatistics activationStatistics;

    public CollectiveCentralEmotion() {
        activationStatistics = new DescriptiveStatistics();
        satisfactionStatistics = new DescriptiveStatistics();
    }

    public void addEmotionalState(double activation, double satisfaction) {
        activationStatistics.addValue(activation);
        satisfactionStatistics.addValue(satisfaction);
    }

    public void addEmotionalState(EmotionalState emotionalState) {
        addEmotionalState(emotionalState.getActivation(), emotionalState.getSatisfaction());
    }

    public EmotionalState getCentralEmotionalState() {
        return new EmotionalState(activationStatistics.getMean(), satisfactionStatistics.getMean());
    }

    public EmotionalState getMaximumDistance() {
        return new EmotionalState(calculateMaximum(activationStatistics), calculateMaximum(satisfactionStatistics));
    }

    private double calculateMaximum(DescriptiveStatistics activationStatistics) {
        double mean = activationStatistics.getMean();

        return Arrays.stream(activationStatistics.getValues())
                .map(value -> Math.abs(value - mean))
                .max()
                .getAsDouble();
    }

    public EmotionalState getEmotionalDispersion() {
        return new EmotionalState(activationStatistics.getStandardDeviation(), satisfactionStatistics.getStandardDeviation());
    }

    public void clear() {
        activationStatistics.clear();
        satisfactionStatistics.clear();
    }

}
