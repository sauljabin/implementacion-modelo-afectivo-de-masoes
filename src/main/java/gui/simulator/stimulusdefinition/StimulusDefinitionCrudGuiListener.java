/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusdefinition;

import gui.WindowsEventsAdapter;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class StimulusDefinitionCrudGuiListener extends WindowsEventsAdapter {

    private StimulusDefinitionCrudGui gui;
    private List<StimulusDefinitionModel> elements;
    private StimulusDefinitionTableModel tableModel;

    public StimulusDefinitionCrudGuiListener(List<StimulusDefinitionModel> elements) {
        this.elements = elements;
        gui = new StimulusDefinitionCrudGui();
        configView();
        initView();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new StimulusDefinitionCrudGuiListener(new ArrayList<>());
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void close() {
        gui.dispose();
    }

    private void initView() {
        tableModel = new StimulusDefinitionTableModel(gui.getStimuliTable(), elements);
    }

    private void configView() {
        gui.addWindowListener(this);
        gui.getAddStimulusButton().setActionCommand(StimulusDefinitionCrudGuiEvent.ADD_STIMULUS.toString());
        gui.getAddStimulusButton().addActionListener(this);

        gui.getDeleteStimulusButton().setActionCommand(StimulusDefinitionCrudGuiEvent.DELETE_STIMULUS.toString());
        gui.getDeleteStimulusButton().addActionListener(this);

        gui.getEditStimulusButton().setActionCommand(StimulusDefinitionCrudGuiEvent.EDIT_STIMULUS.toString());
        gui.getEditStimulusButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(StimulusDefinitionCrudGuiEvent.CLOSE_WINDOW.toString());
        gui.getCancelButton().addActionListener(this);
    }

    private void eventHandler(StimulusDefinitionCrudGuiEvent event) {
        switch (event) {
            case CLOSE_WINDOW:
                close();
                break;
            case ADD_STIMULUS:
                new StimulusDefinitionGuiListener(model -> tableModel.add(model));
                break;
            case EDIT_STIMULUS:
                if (tableModel.hasSelected()) {
                    new StimulusDefinitionGuiListener(tableModel.getSelectedElement(), model -> tableModel.fireTableDataChanged());
                }
                break;
            case DELETE_STIMULUS:
                tableModel.deleteSelectedElements();
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        eventHandler(StimulusDefinitionCrudGuiEvent.valueOf(e.getActionCommand()));
    }

}
