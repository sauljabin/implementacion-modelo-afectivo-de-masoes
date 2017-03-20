/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.notifier;

import jade.content.AgentAction;
import masoes.ontology.stimulus.ObjectStimulus;
import util.ToStringBuilder;

public class NotifyObject implements AgentAction {

    private ObjectStimulus objectStimulus;

    public NotifyObject() {
    }

    public NotifyObject(ObjectStimulus objectStimulus) {
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
