/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.stimulus;

import jade.core.AID;
import util.ToStringBuilder;

public class EventStimulus extends Stimulus {

    private AID affected;
    private String eventName;

    public EventStimulus() {
    }

    public EventStimulus(AID affected, String eventName) {
        this.affected = affected;
        this.eventName = eventName;
    }

    public AID getAffected() {
        return affected;
    }

    public void setAffected(AID affected) {
        this.affected = affected;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder();
        toStringBuilder.object(this);

        if (affected != null) {
            toStringBuilder.append("affected", affected.getLocalName());
        }

        return toStringBuilder.append("eventName", eventName)
                .toString();
    }

}
