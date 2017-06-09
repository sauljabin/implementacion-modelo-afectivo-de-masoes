/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.behaviourModification;

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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AgentsBehaviourModificationChartGui extends JFrame {

    private final List<String> behaviourTypesList;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private String[] behavioursTypes;
    private Translation translation = Translation.getInstance();
    private List<SeriesBehaviourContainer> seriesBehaviourContainers = new LinkedList<>();

    public AgentsBehaviourModificationChartGui(String title) {
        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new AgentsBehaviourModificationChartGuiWindowListener(this));

        collection = new XYSeriesCollection();

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), translation.get("gui.behaviour"), collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();

        BehaviourType[] values = BehaviourType.values();

        behaviourTypesList = Arrays.stream(values)
                .map(behaviourType -> getBehaviourTypeName(behaviourType.toString()))
                .collect(Collectors.toList());

        behaviourTypesList.add(0, "");

        behavioursTypes = behaviourTypesList.toArray(new String[behaviourTypesList.size()]);

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

    public Optional<SeriesBehaviourContainer> getSeriesBehaviourContainer(String agent) {
        return seriesBehaviourContainers.stream()
                .filter(seriesBehaviourContainer -> seriesBehaviourContainer.sameName(agent))
                .findFirst();
    }

    public void addAgent(String agentName) {
        if (getSeriesBehaviourContainer(agentName).isPresent()) {
            return;
        }

        SeriesBehaviourContainer seriesBehaviourContainer = new SeriesBehaviourContainer(agentName, seriesBehaviourContainers.size());

        seriesBehaviourContainers.add(seriesBehaviourContainer);

        collection.addSeries(seriesBehaviourContainer.getSeries());

        xyPlot.getRendererForDataset(collection)
                .setSeriesPaint(seriesBehaviourContainer.getSequence(), seriesBehaviourContainer.getColor());
    }

    public void addBehaviourType(String agentName, int iteration, AgentState agentState) {
        Optional<SeriesBehaviourContainer> seriesBehaviourContainer = getSeriesBehaviourContainer(agentName);

        if (!seriesBehaviourContainer.isPresent()) {
            return;
        }

        XYSeries xySeries = seriesBehaviourContainer.get().getSeries();
        if (xySeries == null) {
            return;
        }

        int y = getTypeIndex(agentState);

        xySeries.add(iteration, y);
    }

    public int getTypeIndex(AgentState agentState) {
        return behaviourTypesList.indexOf(getBehaviourTypeName(agentState.getBehaviourState().getType()));
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
