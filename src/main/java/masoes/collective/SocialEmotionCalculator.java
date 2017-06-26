/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import masoes.component.behavioural.EmotionalState;
import masoes.ontology.state.collective.CentralEmotion;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.MaximumDistances;

import java.util.ArrayList;
import java.util.List;

public class SocialEmotionCalculator {

    private List<EmotionalState> emotionalStates;

    public SocialEmotionCalculator() {
        emotionalStates = new ArrayList<>();
    }

    public void addEmotionalState(EmotionalState emotionalState) {
        emotionalStates.add(emotionalState);
    }

    public void clear() {
        emotionalStates.clear();
    }

    public double getActivationMean() {
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(EmotionalState::getActivation)
                .average()
                .getAsDouble();
    }

    public double getSatisfactionMean() {
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(EmotionalState::getSatisfaction)
                .average()
                .getAsDouble();
    }

    public double getActivationStandardDeviation() {
        return Math.sqrt(getActivationVariance());
    }

    public double getSatisfactionStandardDeviation() {
        return Math.sqrt(getSatisfactionVariance());
    }

    public double getActivationVariance() {
        double mean = getActivationMean();
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(value -> Math.pow(value.getActivation() - mean, 2))
                .sum() / emotionalStates.size();
    }

    public double getSatisfactionVariance() {
        double mean = getSatisfactionMean();
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(value -> Math.pow(value.getSatisfaction() - mean, 2))
                .sum() / emotionalStates.size();
    }

    public double getActivationMaximumDistance() {
        double mean = getActivationMean();
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(value -> Math.abs(value.getActivation() - mean))
                .max()
                .getAsDouble();
    }

    public double getSatisfactionMaximumDistance() {
        double mean = getSatisfactionMean();
        return emotionalStates.isEmpty() ? 0 : emotionalStates.stream()
                .mapToDouble(value -> Math.abs(value.getSatisfaction() - mean))
                .max()
                .getAsDouble();
    }

    public CentralEmotion getCentralEmotionalState() {
        return new CentralEmotion(getActivationMean(), getSatisfactionMean());
    }

    public MaximumDistances getMaximumDistances() {
        return new MaximumDistances(getActivationMaximumDistance(), getSatisfactionMaximumDistance());
    }

    public EmotionalDispersion getEmotionalDispersion() {
        return new EmotionalDispersion(getActivationStandardDeviation(), getSatisfactionStandardDeviation());
    }

}
