/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;

public class ActionMatchExpression extends OntologyMatchExpression {

    private Class<? extends AgentAction> actionClass;
    private ContentManager contentManager;

    public ActionMatchExpression(Ontology ontology, Class<? extends AgentAction> actionClass) {
        super(ontology);
        contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(ontology);
        this.actionClass = actionClass;
    }

    @Override
    public boolean match(ACLMessage aclMessage) {
        return super.match(aclMessage) && isCorrectAction(aclMessage);
    }

    private boolean isCorrectAction(ACLMessage aclMessage) {
        try {
            Action contentElement = (Action) contentManager.extractContent(aclMessage);
            return contentElement.getAction().getClass().equals(actionClass);
        } catch (Exception e) {
            return false;
        }
    }

}
