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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StimulusTableModel extends AbstractTableModel {

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);

    private static final int COLUMN_STIMULUS = 0;
    private static final int COLUMN_VALUE = 1;
    private static final int COLUMN_ACTIVATION = 2;
    private static final int COLUMN_SATISFACTION = 3;
    private static final int COLUMN_CONDITION = 4;

    private List<StimulusModel> stimuli;

    private Translation translation = Translation.getInstance();
    private String[] columns;
    private JTable table;

    public StimulusTableModel(JTable table) {
        this.table = table;
        this.stimuli = new ArrayList<>();

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
        return stimuli.size();
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
        if (rowIndex >= stimuli.size()) {
            return null;
        }

        StimulusModel stimulusModel = stimuli.get(rowIndex);

        switch (columnIndex) {
            case COLUMN_STIMULUS:
                return stimulusModel.getName();
            case COLUMN_VALUE:
                return stimulusModel.getValue();
            case COLUMN_ACTIVATION:
                return stimulusModel.getActivation();
            case COLUMN_SATISFACTION:
                return stimulusModel.getSatisfaction();
            case COLUMN_CONDITION:
                return stimulusModel.isSelf() ? translation.get("gui.self") : translation.get("gui.others");
            default:
                return null;
        }
    }

    public void addStimulus(StimulusModel stimulus) {
        this.stimuli.add(stimulus);
        fireTableDataChanged();
    }

    public List<StimulusModel> getStimuli() {
        return stimuli;
    }

    public void deleteSelectedStimuli() {
        Arrays.stream(table.getSelectedRows())
                .forEach(i -> stimuli.remove(i));
        fireTableDataChanged();
    }

    public StimulusModel getSelectedStimulus() {
        return stimuli.get(table.getSelectedRow());
    }

    public boolean hasSelectedStimulus() {
        return table.getSelectedRows().length > 0;
    }

}
