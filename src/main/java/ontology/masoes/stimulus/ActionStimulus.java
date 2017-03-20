/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes.stimulus;

import jade.core.AID;
import util.ToStringBuilder;

public class ActionStimulus extends Stimulus {

    private AID actor;
    private String actionName;

    public ActionStimulus(AID actor, String actionName) {
        this.actor = actor;
        this.actionName = actionName;
    }

    public ActionStimulus() {
    }

    public AID getActor() {
        return actor;
    }

    public void setActor(AID actor) {
        this.actor = actor;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder();
        toStringBuilder.object(this);

        if (actor != null) {
            toStringBuilder.append("actor", actor.getLocalName());
        }

        return toStringBuilder.append("actionName", actionName).toString();
    }

}
