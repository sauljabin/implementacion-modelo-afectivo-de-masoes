/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulus.table;

import gui.configurator.stimulus.StimulusModel;
import util.ToStringBuilder;

public class SelectableStimulusModel {

    private boolean selected;
    private StimulusModel model;

    public SelectableStimulusModel(StimulusModel model) {
        this.model = model;
        this.selected = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public StimulusModel getModel() {
        return model;
    }

    public void setModel(StimulusModel model) {
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
