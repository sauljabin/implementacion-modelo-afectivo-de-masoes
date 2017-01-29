/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import masoes.agent.MasoesMatchRequestExpression;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MasoesMatchRequestExpressionTest {

    private MasoesMatchRequestExpression masoesMatchRequestExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        masoesMatchRequestExpression = new MasoesMatchRequestExpression();
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(MasoesOntology.ONTOLOGY_NAME);
    }

    @Test
    public void shouldMatchWithMasoesMessage() {
        assertTrue(masoesMatchRequestExpression.match(message));
    }

}