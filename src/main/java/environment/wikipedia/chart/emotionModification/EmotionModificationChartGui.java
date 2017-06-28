/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.emotionModification;

import masoes.component.behavioural.AffectiveModel;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmotionModificationChartGui extends JFrame {

    private final List<String> emotionTypesList;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private String[] emotionTypes;
    private Translation translation = Translation.getInstance();
    private List<SeriesEmotionContainer> seriesEmotionContainers = new LinkedList<>();
    private EmotionModificationChartGuiCallback callback;

    public EmotionModificationChartGui(EmotionModificationChartGuiCallback callback) {
        this.callback = callback;
        setSize(560, 400);
        String title = translation.get("gui.emotion");
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                hideGui();
            }
        });

        collection = new XYSeriesCollection();

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), title, collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();

        AffectiveModel affectiveModel = AffectiveModel.getInstance();

        emotionTypesList = affectiveModel.getEmotions()
                .stream()
                .map(emotion -> getEmotionName(emotion.getName()))
                .collect(Collectors.toList());

        Collections.reverse(emotionTypesList);

        emotionTypesList.add(0, "");

        emotionTypes = emotionTypesList.toArray(new String[emotionTypesList.size()]);

        SymbolAxis rangeAxis = new SymbolAxis(translation.get("gui.emotion"), emotionTypes);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, emotionTypes.length);
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

    public String getEmotionName(String emotionName) {
        return Translation.getInstance().get(emotionName.toString().toLowerCase().trim());
    }

    public Optional<SeriesEmotionContainer> getSeriesEmotionContainer(String agent) {
        return seriesEmotionContainers.stream()
                .filter(seriesEmotionContainer -> seriesEmotionContainer.sameName(agent))
                .findFirst();
    }

    public void addAgent(String agentName) {
        if (getSeriesEmotionContainer(agentName).isPresent()) {
            return;
        }

        SeriesEmotionContainer seriesEmotionContainer = new SeriesEmotionContainer(agentName, seriesEmotionContainers.size());

        seriesEmotionContainers.add(seriesEmotionContainer);

        collection.addSeries(seriesEmotionContainer.getSeries());

        xyPlot.getRendererForDataset(collection)
                .setSeriesPaint(seriesEmotionContainer.getSequence(), seriesEmotionContainer.getColor());
    }

    public void addEmotion(String agentName, int iteration, AgentState agentState) {
        Optional<SeriesEmotionContainer> seriesEmotionContainer = getSeriesEmotionContainer(agentName);

        if (!seriesEmotionContainer.isPresent()) {
            return;
        }

        XYSeries xySeries = seriesEmotionContainer.get().getSeries();
        if (xySeries == null) {
            return;
        }

        int y = getEmotionIndex(agentState);

        xySeries.add(iteration, y);
    }

    public int getEmotionIndex(AgentState agentState) {
        return emotionTypesList.indexOf(getEmotionName(agentState.getEmotionState().getName()));
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

    public void clear() {
        collection.removeAllSeries();
        seriesEmotionContainers.clear();
    }

    public void exportImage(File folder, int width, int height) throws IOException {
        String extension = "png";
        File file = new File(folder, getTitle() + "." + extension);
        ImageIO.write(chart.createBufferedImage(width, height), extension, file);
    }

}
