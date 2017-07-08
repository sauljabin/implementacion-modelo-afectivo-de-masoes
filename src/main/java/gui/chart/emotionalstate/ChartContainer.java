/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.chart.emotionalstate;

import masoes.component.behavioural.EmotionalState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import translate.Translation;

import java.awt.*;

public class ChartContainer {

    private JFreeChart chart;
    private ChartPanel chartPanel;
    private XYSeriesCollection collection;
    private XYSeries seriesSatisfaction;
    private XYSeries seriesActivation;
    private String agentName;
    private Translation translation = Translation.getInstance();

    public ChartContainer(String title, String agentName) {
        this.agentName = agentName;
        seriesActivation = new XYSeries(translation.get("gui.activation"));
        seriesSatisfaction = new XYSeries(translation.get("gui.satisfaction"));

        collection = new XYSeriesCollection();
        collection.addSeries(seriesActivation);
        collection.addSeries(seriesSatisfaction);

        chart = ChartFactory.createXYLineChart(title + " " + agentName, translation.get("gui.iteration"), translation.get("gui.emotional_range"), collection, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(-1., 1.);

        chartPanel = new ChartPanel(chart);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        xyPlot.setRenderer(renderer);
        xyPlot.setBackgroundPaint(Color.WHITE);
        xyPlot.setDomainGridlinePaint(Color.BLACK);
        xyPlot.setRangeGridlinePaint(Color.BLACK);

        xyPlot.setRangeGridlinesVisible(true);
        xyPlot.setDomainGridlinesVisible(true);

        chart.getLegend().setFrame(BlockBorder.NONE);
    }

    public String getAgentName() {
        return agentName;
    }

    public boolean sameName(String name) {
        return getAgentName().equals(name);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void addEmotionalState(int iteration, EmotionalState emotionalState) {
        seriesSatisfaction.add(iteration, emotionalState.getSatisfaction());
        seriesActivation.add(iteration, emotionalState.getActivation());
    }

}

