/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import gui.WindowsEventsAdapter;
import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class ConfiguratorViewListener extends WindowsEventsAdapter {

    private ConfiguratorViewController controller;

    public ConfiguratorViewListener(ConfiguratorView view, ConfiguratorViewController controller) {
        this.controller = controller;

        view.addWindowListener(this);

        view.getAddStimulusButton().setActionCommand(ConfiguratorViewEvent.ADD_STIMULUS.toString());
        view.getAddStimulusButton().addActionListener(this);

        view.getDeleteStimulusButton().setActionCommand(ConfiguratorViewEvent.DELETE_STIMULUS.toString());
        view.getDeleteStimulusButton().addActionListener(this);

        view.getEditStimulusButton().setActionCommand(ConfiguratorViewEvent.EDIT_STIMULUS.toString());
        view.getEditStimulusButton().addActionListener(this);

        view.getAddAgentButton().setActionCommand(ConfiguratorViewEvent.ADD_AGENT.toString());
        view.getAddAgentButton().addActionListener(this);

        view.getDeleteAgentButton().setActionCommand(ConfiguratorViewEvent.DELETE_AGENT.toString());
        view.getDeleteAgentButton().addActionListener(this);

        view.getEditAgentButton().setActionCommand(ConfiguratorViewEvent.EDIT_AGENT.toString());
        view.getEditAgentButton().addActionListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, ConfiguratorViewEvent.CLOSE_WINDOW.getInt());
        controller.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, ConfiguratorViewEvent.valueOf(e.getActionCommand()).getInt());
        controller.postGuiEvent(guiEvent);
    }

}
