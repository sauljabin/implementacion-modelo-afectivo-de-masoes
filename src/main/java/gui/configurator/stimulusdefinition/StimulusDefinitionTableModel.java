/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulusdefinition;

import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StimulusDefinitionTableModel extends AbstractTableModel {

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);

    private static final int COLUMN_STIMULUS = 0;
    private static final int COLUMN_VALUE = 1;
    private static final int COLUMN_ACTIVATION = 2;
    private static final int COLUMN_SATISFACTION = 3;
    private static final int COLUMN_CONDITION = 4;

    private List<StimulusDefinitionModel> elements;

    private Translation translation = Translation.getInstance();
    private String[] columns;
    private JTable table;

    public StimulusDefinitionTableModel(JTable table, List<StimulusDefinitionModel> elements) {
        this.table = table;
        this.elements = elements;

        columns = new String[]{
                translation.get("gui.stimulus"),
                translation.get("gui.value"),
                translation.get("gui.pa"),
                translation.get("gui.ps"),
                translation.get("gui.condition")
        };

        table.setModel(this);

        setTableFontSize(table);
        setColumnSize(table, COLUMN_ACTIVATION, 50);
        setColumnSize(table, COLUMN_SATISFACTION, 50);
        setColumnSize(table, COLUMN_CONDITION, 100);
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

        StimulusDefinitionModel stimulusDefinitionModel = elements.get(rowIndex);

        switch (columnIndex) {
            case COLUMN_STIMULUS:
                return stimulusDefinitionModel.getName();
            case COLUMN_VALUE:
                return stimulusDefinitionModel.getValue();
            case COLUMN_ACTIVATION:
                return StringFormatter.toString(stimulusDefinitionModel.getActivation());
            case COLUMN_SATISFACTION:
                return StringFormatter.toString(stimulusDefinitionModel.getSatisfaction());
            case COLUMN_CONDITION:
                return stimulusDefinitionModel.isSelf() ? translation.get("gui.self") : translation.get("gui.others");
            default:
                return null;
        }
    }

    public void add(StimulusDefinitionModel model) {
        elements.add(model);
        fireTableDataChanged();
    }

    public List<StimulusDefinitionModel> getElements() {
        return elements;
    }

    public void deleteSelectedElements() {
        getSelectedElements()
                .forEach(stimulusModel -> elements.remove(stimulusModel));
        fireTableDataChanged();
    }

    public StimulusDefinitionModel getSelectedElement() {
        if (hasSelectedStimulus()) {
            return elements.get(table.getSelectedRow());
        }
        return null;
    }

    public boolean hasSelectedStimulus() {
        return table.getSelectedRows().length > 0;
    }

    public List<StimulusDefinitionModel> getSelectedElements() {
        return Arrays.stream(table.getSelectedRows())
                .mapToObj(i -> elements.get(i))
                .collect(Collectors.toList());
    }

}
