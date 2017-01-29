/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MasoesRequestMatchExpressionTest {

    private MasoesRequestMatchExpression masoesRequestMatchExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        masoesRequestMatchExpression = new MasoesRequestMatchExpression();
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(MasoesOntology.ONTOLOGY_NAME);
    }

    @Test
    public void shouldMatchWithMasoesMessage() {
        assertTrue(masoesRequestMatchExpression.match(message));
    }

}