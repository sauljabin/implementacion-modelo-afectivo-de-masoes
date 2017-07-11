/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusconfiguration;

import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import knowledge.KnowledgeClause;
import util.StringFormatter;
import util.ToStringBuilder;

public class StimulusConfigurationModel {

    private boolean selected;
    private StimulusDefinitionModel model;

    private double activation;
    private double satisfaction;
    private boolean self;

    public StimulusConfigurationModel(StimulusDefinitionModel model) {
        this.model = model;
        activation = model.getActivation();
        satisfaction = model.getSatisfaction();
        self = model.isSelf();
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

    public StimulusDefinitionModel getModel() {
        return model;
    }

    public String toClause() {
        return new KnowledgeClause("stimulus")
                .argument("AGENT")
                .argument(model.getValue())
                .argument(StringFormatter.toString(activation))
                .argument(StringFormatter.toString(satisfaction))
                .body(self ? "self(AGENT)" : "other(AGENT)")
                .toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("selected", selected)
                .append("model", model)
                .append("activation", activation)
                .append("satisfaction", satisfaction)
                .append("self", self)
                .toString();
    }

}
