/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agenttypedefinition;

import environment.dummy.DummyEmotionalAgent;
import gui.WindowsEventsAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class AgentTypeDefinitionCrudGuiListener extends WindowsEventsAdapter {

    private AgentTypeDefinitionCrudGui gui;
    private List<AgentTypeDefinitionModel> elements;
    private AgentTypeDefinitionCrudGuiCallback callback;
    private AgentTypeDefinitionTableModel tableModel;

    public AgentTypeDefinitionCrudGuiListener(List<AgentTypeDefinitionModel> elements, AgentTypeDefinitionCrudGuiCallback callback) {
        this.elements = elements;
        this.callback = callback;
        gui = new AgentTypeDefinitionCrudGui();
        configView();
        initView();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        ArrayList<AgentTypeDefinitionModel> elements = new ArrayList<>();
        elements.add(new AgentTypeDefinitionModel(DummyEmotionalAgent.class, "Dummy"));
        new AgentTypeDefinitionCrudGuiListener(elements, models -> System.out.println(models));
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
        gui.getAddAgentButton().setActionCommand(AgentTypeDefinitionCrudGuiEvent.ADD_AGENT.toString());
        gui.getAddAgentButton().addActionListener(this);

        gui.getDeleteAgentButton().setActionCommand(AgentTypeDefinitionCrudGuiEvent.DELETE_AGENT.toString());
        gui.getDeleteAgentButton().addActionListener(this);

        gui.getEditAgentButton().setActionCommand(AgentTypeDefinitionCrudGuiEvent.EDIT_AGENT.toString());
        gui.getEditAgentButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(AgentTypeDefinitionCrudGuiEvent.CLOSE_WINDOW.toString());
        gui.getCancelButton().addActionListener(this);
    }

    private void eventHandler(AgentTypeDefinitionCrudGuiEvent event) {
        switch (event) {
            case CLOSE_WINDOW:
                close();
                break;
            case ADD_AGENT:
                new AgentTypeDefinitionGuiListener(model -> tableModel.add(model));
                break;
            case EDIT_AGENT:
                if (tableModel.hasSelected()) {
                    new AgentTypeDefinitionGuiListener(tableModel.getSelectedElement(), model -> tableModel.fireTableDataChanged());
                }
                break;
            case DELETE_AGENT:
                deleteAgentType();
                break;
        }
    }

    private void deleteAgentType() {
        try {
            if (callback != null) {
                callback.beforeDelete(tableModel.getSelectedElements());
            }
            tableModel.deleteSelectedElements();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentTypeDefinitionCrudGuiEvent.valueOf(e.getActionCommand()));
    }

}
