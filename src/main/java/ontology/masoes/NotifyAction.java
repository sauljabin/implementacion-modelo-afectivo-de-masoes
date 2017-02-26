/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.AgentAction;
import jade.core.AID;
import util.ToStringBuilder;

public class NotifyAction implements AgentAction {

    private AID actor;
    private String actionName;

    public NotifyAction() {
    }

    public NotifyAction(AID actor, String actionName) {
        this.actor = actor;
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public AID getActor() {
        return actor;
    }

    public void setActor(AID actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("actor", actor)
                .append("actionName", actionName)
                .toString();
    }

}
