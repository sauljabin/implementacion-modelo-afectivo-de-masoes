/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.agentstate;

import jade.gui.GuiEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AgentStateGuiListener extends WindowAdapter {

    private AgentStateGui agentStateGui;
    private AgentStateGuiAgent agentStateGuiAgent;

    public AgentStateGuiListener(AgentStateGuiAgent agentStateGuiAgent, AgentStateGui agentStateGui) {
        this.agentStateGui = agentStateGui;
        this.agentStateGuiAgent = agentStateGuiAgent;
        setUpGui();
    }

    private void setUpGui() {
        agentStateGui.addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(agentStateGui, AgentStateGuiEvent.CLOSE_WINDOW.getInt());
        agentStateGuiAgent.postGuiEvent(guiEvent);
    }

}
