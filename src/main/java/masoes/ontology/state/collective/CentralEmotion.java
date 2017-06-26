/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state.collective;

import jade.content.Concept;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import util.StringFormatter;
import util.ToStringBuilder;

public class CentralEmotion implements Concept {

    private double activation;
    private double satisfaction;

    public CentralEmotion() {
    }

    public CentralEmotion(double activation, double satisfaction) {
        this.activation = activation;
        this.satisfaction = satisfaction;
    }

    public CentralEmotion(EmotionalState emotionalState) {
        this.activation = emotionalState.getActivation();
        this.satisfaction = emotionalState.getSatisfaction();
    }

    public double getActivation() {
        return activation;
    }

    public void setActivation(double activation) {
        this.activation = activation;
    }

    public double getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(double satisfaction) {
        this.satisfaction = satisfaction;
    }

    public EmotionalState toEmotionalState() {
        return new EmotionalState(activation, satisfaction);
    }

    public Emotion getEmotion() {
        return AffectiveModel.getInstance().searchEmotion(toEmotionalState());
    }

    public String toStringPoint() {
        return StringFormatter.toStringPoint(activation, satisfaction);
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .toString();
    }

}
