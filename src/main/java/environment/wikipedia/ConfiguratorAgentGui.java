/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ConfiguratorAgentGui extends JFrame {

    private Translation translation;

    public ConfiguratorAgentGui() {
        translation = Translation.getInstance();
        setUp();
    }

    public static void main(String[] args) {
        ConfiguratorAgentGui configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentGui.showGui();
    }

    private void setUp() {
        setTitle(translation.get("gui.configurator"));
        setSize(900, 600);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponents();
        setLocationRelativeTo(this);
    }

    private void addComponents() {
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void showGui() {
        setVisible(true);
    }

    public void addActionListener(ActionListener actionListener) {
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
