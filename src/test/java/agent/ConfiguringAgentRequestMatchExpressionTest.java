/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import ontology.configurable.ConfigurableOntology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConfiguringAgentRequestMatchExpressionTest {

    private ConfiguringAgentRequestMatchExpression configuringAgentRequestMatchExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        configuringAgentRequestMatchExpression = new ConfiguringAgentRequestMatchExpression();
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(ConfigurableOntology.ONTOLOGY_NAME);
    }

    @Test
    public void shouldMatchWithConfigurableRequestMessage() {
        assertTrue(configuringAgentRequestMatchExpression.match(message));
    }

}