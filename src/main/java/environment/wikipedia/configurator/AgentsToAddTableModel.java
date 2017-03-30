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

    private static final int COLUMN_AGENT = 0;
    private static final int COLUMN_INITIAL_EMOTIONAL_STATE = 1;
    private static final int COLUMN_EMOTION = 2;

    private List<AgentToAdd> agentsToAdd;
    private Translation translation;
    private String[] columns;

    public AgentsToAddTableModel() {
        agentsToAdd = new ArrayList<>();
        translation = Translation.getInstance();
        columns = new String[]{
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

}
