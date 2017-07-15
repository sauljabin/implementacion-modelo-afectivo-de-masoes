/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusconfiguration;

import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import knowledge.KnowledgeClause;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import util.StringFormatter;
import util.ToStringBuilder;

public class StimulusConfigurationModel {

    private boolean selected;
    private StimulusDefinitionModel stimulusDefinition;

    private double activation;
    private double satisfaction;
    private boolean self;

    public StimulusConfigurationModel() {
    }

    public StimulusConfigurationModel(StimulusDefinitionModel stimulusDefinition) {
        this.stimulusDefinition = stimulusDefinition;
        activation = stimulusDefinition.getActivation();
        satisfaction = stimulusDefinition.getSatisfaction();
        self = stimulusDefinition.isSelf();
        selected = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public StimulusDefinitionModel getStimulusDefinition() {
        return stimulusDefinition;
    }

    public void setStimulusDefinition(StimulusDefinitionModel stimulusDefinition) {
        this.stimulusDefinition = stimulusDefinition;
    }

    public String toClause() {
        return new KnowledgeClause("stimulus")
                .argument("AGENT")
                .argument(stimulusDefinition.getValue())
                .argument(StringFormatter.toString(activation))
                .argument(StringFormatter.toString(satisfaction))
                .body(self ? "self(AGENT)" : "other(AGENT)")
                .toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("selected", selected)
                .append("stimulusDefinition", stimulusDefinition)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .append("self", self)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StimulusConfigurationModel that = (StimulusConfigurationModel) o;

        return new EqualsBuilder()
                .append(selected, that.selected)
                .append(activation, that.activation)
                .append(satisfaction, that.satisfaction)
                .append(self, that.self)
                .append(stimulusDefinition, that.stimulusDefinition)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(selected)
                .append(stimulusDefinition)
                .append(activation)
                .append(satisfaction)
                .append(self)
                .toHashCode();
    }

}
