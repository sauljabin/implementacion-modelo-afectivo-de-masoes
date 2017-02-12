/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Concept;
import util.ToStringBuilder;

public class EmotionState implements Concept {

    private double activation;
    private double satisfaction;
    private String className;
    private String name;

    public EmotionState() {
    }

    public EmotionState(String name, String className, double activation, double satisfaction) {
        this.activation = activation;
        this.satisfaction = satisfaction;
        this.className = className;
        this.name = name;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", name)
                .append("className", className)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .toString();
    }

}
