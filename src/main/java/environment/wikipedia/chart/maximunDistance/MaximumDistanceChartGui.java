/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.maximunDistance;

import masoes.ontology.state.collective.MaximumDistance;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class MaximumDistanceChartGui extends JFrame {

    private Translation translation = Translation.getInstance();
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYSeries seriesSatisfaction;
    private XYSeries seriesActivation;
    private XYPlot xyPlot;
    private MaximumDistanceChartGuiCallback callback;

    public MaximumDistanceChartGui(MaximumDistanceChartGuiCallback callback) {
        this.callback = callback;
        setSize(560, 400);
        String title = translation.get("gui.maximum_distance");
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                hideGui();
            }
        });

        seriesActivation = new XYSeries(translation.get("gui.activation"));
        seriesSatisfaction = new XYSeries(translation.get("gui.satisfaction"));

        collection = new XYSeriesCollection();
        collection.addSeries(seriesActivation);
        collection.addSeries(seriesSatisfaction);

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), title, collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        rangeAxis.setRange(0, 2.1);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        xyPlot.setRenderer(renderer);
        xyPlot.setBackgroundPaint(Color.WHITE);
        xyPlot.setDomainGridlinePaint(Color.BLACK);
        xyPlot.setRangeGridlinePaint(Color.BLACK);

        xyPlot.setRangeGridlinesVisible(true);
        xyPlot.setDomainGridlinesVisible(true);

        chart.getLegend().setFrame(BlockBorder.NONE);
    }

    public void addMaximumDistance(int iteration, MaximumDistance maximumDistance) {
        seriesSatisfaction.add(iteration, maximumDistance.getSatisfaction());
        seriesActivation.add(iteration, maximumDistance.getActivation());
    }

    public void showGui() {
        setVisible(true);
    }

    public void hideGui() {
        callback.beforeHide();
        setVisible(false);
    }

    public void disposeGui() {
        dispose();
    }

    public void exportImage(File folder, int width, int height) throws IOException {
        String extension = "png";
        File file = new File(folder, getTitle() + "." + extension);
        ImageIO.write(chart.createBufferedImage(width, height), extension, file);
    }

    public void clear() {
        seriesSatisfaction.clear();
        seriesActivation.clear();
    }

}
