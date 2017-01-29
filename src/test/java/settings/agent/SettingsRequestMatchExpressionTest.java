/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.agent;

import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.junit.Before;
import org.junit.Test;
import settings.ontology.SettingsOntology;

import static org.junit.Assert.assertTrue;

public class SettingsRequestMatchExpressionTest {

    private SettingsRequestMatchExpression settingsRequestMatchExpression;
    private ACLMessage message;

    @Before
    public void setUp() {
        settingsRequestMatchExpression = new SettingsRequestMatchExpression();
        message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setOntology(SettingsOntology.ONTOLOGY_NAME);
    }

    @Test
    public void shouldMatchWithMasoesMessage() {
        assertTrue(settingsRequestMatchExpression.match(message));
    }

}