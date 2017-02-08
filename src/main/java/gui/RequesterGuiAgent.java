/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.settings.GetAllSettings;
import ontology.settings.SettingsOntology;

public class RequesterGuiAgent extends GuiAgent {

    private RequesterGui requesterGui;
    private RequesterGuiListener requesterGuiListener;

    public RequesterGuiAgent() {
        requesterGui = new RequesterGui();
        requesterGuiListener = new RequesterGuiListener(requesterGui, this);
    }

    @Override
    protected void setup() {
        requesterGui.setSenderAgentName(getLocalName());
        requesterGui.showGui();
    }

    @Override
    protected void takeDown() {
        requesterGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        switch (RequesterGuiEvent.fromInt(guiEvent.getType())) {
            case CLOSE_WINDOW:
                doDelete();
                break;
            case SEND_MESSAGE:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        AID receiver = getAID(requesterGui.getReceiverAgentName());
        GetAllSettings agentAction = new GetAllSettings();
        SettingsOntology ontology = SettingsOntology.getInstance();

        OntologyAssistant ontologyAssistant = new OntologyAssistant(this, ontology);
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, agentAction);

        requesterGui.logMessage(requestAction);
        send(requestAction);
    }

}
