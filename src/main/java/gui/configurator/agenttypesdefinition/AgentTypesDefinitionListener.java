/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agenttypesdefinition;

import environment.dummy.DummyEmotionalAgent;
import gui.WindowsEventsAdapter;
import gui.configurator.agenttypedefinition.AgentTypeDefinitionListener;
import gui.configurator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.configurator.agenttypedefinition.table.AgentTypeDefinitionTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class AgentTypesDefinitionListener extends WindowsEventsAdapter {

    private AgentTypesDefinitionGui gui;
    private List<AgentTypeDefinitionModel> elements;
    private AgentTypeDefinitionTableModel tableModel;

    public AgentTypesDefinitionListener(List<AgentTypeDefinitionModel> elements) {
        this.elements = elements;
        gui = new AgentTypesDefinitionGui();
        configView();
        initView();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        ArrayList<AgentTypeDefinitionModel> elements = new ArrayList<>();
        elements.add(new AgentTypeDefinitionModel(DummyEmotionalAgent.class, "Dummy"));
        new AgentTypesDefinitionListener(elements);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void close() {
        gui.dispose();
    }

    private void initView() {
        tableModel = new AgentTypeDefinitionTableModel(gui.getAgentTypesTable(), elements);
    }

    private void configView() {
        gui.addWindowListener(this);
        gui.getAddAgentButton().setActionCommand(AgentTypesDefinitionEvent.ADD_AGENT.toString());
        gui.getAddAgentButton().addActionListener(this);

        gui.getDeleteAgentButton().setActionCommand(AgentTypesDefinitionEvent.DELETE_AGENT.toString());
        gui.getDeleteAgentButton().addActionListener(this);

        gui.getEditAgentButton().setActionCommand(AgentTypesDefinitionEvent.EDIT_AGENT.toString());
        gui.getEditAgentButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(AgentTypesDefinitionEvent.CLOSE_WINDOW.toString());
        gui.getCancelButton().addActionListener(this);
    }

    private void eventHandler(AgentTypesDefinitionEvent event) {
        switch (event) {
            case CLOSE_WINDOW:
                close();
                break;
            case ADD_AGENT:
                new AgentTypeDefinitionListener(model -> tableModel.add(model));
                break;
            case EDIT_AGENT:
                if (tableModel.getSelectedElement() != null) {
                    new AgentTypeDefinitionListener(tableModel.getSelectedElement(), model -> tableModel.fireTableDataChanged());
                }
                break;
            case DELETE_AGENT:
                tableModel.deleteSelectedElements();
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentTypesDefinitionEvent.valueOf(e.getActionCommand()));
    }

}
