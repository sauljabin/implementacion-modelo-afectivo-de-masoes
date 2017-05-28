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

public class KnowledgeRulesTableModel extends AbstractTableModel {

    private static final int COLUMN_SELECTED = 0;
    private static final int COLUMN_RULE = 1;
    private static final int COLUMN_ACTIVATION = 2;
    private static final int COLUMN_SATISFACTION = 3;

    private List<KnowledgeRule> knowledgeRules;
    private Translation translation;
    private String[] columns;

    public KnowledgeRulesTableModel() {
        knowledgeRules = new ArrayList<>();
        translation = Translation.getInstance();
        columns = new String[]{
                "",
                translation.get("gui.event"),
                translation.get("gui.activation"),
                translation.get("gui.satisfaction")
        };
    }

    public List<KnowledgeRule> getKnowledgeRules() {
        return knowledgeRules;
    }

    public void setKnowledgeRules(List<KnowledgeRule> knowledgeRules) {
        this.knowledgeRules = knowledgeRules;
        fireTableDataChanged();
    }

    public void addKnowledgeRule(KnowledgeRule knowledgeRule) {
        this.knowledgeRules.add(knowledgeRule);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return knowledgeRules.size();
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
        if (rowIndex >= knowledgeRules.size()) {
            return null;
        }
        KnowledgeRule knowledgeRule = knowledgeRules.get(rowIndex);
        switch (columnIndex) {
            case COLUMN_SELECTED:
                return knowledgeRule.isSelected();
            case COLUMN_RULE:
                return translate(knowledgeRule.getRule());
            case COLUMN_ACTIVATION:
                return translate(knowledgeRule.getActivation());
            case COLUMN_SATISFACTION:
                return translate(knowledgeRule.getSatisfaction());
            default:
                return null;
        }
    }

    public String translate(String key) {
        return translation.get(key.toLowerCase().trim());
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case COLUMN_SELECTED:
                return Boolean.class;
            case COLUMN_RULE:
                return String.class;
            case COLUMN_ACTIVATION:
                return String.class;
            case COLUMN_SATISFACTION:
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
            KnowledgeRule knowledgeRule = knowledgeRules.get(rowIndex);
            knowledgeRule.setSelected((Boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void selectAllEvents() {
        knowledgeRules.forEach(knowledgeRule -> knowledgeRule.setSelected(true));
        fireTableDataChanged();
    }

    public void deselectAllEvents() {
        knowledgeRules.forEach(knowledgeRule -> knowledgeRule.setSelected(false));
        fireTableDataChanged();
    }

}
