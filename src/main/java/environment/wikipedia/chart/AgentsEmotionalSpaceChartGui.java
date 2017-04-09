/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;
import util.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class AgentsEmotionalSpaceChartGui extends JDialog {

    private  Translation translation;
    private  AgentsEmotionalSpaceChart emotionalSpaceChart;
    private Map<String, Color> colorsMap = new HashMap<>();
    private Map<String, EmotionalState> emotionMap = new HashMap<>();
    private  JPanel agentsNamePanel;

    public AgentsEmotionalSpaceChartGui(String title) {
        translation = Translation.getInstance();

        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                emotionalSpaceChart.stop();
                dispose();
            }
        });

        emotionalSpaceChart = new AgentsEmotionalSpaceChart(colorsMap, emotionMap);
        add(emotionalSpaceChart, BorderLayout.CENTER);

        agentsNamePanel = new JPanel(new MigLayout("insets 3"));

        JPanel west = new JPanel(new MigLayout("insets 5"));
        west.add(new JLabel(translation.get("gui.agents")), "w 150, wrap 10");
        add(west, BorderLayout.WEST);

        JScrollPane collaboratorNames = new JScrollPane(agentsNamePanel);

        west.add(collaboratorNames, "w 150");
    }

    public void start() {
        setVisible(true);
        emotionalSpaceChart.start();
    }

    public void stop() {
        emotionalSpaceChart.stop();
        dispose();
    }

    public void addAgent(String agent) {
        Color color = Colors.getColor(colorsMap.size());
        colorsMap.put(agent, color);
        JLabel agentName = new JLabel(agent);
        agentName.setForeground(color);
        agentsNamePanel.add(agentName, "wrap");
        revalidate();
        repaint();
    }

    public void addEmotion(String agent, EmotionalState emotionalState) {
        emotionMap.put(agent, emotionalState);
    }

}
