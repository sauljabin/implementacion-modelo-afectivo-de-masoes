/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.TreeMap;

public class AgentsEmotionalStateChartGui extends JFrame {

    private static final int COLUMN_SIZE = 4;
    private static final int CHART_W = 350;
    private static final int CHART_H = 280;
    private Translation translation = Translation.getInstance();
    private JPanel centerPanel;
    private Map<String, ChartContainer> agents = new TreeMap<>();

    public AgentsEmotionalStateChartGui(String title) {
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
            }
        });

        centerPanel = new JPanel(new MigLayout());
        add(centerPanel);
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

    public void addAgent(String agentName) {
        if (agents.containsKey(agentName)) {
            return;
        }

        ChartContainer chartContainer = new ChartContainer(agentName);
        agents.put(agentName, chartContainer);

        String layout = "w " + CHART_W + ", h " + CHART_H;

        if (agents.size() % COLUMN_SIZE == 0) {
            layout += ", wrap";
        }

        centerPanel.add(chartContainer.getChartPanel(), layout);

        calculateNewWindowSize();
        revalidate();
        repaint();
    }

    private void calculateNewWindowSize() {
        int h = ((int) Math.ceil((double) agents.size() / (double) COLUMN_SIZE)) * CHART_H;
        int w = 0;
        if (agents.size() >= COLUMN_SIZE) {
            w = COLUMN_SIZE * CHART_W;
        } else {
            w = agents.size() * CHART_W;
        }
        int PADDING = 10;
        setSize(w + PADDING, h + PADDING);
    }

    public void addEmotionalState(String agentName, int iteration, EmotionalState emotionalState) {
        ChartContainer chartContainer = agents.get(agentName);
        chartContainer.addEmotionalState(iteration, emotionalState);
    }

    private class ChartContainer {

        private JFreeChart chart;
        private ChartPanel chartPanel;
        private XYSeriesCollection collection;
        private XYSeries seriesSatisfaction;
        private XYSeries seriesActivation;
        private String agentName;

        public ChartContainer(String agentName) {
            this.agentName = agentName;
            seriesActivation = new XYSeries(translation.get("gui.activation"));
            seriesSatisfaction = new XYSeries(translation.get("gui.satisfaction"));

            collection = new XYSeriesCollection();
            collection.addSeries(seriesActivation);
            collection.addSeries(seriesSatisfaction);

            chart = ChartFactory.createXYLineChart(agentName, translation.get("gui.iteration"), "", collection, PlotOrientation.VERTICAL, true, true, false);
            XYPlot xyPlot = chart.getXYPlot();
            ValueAxis rangeAxis = xyPlot.getRangeAxis();
            rangeAxis.setRange(-1., 1.);

            chartPanel = new ChartPanel(chart);
        }

        public ChartPanel getChartPanel() {
            return chartPanel;
        }

        public void addEmotionalState(int iteration, EmotionalState emotionalState) {
            seriesSatisfaction.add(iteration, emotionalState.getSatisfaction());
            seriesActivation.add(iteration, emotionalState.getActivation());
        }

    }

}
