/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.app.setting.Setting;

import java.util.Optional;

public class SettingsBehaviour extends CyclicBehaviour {

    private MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);


        if (msg != null) {

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            if (msg.getContent() == null) {
                reply.setContent(Setting.allToString());
            } else if (Optional.ofNullable(Setting.get(msg.getContent(), null)).isPresent()) {
                reply.setContent(Setting.get(msg.getContent()));
            } else {
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            }

            myAgent.send(reply);

        } else {
            block();
        }

    }
}
