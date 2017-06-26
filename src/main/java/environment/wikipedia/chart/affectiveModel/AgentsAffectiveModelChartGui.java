/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.affectiveModel;

import gui.WindowsEventsAdapter;
import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AgentsAffectiveModelChartGui extends JFrame {

    private AgentsAffectiveModelChart agentsAffectiveModelChart;
    private JPanel agentsNamePanel;
    private Translation translation = Translation.getInstance();
    private List<EmotionalStateContainer> emotionalStateContainers = new LinkedList<>();
    private AgentsAffectiveModelChartGuiCallback callback;

    public AgentsAffectiveModelChartGui(AgentsAffectiveModelChartGuiCallback callback) {
        this.callback = callback;
        setSize(560, 400);
        setTitle(translation.get("gui.emotional_states"));
        setLayout(new BorderLayout());

        addWindowListener(new WindowsEventsAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hideGui();
            }
        });

        agentsAffectiveModelChart = new AgentsAffectiveModelChart(emotionalStateContainers);
        add(agentsAffectiveModelChart, BorderLayout.CENTER);

        agentsNamePanel = new JPanel(new MigLayout("insets 3"));

        JPanel west = new JPanel(new MigLayout("insets 5"));
        west.add(new JLabel(translation.get("gui.agents")), "w 150, wrap 10");
        add(west, BorderLayout.WEST);

        JScrollPane collaboratorNames = new JScrollPane(agentsNamePanel);

        west.add(collaboratorNames, "w 150");
    }

    public void showGui() {
        setVisible(true);
        agentsAffectiveModelChart.start();
    }

    public void hideGui() {
        agentsAffectiveModelChart.stop();
        callback.beforeHide();
        setVisible(false);
    }

    public void disposeGui() {
        agentsAffectiveModelChart.stop();
        dispose();
    }

    public void addAgent(String agent) {
        if (getEmotionalStateContainer(agent).isPresent()) {
            return;
        }

        EmotionalStateContainer emotionalStateContainer = new EmotionalStateContainer(agent, emotionalStateContainers.size());

        agentsNamePanel.add(emotionalStateContainer.getLabel(), "wrap");

        emotionalStateContainers.add(emotionalStateContainer);

        revalidate();
        repaint();
    }

    private Optional<EmotionalStateContainer> getEmotionalStateContainer(String agent) {
        return emotionalStateContainers.stream()
                .filter(emotionalStateContainer -> emotionalStateContainer.sameName(agent))
                .findFirst();
    }

    public void addEmotionalState(String agent, EmotionalState emotionalState) {
        Optional<EmotionalStateContainer> emotionalStateContainer = getEmotionalStateContainer(agent);

        if (!emotionalStateContainer.isPresent()) {
            return;
        }

        emotionalStateContainer.get().setEmotionalState(emotionalState);
    }

    public void exportImage(File folder, int height) throws IOException {
        String extension = "png";
        File file = new File(folder, getTitle() + "." + extension);

        BufferedImage imageAffectiveModel = new BufferedImage(height, height, 1);
        Graphics2D graphicsAffectiveModel = (Graphics2D) imageAffectiveModel.getGraphics();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphicsAffectiveModel.setRenderingHints(renderingHints);

        agentsAffectiveModelChart.render(height, height, graphicsAffectiveModel);

        double columnContentSize = 16;
        int columnWidth = 150;
        int widthAgents = (int) (columnWidth * Math.ceil((double) emotionalStateContainers.size() / columnContentSize));

        BufferedImage imageAgents = new BufferedImage(widthAgents, height, 1);
        Graphics2D graphicsAgents = (Graphics2D) imageAgents.getGraphics();
        graphicsAgents.setRenderingHints(renderingHints);
        graphicsAgents.setBackground(Color.WHITE);
        graphicsAgents.clearRect(0, 0, widthAgents, height);

        Iterator<EmotionalStateContainer> iteratorAgents = emotionalStateContainers.iterator();

        int x;
        int y = 7;
        int i = 1;

        while (iteratorAgents.hasNext()) {
            x = (10 + (int) (Math.ceil((double) i / columnContentSize) - 1) * columnWidth);

            EmotionalStateContainer emotionalStateContainer = iteratorAgents.next();

            graphicsAgents.setColor(emotionalStateContainer.getColor());

            if (iteratorAgents.hasNext()) {
                graphicsAgents.fillOval(x, y, 11, 11);
            } else {
                graphicsAgents.fillRect(x, y, 11, 11);
            }

            graphicsAgents.setColor(Color.BLACK);

            graphicsAgents.drawString(emotionalStateContainer.getAgentName(), x + 18, y + 10);

            if (i % columnContentSize == 0) {
                y = 7;
            } else {
                y += 25;
            }
            i++;
        }

        BufferedImage image = new BufferedImage(widthAgents + height, height, 1);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.drawImage(imageAgents, null, 0, 0);
        graphics.drawImage(imageAffectiveModel, null, widthAgents, 0);

        ImageIO.write(image, extension, file);
    }

}
