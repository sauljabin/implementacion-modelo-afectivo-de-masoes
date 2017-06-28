/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent.table;

import environment.wikipedia.configurator.agent.AgentModel;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AgentTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT = 0;
    private static final int COLUMN_EMOTION = 1;
    private static final int COLUMN_EMOTION_TYPE = 2;
    private static final int COLUMN_ACTIVATION = 3;
    private static final int COLUMN_SATISFACTION = 4;

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);

    private JTable table;
    private Translation translation = Translation.getInstance();
    private String[] columns;
    private List<AgentModel> agents;

    public AgentTableModel(JTable table) {
        this.table = table;

        this.agents = new ArrayList<>();

        columns = new String[]{
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S"
        };

        table.setModel(this);

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

    public List<AgentModel> getAgents() {
        return agents;
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

        AgentModel agentState = agents.get(rowIndex);
        Emotion emotion = getEmotion(agentState);

        switch (columnIndex) {
            case COLUMN_AGENT:
                return agentState.getName();
            case COLUMN_EMOTION:
                return translate(emotion.getName());
            case COLUMN_EMOTION_TYPE:
                return translate(emotion.getType().toString());
            case COLUMN_ACTIVATION:
                return StringFormatter.toString(agentState.getActivation());
            case COLUMN_SATISFACTION:
                return StringFormatter.toString(agentState.getSatisfaction());
            default:
                return null;
        }
    }

    private String translate(String string) {
        return translation.get(string.toLowerCase());
    }

    private Emotion getEmotion(AgentModel agentState) {
        EmotionalState emotionalState = new EmotionalState(agentState.getActivation(), agentState.getSatisfaction());
        return AffectiveModel.getInstance().searchEmotion(emotionalState);
    }

    public void addAgent(AgentModel agent) {
        this.agents.add(agent);
        fireTableDataChanged();
    }

    public void deleteSelectedAgent() {
        Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> agents.get(i))
                .collect(Collectors.toList())
                .forEach(agentModel -> agents.remove(agentModel));
        fireTableDataChanged();
    }

    public AgentModel getSelectedAgent() {
        return agents.get(table.getSelectedRow());
    }

    public boolean hasSelectedAgent() {
        return table.getSelectedRows().length > 0;
    }

}
