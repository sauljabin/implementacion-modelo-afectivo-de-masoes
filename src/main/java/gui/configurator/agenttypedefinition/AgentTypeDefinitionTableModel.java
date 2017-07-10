/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agenttypedefinition;

import translate.Translation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AgentTypeDefinitionTableModel extends AbstractTableModel {

    private static final int COLUMN_AGENT_TYPE = 0;
    private static final int COLUMN_NAME = 1;
    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);
    private final String[] columns;

    private Translation translation = Translation.getInstance();

    private JTable table;
    private List<AgentTypeDefinitionModel> elements;

    public AgentTypeDefinitionTableModel(JTable table, List<AgentTypeDefinitionModel> elements) {
        this.table = table;
        this.elements = elements;
        columns = new String[]{
                translation.get("gui.agent_type"),
                translation.get("gui.name")
        };
        table.setModel(this);

        setTableFontSize(table);
        setColumnSize(table, COLUMN_AGENT_TYPE, 300);
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

        AgentTypeDefinitionModel model = elements.get(rowIndex);

        switch (columnIndex) {
            case COLUMN_AGENT_TYPE:
                return model.getAgentTypeClass().getName();
            case COLUMN_NAME:
                return model.getAgentTypeName();
            default:
                return null;
        }
    }

    public void add(AgentTypeDefinitionModel model) {
        elements.add(model);
        fireTableDataChanged();
    }

    public void deleteSelectedElements() {
        getSelectedElements()
                .forEach(model -> elements.remove(model));
        fireTableDataChanged();
    }

    public AgentTypeDefinitionModel getSelectedElement() {
        if (hasSelectedStimulus()) {
            return elements.get(table.getSelectedRow());
        }
        return null;
    }

    public boolean hasSelectedStimulus() {
        return table.getSelectedRows().length > 0;
    }

    public List<AgentTypeDefinitionModel> getSelectedElements() {
        return Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> elements.get(i))
                .collect(Collectors.toList());
    }

}
