/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.ontology.state.AgentState;
import translate.Translation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AgentsStateTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT = 0;
    private static final int COLUMN_EMOTION = 1;
    private static final int COLUMN_EMOTION_TYPE = 2;
    private static final int COLUMN_ACTIVATION = 3;
    private static final int COLUMN_SATISFACTION = 4;
    private static final int COLUMN_BEHAVIOUR_TYPE = 5;

    private List<AgentState> agentStates;
    private String[] columns;
    private Translation translation;
    private JTable table;

    public AgentsStateTableModel() {
        translation = Translation.getInstance();
        agentStates = new ArrayList<>();
        columns = new String[]{
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                translation.get("gui.activation_x"),
                translation.get("gui.satisfaction_y"),
                translation.get("gui.behaviour")
        };
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return agentStates.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AgentState agentState = agentStates.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_AGENT:
                return agentState.getAgent().getLocalName();
            case COLUMN_EMOTION:
                return translation.get(agentState.getEmotionState().getName().toLowerCase());
            case COLUMN_EMOTION_TYPE:
                return translation.get(agentState.getEmotionState().getType().toLowerCase());
            case COLUMN_ACTIVATION:
                return String.format("%.2f", agentState.getEmotionState().getActivation());
            case COLUMN_SATISFACTION:
                return String.format("%.2f", agentState.getEmotionState().getSatisfaction());
            case COLUMN_BEHAVIOUR_TYPE:
                return translation.get(agentState.getBehaviourState().getType().toLowerCase());
            default:
                return null;
        }
    }

    public List<AgentState> getAgentStates() {
        return agentStates;
    }

    public void setAgentStates(List<AgentState> agentStates) {
        this.agentStates = agentStates;

        int selectedRow = -1;

        if (table != null) {
            selectedRow = table.getSelectedRow();
        }

        fireTableDataChanged();

        if (selectedRow >= 0) {
            table.addRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    public void setTable(JTable table) {
        this.table = table;
    }

}
