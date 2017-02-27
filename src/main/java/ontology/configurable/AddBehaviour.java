/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.configurable;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class AddBehaviour implements AgentAction {

    private String name;
    private String className;

    public AddBehaviour() {
    }

    public AddBehaviour(String name, String className) {
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", name)
                .append("className", className)
                .toString();
    }

}


