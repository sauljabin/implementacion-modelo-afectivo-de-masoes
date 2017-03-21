/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state;

import jade.content.Concept;
import util.ToStringBuilder;

public class BehaviourState implements Concept {

    private String type;

    public BehaviourState() {
    }

    public BehaviourState(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("type", type)
                .toString();
    }

}
