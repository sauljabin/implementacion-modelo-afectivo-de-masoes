/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.configurable;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class RemoveBehaviour implements AgentAction {

    private String name;

    public RemoveBehaviour() {
    }

    public RemoveBehaviour(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", name)
                .toString();
    }

}


