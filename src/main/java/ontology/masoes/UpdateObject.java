/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class UpdateObject implements AgentAction {

    private ObjectStimulus objectStimulus;

    public UpdateObject() {
    }

    public UpdateObject(ObjectStimulus objectStimulus) {
        this.objectStimulus = objectStimulus;
    }

    public ObjectStimulus getObjectStimulus() {
        return objectStimulus;
    }

    public void setObjectStimulus(ObjectStimulus objectStimulus) {
        this.objectStimulus = objectStimulus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("objectStimulus", objectStimulus)
                .toString();
    }

}
