/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.notifier;

import jade.content.AgentAction;
import masoes.ontology.stimulus.EventStimulus;
import util.ToStringBuilder;

public class NotifyEvent implements AgentAction {

    private EventStimulus eventStimulus;

    public NotifyEvent() {
    }

    public NotifyEvent(EventStimulus eventStimulus) {
        this.eventStimulus = eventStimulus;
    }

    public EventStimulus getEventStimulus() {
        return eventStimulus;
    }

    public void setEventStimulus(EventStimulus eventStimulus) {
        this.eventStimulus = eventStimulus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("eventStimulus", eventStimulus)
                .toString();
    }

}
