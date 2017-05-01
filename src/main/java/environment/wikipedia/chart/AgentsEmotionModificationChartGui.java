/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.BehaviourType;
import masoes.component.behavioural.EmotionalSpace;
import masoes.ontology.state.AgentState;
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
import util.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgentsEmotionModificationChartGui extends JFrame {

    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private String[] emotionTypes;
    private Translation translation = Translation.getInstance();
    private Map<String, XYSeries> agents = new HashMap<>();
    private Map<String, Color> colorsMap = new HashMap<>();

    public AgentsEmotionModificationChartGui(String title) {
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

        BehaviourType[] values = BehaviourType.values();

        EmotionalSpace emotionalSpace = new EmotionalSpace();

        List<String> typesList = emotionalSpace.getEmotions()
                .stream()
                .map(emotion -> getEmotionName(emotion.getName()))
                .collect(Collectors.toList());

        Collections.reverse(typesList);

        typesList.add(0, "");

        emotionTypes = typesList.toArray(new String[typesList.size()]);

        SymbolAxis rangeAxis = new SymbolAxis("", emotionTypes);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, emotionTypes.length);
        xyPlot.setRangeAxis(rangeAxis);
    }

    public String getEmotionName(String emotionName) {
        return Translation.getInstance().get(emotionName.toString().toLowerCase().trim());
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

    public void addEmotion(String agentName, int iteration, AgentState agentState) {
        XYSeries xySeries = agents.get(agentName);
        if (xySeries == null) {
            return;
        }
        int y = Arrays.asList(emotionTypes).indexOf(getEmotionName(agentState.getEmotionState().getName()));
        xySeries.add(iteration, y);
    }

    public void start() {
        setVisible(true);
    }

    public void stop() {
        dispose();
    }

}
