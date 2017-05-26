/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;

public class ActionMatchExpression extends OntologyMatchExpression {

    private Class<? extends AgentAction> action;
    private ContentManager contentManager;

    public ActionMatchExpression(Ontology ontology, Class<? extends AgentAction> action) {
        super(ontology);
        contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(ontology);
        this.action = action;
    }

    @Override
    public boolean match(ACLMessage aclMessage) {
        return super.match(aclMessage) && isCorrectAction(aclMessage, action);
    }

    private boolean isCorrectAction(ACLMessage aclMessage, Class<? extends AgentAction> action) {
        try {
            return contentManager.extractContent(aclMessage).getClass().equals(action);
        } catch (Exception e) {
            throw new ExtractOntologyContentException(e);
        }
    }

}
