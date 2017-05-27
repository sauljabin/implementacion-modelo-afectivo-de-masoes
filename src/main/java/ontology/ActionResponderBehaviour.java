/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.AgentAction;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;

public class ActionResponderBehaviour extends OntologyResponderBehaviour {

    private final Class<? extends AgentAction> actionClass;

    public ActionResponderBehaviour(Agent agent, MessageTemplate messageTemplate, Ontology ontology, Class<? extends AgentAction> actionClass) {
        super(agent, messageTemplate, ontology);
        this.actionClass = actionClass;
    }

    public ActionResponderBehaviour(Agent agent, Ontology ontology, Class<? extends AgentAction> actionClass) {
        this(agent, new MessageTemplate(new ActionMatchExpression(ontology, actionClass)), ontology, actionClass);
    }

    @Override
    public boolean isValidAction(Action action) {
        return action.getAction().getClass().equals(actionClass);
    }

}
