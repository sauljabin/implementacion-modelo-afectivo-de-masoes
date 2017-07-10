/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agenttypedefinition;

import gui.WindowsEventsAdapter;
import masoes.agent.EmotionalAgent;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class AgentTypeDefinitionGuiListener extends WindowsEventsAdapter {

    private AgentTypeDefinitionGui gui;
    private AgentTypeDefinitionModel model;
    private AgentTypeDefinitionGuiCallback callback;

    public AgentTypeDefinitionGuiListener(AgentTypeDefinitionGuiCallback callback) {
        this(null, callback);
    }

    public AgentTypeDefinitionGuiListener(AgentTypeDefinitionModel model, AgentTypeDefinitionGuiCallback callback) {
        this.model = model;
        this.callback = callback;
        gui = new AgentTypeDefinitionGui();
        configView();
        initView();
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new AgentTypeDefinitionGuiListener(model -> System.out.println(model));
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void close() {
        gui.dispose();
    }

    private void initView() {
        Object[] classes = getClasses();

        if (model == null) {
            model = new AgentTypeDefinitionModel();
            model.setAgentTypeName("");
            model.setAgentTypeClass(((EmotionalAgentClassWrapper) classes[0]).getAgentClass());
        }

        gui.getAgentTypesCombo().setModel(new DefaultComboBoxModel(classes));

        gui.getAgentTypeName().setText(model.getAgentTypeName());
        gui.getAgentTypesCombo().setSelectedItem(Arrays.stream(classes)
                .filter(o -> ((EmotionalAgentClassWrapper) o).getAgentClass().equals(model.getAgentTypeClass()))
                .findFirst()
                .get()
        );
    }

    private Object[] getClasses() {
        Reflections reflections = new Reflections("");
        return reflections.getSubTypesOf(EmotionalAgent.class)
                .stream()
                .map(aClass -> new EmotionalAgentClassWrapper(aClass))
                .sorted()
                .toArray();
    }

    private void configView() {
        gui.addWindowListener(this);
        gui.getSaveButton().setActionCommand(AgentTypeDefinitionGuiEvent.SAVE.toString());
        gui.getSaveButton().addActionListener(this);

        gui.getSaveAndNewButton().setActionCommand(AgentTypeDefinitionGuiEvent.SAVE_AND_NEW.toString());
        gui.getSaveAndNewButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(AgentTypeDefinitionGuiEvent.CANCEL.toString());
        gui.getCancelButton().addActionListener(this);

        if (model != null) {
            gui.getSaveAndNewButton().setVisible(false);
        }
    }

    private void eventHandler(AgentTypeDefinitionGuiEvent event) {
        switch (event) {
            case SAVE:
                save();
                break;
            case SAVE_AND_NEW:
                saveAndNew();
                break;
            case CANCEL:
                close();
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentTypeDefinitionGuiEvent.valueOf(e.getActionCommand()));
    }

    private void saveAndNew() {
        updateModel();
        model = null;
        initView();
    }

    private void updateModel() {
        EmotionalAgentClassWrapper emotionalAgentClassWrapper = (EmotionalAgentClassWrapper) gui.getAgentTypesCombo().getSelectedItem();
        model.setAgentTypeClass(emotionalAgentClassWrapper.getAgentClass());

        model.setAgentTypeName(gui.getAgentTypeName().getText());

        if (callback != null) {
            callback.afterSave(model);
        }
    }

    private void save() {
        updateModel();
        close();
    }

}
