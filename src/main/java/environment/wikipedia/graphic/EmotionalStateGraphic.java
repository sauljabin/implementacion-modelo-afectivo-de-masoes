/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.graphic;

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

public class EmotionalStateGraphic extends JDialog {

    private final Translation translation;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYSeries seriesSatisfaction;
    private XYSeries seriesActivation;

    public EmotionalStateGraphic(String title) {
        translation = Translation.getInstance();

        setSize(600, 400);
        setLocationRelativeTo(this);
        setTitle(title);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
            }
        });

        setLayout(new BorderLayout());

        seriesActivation = new XYSeries(translation.get("gui.activation"));
        seriesSatisfaction = new XYSeries(translation.get("gui.satisfaction"));

        collection = new XYSeriesCollection();
        collection.addSeries(seriesActivation);
        collection.addSeries(seriesSatisfaction);

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), "", collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(0.0, 1.0);

        setVisible(true);
    }

    public void addDispersion(int iteration, EmotionalState emotionalState) {
        seriesSatisfaction.add(iteration, emotionalState.getSatisfaction());
        seriesActivation.add(iteration, emotionalState.getActivation());
    }

}
