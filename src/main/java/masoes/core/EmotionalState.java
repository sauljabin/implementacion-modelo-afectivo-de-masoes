/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

public class EmotionalState {

    private double activation;
    private double satisfaction;

    public EmotionalState(double activation, double satisfaction) {
        this.activation = activation;
        this.satisfaction = satisfaction;
    }

    public double getActivation() {
        return activation;
    }

    public double getSatisfaction() {
        return satisfaction;
    }

}
