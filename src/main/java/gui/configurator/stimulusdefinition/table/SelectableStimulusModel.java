/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulusdefinition.table;

import gui.configurator.stimulusdefinition.StimulusDefinitionModel;
import util.ToStringBuilder;

public class SelectableStimulusModel {

    private boolean selected;
    private StimulusDefinitionModel model;

    public SelectableStimulusModel(StimulusDefinitionModel model) {
        this.model = model;
        this.selected = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public StimulusDefinitionModel getModel() {
        return model;
    }

    public void setModel(StimulusDefinitionModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("selected", selected)
                .append("model", model)
                .toString();
    }

}
