/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Concept;
import jade.core.AID;
import util.ToStringBuilder;

public class Stimulus implements Concept {

    private AID actor;
    private String actionName;

    public Stimulus(AID actor, String actionName) {
        this.actor = actor;
        this.actionName = actionName;
    }

    public Stimulus() {
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
        return new ToStringBuilder()
                .append("actor", actor)
                .append("actionName", actionName)
                .toString();
    }

}
