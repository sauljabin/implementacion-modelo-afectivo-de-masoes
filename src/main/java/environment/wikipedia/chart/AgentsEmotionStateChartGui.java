/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalSpace;
import masoes.component.behavioural.EmotionalState;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgentsEmotionStateChartGui extends JDialog {

    private Translation translation;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private Map<String, XYSeries> agents;
    private final EmotionalSpace emotionalSpace;
    private final String[] emotions;

    public AgentsEmotionStateChartGui(String title) {
        translation = Translation.getInstance();
        agents = new HashMap<>();
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

        chart = ChartFactory.createXYLineChart(title, translation.get("gui.iteration"), "", collection, PlotOrientation.VERTICAL, true, true, false);
        add(new ChartPanel(chart), BorderLayout.CENTER);

        xyPlot = chart.getXYPlot();

        emotionalSpace = new EmotionalSpace();
        List<String> listEmotionsName = emotionalSpace.getEmotions()
                .stream()
                .map(emotion -> getEmotionName(emotion))
                .collect(Collectors.toList());

        listEmotionsName.add(0, "");

        emotions = listEmotionsName.toArray(new String[listEmotionsName.size()]);

        SymbolAxis rangeAxis = new SymbolAxis("", emotions);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, emotions.length);
        xyPlot.setRangeAxis(rangeAxis);
    }

    public String getEmotionName(Emotion emotion) {
        return Translation.getInstance().get(emotion.getName().toLowerCase().trim());
    }

    public static void main(String[] args) {
        AgentsEmotionStateChartGui agentsEmotionStateChartGui = new AgentsEmotionStateChartGui("title");
        agentsEmotionStateChartGui.start();
    }

    public void addAgent(String agentName) {
        XYSeries xySeries = new XYSeries(agentName);
        agents.put(agentName, xySeries);
        collection.addSeries(xySeries);
    }

    public void addEmotion(int iteration, String agentName, EmotionalState emotionalState) {
        XYSeries xySeries = agents.get(agentName);
        if (xySeries == null) {
            return;
        }
        int y = Arrays.asList(emotions).indexOf(getEmotionName(emotionalState.toEmotion()));
        xySeries.add(iteration, y);
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

}
