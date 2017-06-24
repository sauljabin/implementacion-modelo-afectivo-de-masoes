/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import environment.wikipedia.configurator.stimulus.StimulusModel;
import environment.wikipedia.configurator.stimulus.StimulusViewController;
import environment.wikipedia.configurator.stimulus.table.StimulusTableModel;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import util.StringFormatter;

import javax.swing.*;

public class ConfiguratorViewController extends GuiAgent {

    private ConfiguratorViewListener configuratorViewListener;
    private ConfiguratorView configuratorView;
    private StimulusTableModel stimulusTableModel;
    private AgentLogger logger;

    public ConfiguratorViewController() {
        logger = new AgentLogger(this);
        configuratorView = new ConfiguratorView();
        configuratorViewListener = new ConfiguratorViewListener(configuratorView, this);
        configView();
        configuratorView.setVisible(true);
    }

    private void configView() {
        stimulusTableModel = new StimulusTableModel(configuratorView.getStimuliTable());
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación alta", .3, .3));
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación media", 0, .1));
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación baja", -.05, .05));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación alta", -.3, -.3));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación media", 0, -.1));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación baja", -.05, -.05));
    }

    private StimulusModel createStimulus(String name, double activation, double satisfaction) {
        return new StimulusModel(name, StringFormatter.toCamelCase(name), activation, satisfaction, true);
    }

    @Override
    protected void takeDown() {
        configuratorView.dispose();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorViewEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
                case ADD_STIMULUS:
                    new StimulusViewController(
                            newStimulus -> stimulusTableModel.addStimulus(newStimulus)
                    );
                    break;
                case DELETE_STIMULUS:
                    stimulusTableModel.deleteSelectedStimuli();
                    break;
                case EDIT_STIMULUS:
                    if (stimulusTableModel.hasSelectedStimulus()) {
                        new StimulusViewController(
                                stimulusTableModel.getSelectedStimulus(),
                                updatedStimulus -> stimulusTableModel.fireTableDataChanged()
                        );
                    }
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            showError(e.getMessage());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(configuratorView, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
