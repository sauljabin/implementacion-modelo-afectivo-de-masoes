/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import ontology.masoes.MasoesOntology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MasoesMatchExpressionTest {

    private MasoesMatchExpression masoesMatchExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        masoesMatchExpression = new MasoesMatchExpression();
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(MasoesOntology.ONTOLOGY_NAME);
    }

    @Test
    public void shouldMatchWithMasoesRequestMessage() {
        assertTrue(masoesMatchExpression.match(message));
    }

}