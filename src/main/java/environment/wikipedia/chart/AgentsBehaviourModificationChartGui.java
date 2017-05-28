/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.BehaviourType;
import masoes.ontology.state.AgentState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import translate.Translation;
import util.Colors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgentsBehaviourModificationChartGui extends JFrame {

    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private String[] behavioursTypes;
    private Translation translation = Translation.getInstance();
    private Map<String, XYSeries> agents = new HashMap<>();
    private Map<String, Color> colorsMap = new HashMap<>();

    public AgentsBehaviourModificationChartGui(String title) {
        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                dispose();
            }
        });

        collection = new XYSeriesCollection();

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), translation.get("gui.behaviour"), collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();

        BehaviourType[] values = BehaviourType.values();

        List<String> typesList = Arrays.stream(values)
                .map(behaviourType -> getBehaviourTypeName(behaviourType.toString()))
                .collect(Collectors.toList());

        typesList.add(0, "");

        behavioursTypes = typesList.toArray(new String[typesList.size()]);

        SymbolAxis rangeAxis = new SymbolAxis(translation.get("gui.behaviour"), behavioursTypes);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, behavioursTypes.length);
        rangeAxis.setLabelFont(chart.getXYPlot().getDomainAxis().getLabelFont());
        xyPlot.setRangeAxis(rangeAxis);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        xyPlot.setRenderer(renderer);
        xyPlot.setBackgroundPaint(Color.WHITE);
        xyPlot.setDomainGridlinePaint(Color.BLACK);
        xyPlot.setRangeGridlinePaint(Color.BLACK);

        xyPlot.setRangeGridlinesVisible(true);
        xyPlot.setDomainGridlinesVisible(true);

        chart.getLegend().setFrame(BlockBorder.NONE);
    }

    public String getBehaviourTypeName(String behaviourType) {
        return Translation.getInstance().get(behaviourType.toString().toLowerCase().trim());
    }

    public void addAgent(String agentName) {
        if (agents.containsKey(agentName)) {
            return;
        }
        Color color = Colors.getColor(colorsMap.size());
        colorsMap.put(agentName, color);
        XYSeries xySeries = new XYSeries(agentName);
        agents.put(agentName, xySeries);
        collection.addSeries(xySeries);
        xyPlot.getRendererForDataset(collection).setSeriesPaint(agents.size() - 1, color);
    }

    public void addBehaviourType(String agentName, int iteration, AgentState agentState) {
        XYSeries xySeries = agents.get(agentName);
        if (xySeries == null) {
            return;
        }
        int y = Arrays.asList(behavioursTypes).indexOf(getBehaviourTypeName(agentState.getBehaviourState().getType()));
        xySeries.add(iteration, y);
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
