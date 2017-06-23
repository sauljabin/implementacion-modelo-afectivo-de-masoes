/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.stimulus.table;

import environment.wikipedia.configurator.stimulus.StimulusModel;
import translate.Translation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SelectableStimulusTableModel extends AbstractTableModel {

    private static final int COLUMN_SELECTED = 0;
    private static final int COLUMN_STIMULUS = 1;
    private final JTable table;
    private final List<StimulusModel> stimulusModels;

    private Translation translation = Translation.getInstance();

    private List<SelectableStimulusModel> selectableStimulusModels;
    private String[] columns;

    public SelectableStimulusTableModel(JTable table, List<StimulusModel> stimulusModels) {
        this.table = table;
        this.stimulusModels = stimulusModels;
        this.selectableStimulusModels = createSelectableStimuli();

        this.columns = new String[]{
                "",
                translation.get("gui.stimulus")
        };

        this.table.setModel(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(COLUMN_SELECTED).setMaxWidth(40);
    }

    private List<SelectableStimulusModel> createSelectableStimuli() {
        return stimulusModels.stream()
                .map(stimulusModel -> new SelectableStimulusModel(stimulusModel))
                .collect(Collectors.toList());
    }

    @Override
    public int getRowCount() {
        return selectableStimulusModels.size();
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
        if (rowIndex >= selectableStimulusModels.size()) {
            return null;
        }

        SelectableStimulusModel model = selectableStimulusModels.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_SELECTED:
                return model.isSelected();
            case COLUMN_STIMULUS:
                return model.getModel().getName();
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
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_SELECTED ? true : false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_SELECTED) {
            SelectableStimulusModel model = selectableStimulusModels.get(rowIndex);
            model.setSelected((Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void selectAllEvents() {
        selectableStimulusModels.forEach(model -> model.setSelected(true));
        fireTableDataChanged();
    }

    public void deselectAllEvents() {
        selectableStimulusModels.forEach(model -> model.setSelected(false));
        fireTableDataChanged();
    }

}
