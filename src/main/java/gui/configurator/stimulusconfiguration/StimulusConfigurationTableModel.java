/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulusconfiguration;

import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.util.List;

public class StimulusConfigurationTableModel extends AbstractTableModel {

    private static final int COLUMN_SELECTED = 0;
    private static final int COLUMN_STIMULUS = 1;
    private static final int COLUMN_ACTIVATION = 2;
    private static final int COLUMN_SATISFACTION = 3;
    private static final int COLUMN_CONDITION = 4;
    private final JTable table;
    private Translation translation = Translation.getInstance();

    private List<StimulusConfigurationModel> stimulusConfigurationModels;
    private String[] columns;

    public StimulusConfigurationTableModel(JTable table, List<StimulusConfigurationModel> stimulusConfigurationModels) {
        this.stimulusConfigurationModels = stimulusConfigurationModels;

        this.columns = new String[]{
                "",
                translation.get("gui.stimulus"),
                translation.get("gui.pa"),
                translation.get("gui.ps"),
                translation.get("gui.condition")
        };

        this.table = table;
        this.table.setModel(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(COLUMN_SELECTED).setMaxWidth(40);
    }

    @Override
    public int getRowCount() {
        return stimulusConfigurationModels.size();
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
        if (rowIndex >= stimulusConfigurationModels.size()) {
            return null;
        }

        StimulusConfigurationModel model = stimulusConfigurationModels.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_SELECTED:
                return model.isSelected();
            case COLUMN_STIMULUS:
                return model.getModel().getName();
            case COLUMN_ACTIVATION:
                return StringFormatter.toString(model.getActivation());
            case COLUMN_SATISFACTION:
                return StringFormatter.toString(model.getSatisfaction());
            case COLUMN_CONDITION:
                return model.isSelf() ? translation.get("gui.self") : translation.get("gui.others");
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case COLUMN_SELECTED:
                return Boolean.class;
            case COLUMN_STIMULUS:
            case COLUMN_ACTIVATION:
            case COLUMN_SATISFACTION:
            case COLUMN_CONDITION:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_SELECTED;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_SELECTED) {
            StimulusConfigurationModel model = stimulusConfigurationModels.get(rowIndex);
            model.setSelected((Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void selectElements() {
        stimulusConfigurationModels.forEach(model -> model.setSelected(true));
        fireTableDataChanged();
    }

    public void deselectElements() {
        stimulusConfigurationModels.forEach(model -> model.setSelected(false));
        fireTableDataChanged();
    }

    public StimulusConfigurationModel getSelectedElement() {
        if (hasSelected()) {
            return stimulusConfigurationModels.get(table.getSelectedRow());
        }
        return null;
    }

    public boolean hasSelected() {
        return table.getSelectedRows().length > 0;
    }

}
