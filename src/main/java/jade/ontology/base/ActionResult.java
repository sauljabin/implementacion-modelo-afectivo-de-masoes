/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import jade.content.AgentAction;
import jade.content.Predicate;

public class ActionResult implements Predicate {

    private String result;
    private AgentAction action;

    public ActionResult() {
    }

    public ActionResult(String result, AgentAction action) {
        this.result = result;
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public AgentAction getAction() {
        return action;
    }

    public void setAction(AgentAction action) {
        this.action = action;
    }
}
