/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class NotifyAction implements AgentAction {

    private ActionStimulus actionStimulus;

    public NotifyAction() {
    }

    public NotifyAction(ActionStimulus actionStimulus) {
        this.actionStimulus = actionStimulus;
    }

    public ActionStimulus getActionStimulus() {
        return actionStimulus;
    }

    public void setActionStimulus(ActionStimulus actionStimulus) {
        this.actionStimulus = actionStimulus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("actionStimulus", actionStimulus)
                .toString();
    }

}
