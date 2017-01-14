/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import jade.content.AgentAction;
import jade.content.Predicate;

public class FailedAction implements Predicate {

    private String reason;
    private AgentAction action;

    public FailedAction() {
    }

    public FailedAction(String reason, AgentAction action) {
        this.reason = reason;
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
    }

}
