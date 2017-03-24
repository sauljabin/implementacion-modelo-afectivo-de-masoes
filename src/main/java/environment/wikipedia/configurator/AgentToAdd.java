/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

public class AgentToAdd {

    private AgentToAddType type;
    private int sequence;

    public AgentToAdd() {
    }

    public AgentToAdd(AgentToAddType type, int sequence) {
        this.type = type;
        this.sequence = sequence;
    }

    public AgentToAddType getType() {
        return type;
    }

    public void setType(AgentToAddType type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return String.format("%s%d", type.toString(), sequence);
    }

}
