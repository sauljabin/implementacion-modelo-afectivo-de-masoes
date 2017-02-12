/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Concept;
import util.ToStringBuilder;

public class BehaviourState implements Concept {

    private String className;
    private String name;

    public BehaviourState(String name, String className) {
        this.className = className;
        this.name = name;
    }

    public BehaviourState() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
                .append("className", className)
                .toString();
    }

}
