/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.ontology.state.collective.MaximumDistances;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import translate.Translation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class MaximumDistancesLineChartGui extends JFrame {

    private Translation translation = Translation.getInstance();
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYSeries seriesSatisfaction;
    private XYSeries seriesActivation;
    private XYPlot xyPlot;

    public MaximumDistancesLineChartGui(String title) {
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
        rangeAxis.setRange(0, 2.1);
    }

    public void addMaximumDistances(int iteration, MaximumDistances emotionalState) {
        seriesSatisfaction.add(iteration, emotionalState.getSatisfaction());
        seriesActivation.add(iteration, emotionalState.getActivation());
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

    public void exportImage(File folder, int width, int height) throws IOException {
        String extension = "png";
        File file = new File(folder, getTitle() + "." + extension);
         ImageIO.write(chart.createBufferedImage(width, height), extension, file);
    }

}
