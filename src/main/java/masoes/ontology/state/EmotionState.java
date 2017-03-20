/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state;

import jade.content.Concept;
import util.ToStringBuilder;

public class EmotionState implements Concept {

    private String name;
    private String className;
    private String type;
    private double activation;
    private double satisfaction;

    public EmotionState() {
    }

    public EmotionState(String name, String className, String type, double activation, double satisfaction) {
        this.name = name;
        this.className = className;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", name)
                .append("className", className)
                .append("type", type)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .toString();
    }

}
