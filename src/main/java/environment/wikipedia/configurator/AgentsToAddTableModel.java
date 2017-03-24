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
    private static final int COLUMN_EMOTION = 1;

    private List<AgentsToAdd> agentsToAdds;
    private Translation translation;
    private String[] columns;

    public AgentsToAddTableModel() {
        agentsToAdds = new ArrayList<>();
        translation = Translation.getInstance();
        columns = new String[]{
                translation.get("gui.agent"),
                translation.get("gui.emotion")
        };
    }

    public void setAgentsToAdds(List<AgentsToAdd> agentsToAdds) {
        this.agentsToAdds = agentsToAdds;
        fireTableDataChanged();
    }

    public void addAgentsToAdds(AgentsToAdd agentsToAdd) {
        this.agentsToAdds.add(agentsToAdd);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return agentsToAdds.size();
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
        AgentsToAdd agentsToAdd = agentsToAdds.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_AGENT:
                return agentsToAdd.getName();
            case COLUMN_EMOTION:
                return agentsToAdd.getEmotion().toString();
            default:
                return null;
        }
    }

}
