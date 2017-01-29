/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agent;

import gui.view.RequesterGui;
import gui.view.RequesterGuiEvent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class RequesterGuiAgent extends GuiAgent {

    private RequesterGui requesterGui;

    public RequesterGuiAgent() {
        requesterGui = new RequesterGui(this);
    }

    @Override
    protected void setup() {
        requesterGui.setUp();
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
            default:
                break;
        }
    }

}
