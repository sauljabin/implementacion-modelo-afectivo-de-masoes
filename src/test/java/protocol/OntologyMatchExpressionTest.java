/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package protocol;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OntologyMatchExpressionTest {

    private OntologyMatchExpression configuringAgentMatchExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        String expectedOntology = "expectedOntology";
        configuringAgentMatchExpression = new OntologyMatchExpression(expectedOntology);
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(expectedOntology);
    }

    @Test
    public void shouldMatchWithConfigurableRequestMessage() {
        assertTrue(configuringAgentMatchExpression.match(message));
    }

}