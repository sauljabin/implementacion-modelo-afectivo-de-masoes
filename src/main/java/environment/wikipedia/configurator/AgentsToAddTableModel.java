/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import translate.Translation;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AgentsToAddTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT = 1;
    private static final int COLUMN_INITIAL_EMOTIONAL_STATE = 2;
    private static final int COLUMN_EMOTION = 3;
    private static final int COLUMN_RECEIVE_STIMULUS = 0;

    private List<AgentToAdd> agentsToAdd;
    private Translation translation;
    private String[] columns;

    public AgentsToAddTableModel() {
        agentsToAdd = new ArrayList<>();
        translation = Translation.getInstance();
        columns = new String[]{
                translation.get("gui.receive_stimulus"),
                translation.get("gui.agent"),
                translation.get("gui.initial_emotional_state"),
                translation.get("gui.emotion")
        };
    }

    public List<AgentToAdd> getAgentsToAdd() {
        return agentsToAdd;
    }

    public void setAgentsToAdd(List<AgentToAdd> agentsToAdd) {
        this.agentsToAdd = agentsToAdd;
        fireTableDataChanged();
    }

    public void addAgentToAdd(AgentToAdd agentToAdd) {
        this.agentsToAdd.add(agentToAdd);
        fireTableDataChanged();
    }

    public void removeAgentToAdd(AgentToAdd agentToAdd) {
        this.agentsToAdd.remove(agentToAdd);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return agentsToAdd.size();
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
        if (rowIndex >= agentsToAdd.size()) {
            return null;
        }
        AgentToAdd agentToAdd = agentsToAdd.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_RECEIVE_STIMULUS:
                return agentToAdd.isReceiveStimulus();
            case COLUMN_AGENT:
                return agentToAdd.getAgentName();
            case COLUMN_INITIAL_EMOTIONAL_STATE:
                return agentToAdd.getEmotionalStateString();
            case COLUMN_EMOTION:
                return agentToAdd.getEmotionName();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case COLUMN_RECEIVE_STIMULUS:
                return Boolean.class;
            case COLUMN_AGENT:
                return String.class;
            case COLUMN_INITIAL_EMOTIONAL_STATE:
                return String.class;
            case COLUMN_EMOTION:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_RECEIVE_STIMULUS ? true : false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_RECEIVE_STIMULUS) {
            AgentToAdd agentToAdd = agentsToAdd.get(rowIndex);
            agentToAdd.setReceiveStimulus((Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

}
