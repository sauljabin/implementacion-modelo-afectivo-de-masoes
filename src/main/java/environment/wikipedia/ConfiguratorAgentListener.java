/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfiguratorAgentListener extends WindowAdapter implements ActionListener {

    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgent configuratorAgent;

    public ConfiguratorAgentListener(ConfiguratorAgent configuratorAgent, ConfiguratorAgentGui configuratorAgentGui) {
        this.configuratorAgentGui = configuratorAgentGui;
        this.configuratorAgent = configuratorAgent;
        setUpGui();
    }

    private void setUpGui() {
        configuratorAgentGui.addWindowListener(this);
        configuratorAgentGui.addActionListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(configuratorAgentGui, ConfiguratorAgentEvent.CLOSE_WINDOW.getInt());
        configuratorAgent.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiEvent guiEvent = new GuiEvent(configuratorAgentGui, ConfiguratorAgentEvent.valueOf(e.getActionCommand()).getInt());
        configuratorAgent.postGuiEvent(guiEvent);
    }

}
