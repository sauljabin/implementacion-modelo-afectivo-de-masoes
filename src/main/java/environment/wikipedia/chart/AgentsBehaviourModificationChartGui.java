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

public class AgentsBehaviourModificationChartGui extends JDialog {

    private Translation translation;
    private XYSeriesCollection collection;
    private JFreeChart chart;
    private XYPlot xyPlot;
    private Map<String, XYSeries> agents;
    private String[] behavioursTypes;

    public AgentsBehaviourModificationChartGui(String title) {
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

        BehaviourType[] values = BehaviourType.values();

        List<String> typesList = Arrays.stream(values)
                .map(behaviourType -> getBehaviourTypeName(behaviourType.toString()))
                .collect(Collectors.toList());

        typesList.add(0, "");

        behavioursTypes = typesList.toArray(new String[typesList.size()]);

        SymbolAxis rangeAxis = new SymbolAxis("", behavioursTypes);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, behavioursTypes.length);
        xyPlot.setRangeAxis(rangeAxis);
    }

    public String getBehaviourTypeName(String behaviourType) {
        return Translation.getInstance().get(behaviourType.toString().toLowerCase().trim());
    }


    public static void main(String[] args) {
        AgentsBehaviourModificationChartGui agentsBehaviourModificationChartGui = new AgentsBehaviourModificationChartGui("title");
        agentsBehaviourModificationChartGui.start();
    }

    public void addAgent(String agentName) {
        XYSeries xySeries = new XYSeries(agentName);
        agents.put(agentName, xySeries);
        collection.addSeries(xySeries);
    }

    public void addBehaviourType(int iteration, String agentName, AgentState agentState) {
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

}
