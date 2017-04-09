/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.EmotionalState;
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

public class EmotionalStateLineChartGui extends JDialog {

    private Translation translation;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYSeries seriesSatisfaction;
    private XYSeries seriesActivation;
    private XYPlot xyPlot;

    public EmotionalStateLineChartGui(String title) {
        translation = Translation.getInstance();

        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
            }
        });

        seriesActivation = new XYSeries(translation.get("gui.activation"));
        seriesSatisfaction = new XYSeries(translation.get("gui.satisfaction"));

        collection = new XYSeriesCollection();
        collection.addSeries(seriesActivation);
        collection.addSeries(seriesSatisfaction);

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), "", collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(-1.2, 1.2);
    }

    public void addDispersion(int iteration, EmotionalState emotionalState) {
        seriesSatisfaction.add(iteration, emotionalState.getSatisfaction());
        seriesActivation.add(iteration, emotionalState.getActivation());
    }

    public void setRange(double lower, double upper) {
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(lower, upper);
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

}
