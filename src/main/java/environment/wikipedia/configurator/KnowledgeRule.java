/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

public class KnowledgeRule {

    private boolean selected;
    private String rule;
    private String activation;
    private String satisfaction;

    public KnowledgeRule() {
    }

    public KnowledgeRule(boolean selected, String rule, String activation, String satisfaction) {
        this.selected = selected;
        this.rule = rule;
        this.activation = activation;
        this.satisfaction = satisfaction;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

}
