/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Concept;
import util.ToStringBuilder;

public class EmotionStatus implements Concept {

    private double activation;
    private double satisfaction;

    public EmotionStatus() {
    }

    public EmotionStatus(double activation, double satisfaction) {
        this.activation = activation;
        this.satisfaction = satisfaction;
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

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .toString();
    }

}
