/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusdefinition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import util.StringFormatter;
import util.ToStringBuilder;

public class StimulusDefinitionModel {

    private String name;
    private String value;
    private double activation;
    private double satisfaction;
    private boolean self;

    public StimulusDefinitionModel() {
        this("", "", 0, 0, true);
    }

    public StimulusDefinitionModel(String name, double activation, double satisfaction, boolean self) {
        this.name = name;
        this.activation = activation;
        this.satisfaction = satisfaction;
        this.self = self;
        this.value = StringFormatter.toCamelCase(name);
    }

    public StimulusDefinitionModel(String name, String value, double activation, double satisfaction, boolean self) {
        this.name = name;
        this.value = value;
        this.activation = activation;
        this.satisfaction = satisfaction;
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", name)
                .append("value", value)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .append("self", self)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StimulusDefinitionModel that = (StimulusDefinitionModel) o;

        return new EqualsBuilder()
                .append(activation, that.activation)
                .append(satisfaction, that.satisfaction)
                .append(self, that.self)
                .append(name, that.name)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(value)
                .append(activation)
                .append(satisfaction)
                .append(self)
                .toHashCode();
    }
}
