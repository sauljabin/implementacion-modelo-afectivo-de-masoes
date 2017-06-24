/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionalState;

import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AgentsEmotionalStateChartGui extends JFrame {

    private static final int COLUMN_SIZE = 4;
    private static final int CHART_W = 350;
    private static final int CHART_H = 280;
    private static final int MAX_AGENTS_TO_RESIZE_WINDOW = 12;
    private static final int PADDING = 20;
    private JPanel centerPanel;
    private List<ChartContainer> chartContainers = new LinkedList<>();

    public AgentsEmotionalStateChartGui(String title) {
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new AgentsEmotionalStateChartGuiWindowListener(this));

        centerPanel = new JPanel(new MigLayout());

        JScrollPane scrollPane = new JScrollPane(centerPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

    public Optional<ChartContainer> getChartContainer(String agent) {
        return chartContainers.stream()
                .filter(chartContainer -> chartContainer.sameName(agent))
                .findFirst();
    }

    public void addAgent(String agentName) {
        if (getChartContainer(agentName).isPresent()) {
            return;
        }

        ChartContainer chartContainer = new ChartContainer(getTitle(), agentName);

        chartContainers.add(chartContainer);


        String layout = "w " + CHART_W + ", h " + CHART_H;

        if (chartContainers.size() % COLUMN_SIZE == 0) {
            layout += ", wrap";
        }

        centerPanel.add(chartContainer.getChartPanel(), layout);

        calculateNewWindowSize();
        revalidate();
        repaint();
    }

    private void calculateNewWindowSize() {
        if (chartContainers.size() >= MAX_AGENTS_TO_RESIZE_WINDOW) {
            return;
        }

        int COLUMNS = ((int) Math.ceil((double) chartContainers.size() / (double) COLUMN_SIZE));
        int h = COLUMNS * CHART_H + (COLUMNS + 1) * 8;
        int w;
        if (chartContainers.size() >= COLUMN_SIZE) {
            w = COLUMN_SIZE * CHART_W + (COLUMN_SIZE + 1) * 7;
        } else {
            w = chartContainers.size() * CHART_W + (chartContainers.size() + 1) * 7;
        }
        setSize(w + PADDING, h);
    }

    public void addEmotionalState(String agentName, int iteration, EmotionalState emotionalState) {
        Optional<ChartContainer> chartContainer = getChartContainer(agentName);
        if (!chartContainer.isPresent()) {
            return;
        }
        chartContainer.get().addEmotionalState(iteration, emotionalState);
    }

    public void exportImage(File folder, int width, int height) throws IOException {
        chartContainers.forEach(chartContainer -> {
            String extension = "png";
            File file = new File(folder, String.format("%s %s.%s", getTitle(), chartContainer.getAgentName(), extension));
            try {
                ImageIO.write(chartContainer.getChart().createBufferedImage(width, height), extension, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}