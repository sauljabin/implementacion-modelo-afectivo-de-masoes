/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agentstate;

import masoes.ontology.state.AgentState;
import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgentStateTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT = 0;
    private static final int COLUMN_EMOTION = 1;
    private static final int COLUMN_EMOTION_TYPE = 2;
    private static final int COLUMN_ACTIVATION = 3;
    private static final int COLUMN_SATISFACTION = 4;
    private static final int COLUMN_BEHAVIOUR_TYPE = 5;

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);

    private JTable table;
    private Translation translation = Translation.getInstance();
    private String[] columns;
    private List<AgentState> elements;

    public AgentStateTableModel(JTable table) {
        this.table = table;

        this.elements = new ArrayList<>();

        columns = new String[]{
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour")
        };

        table.setModel(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setTableFontSize(table);
        setColumnSize(table, COLUMN_ACTIVATION, 50);
        setColumnSize(table, COLUMN_SATISFACTION, 50);
    }

    private void setTableFontSize(JTable table) {
        table.getTableHeader().setFont(FONT_9);
        table.setFont(FONT_9);
    }

    private void setColumnSize(JTable table, int column, int size) {
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setMaxWidth(size);
        columnModel.getColumn(column).setMinWidth(size);
    }

    @Override
    public int getRowCount() {
        return elements.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= elements.size()) {
            return null;
        }

        AgentState agentState = elements.get(rowIndex);

        switch (columnIndex) {
            case COLUMN_AGENT:
                return agentState.getAgent().getLocalName();
            case COLUMN_EMOTION:
                return translate(agentState.getEmotionState().getName());
            case COLUMN_EMOTION_TYPE:
                return translate(agentState.getEmotionState().getType());
            case COLUMN_ACTIVATION:
                return StringFormatter.toString(agentState.getEmotionState().getActivation());
            case COLUMN_SATISFACTION:
                return StringFormatter.toString(agentState.getEmotionState().getSatisfaction());
            case COLUMN_BEHAVIOUR_TYPE:
                return translate(agentState.getBehaviourState().getType());
            default:
                return null;
        }
    }

    private String translate(String string) {
        return translation.get(string.toLowerCase());
    }

    public void add(AgentState newState) {
        int selectedRow = table.getSelectedRow();

        Optional<AgentState> agentStateOptional = elements.stream()
                .filter(agentState -> agentState.getAgent().equals(newState.getAgent()))
                .findFirst();

        if (agentStateOptional.isPresent()) {
            int index = elements.indexOf(agentStateOptional.get());
            elements.set(index, newState);
            fireTableRowsUpdated(index, index);
        } else {
            elements.add(newState);
            fireTableDataChanged();
        }

        if (selectedRow >= 0) {
            table.addRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    public AgentState getSelectedElement() {
        if (hasSelected()) {
            return elements.get(table.getSelectedRow());
        }
        return null;
    }

    public boolean hasSelected() {
        return table.getSelectedRows().length > 0;
    }

    public void clear() {
        elements.clear();
        fireTableDataChanged();
    }

}
