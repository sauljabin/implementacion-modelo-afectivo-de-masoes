/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agentconfiguration;

import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AgentConfigurationTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT = 0;
    private static final int COLUMN_EMOTION = 1;
    private static final int COLUMN_EMOTION_TYPE = 2;
    private static final int COLUMN_ACTIVATION = 3;
    private static final int COLUMN_SATISFACTION = 4;

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);

    private JTable table;
    private Translation translation = Translation.getInstance();
    private String[] columns;
    private List<AgentConfigurationModel> elements;

    public AgentConfigurationTableModel(JTable table, List<AgentConfigurationModel> elements) {
        this.table = table;

        this.elements = elements;

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

        AgentConfigurationModel agentState = elements.get(rowIndex);
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

    private Emotion getEmotion(AgentConfigurationModel agentState) {
        EmotionalState emotionalState = new EmotionalState(agentState.getActivation(), agentState.getSatisfaction());
        return AffectiveModel.getInstance().searchEmotion(emotionalState);
    }

    public void add(AgentConfigurationModel agent) {
        this.elements.add(agent);
        fireTableDataChanged();
    }

    public void deleteSelectedElements() {
        getSelectedElements()
                .forEach(agentConfigurationModel -> elements.remove(agentConfigurationModel));
        fireTableDataChanged();
    }

    private List<AgentConfigurationModel> getSelectedElements() {
        return Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> elements.get(i))
                .collect(Collectors.toList());
    }

    public AgentConfigurationModel getSelectedElement() {
        if (hasSelected()) {
            return elements.get(table.getSelectedRow());
        }
        return null;
    }

    public boolean hasSelected() {
        return table.getSelectedRows().length > 0;
    }

}
