/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agent.table;

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
    private List<AgentState> agents;

    public AgentStateTableModel(JTable table) {
        this.table = table;

        this.agents = new ArrayList<>();

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
        return agents.size();
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
        if (rowIndex >= agents.size()) {
            return null;
        }

        AgentState agentState = agents.get(rowIndex);

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

    public void addAgent(AgentState newAgent) {
        int selectedRow = table.getSelectedRow();

        Optional<AgentState> agentStateOptional = agents.stream()
                .filter(agentState -> agentState.getAgent().equals(newAgent.getAgent()))
                .findFirst();

        if (agentStateOptional.isPresent()) {
            int index = agents.indexOf(agentStateOptional.get());
            agents.set(index, newAgent);
            fireTableRowsUpdated(index, index);
        } else {
            agents.add(newAgent);
            fireTableDataChanged();
        }

        if (selectedRow >= 0) {
            table.addRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    public AgentState getSelectedAgent() {
        return agents.get(table.getSelectedRow());
    }

    public boolean hasSelectedAgent() {
        return table.getSelectedRows().length > 0;
    }

    public void clear() {
        agents.clear();
        fireTableDataChanged();
    }

    public List<AgentState> getAgents() {
        return agents;
    }

}
