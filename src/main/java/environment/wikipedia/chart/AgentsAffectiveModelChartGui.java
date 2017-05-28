/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart;

import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;
import util.Colors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.abs;

public class AgentsAffectiveModelChartGui extends JFrame {

    private AgentsAffectiveModelChart agentsAffectiveModelChart;
    private JPanel agentsNamePanel;
    private Translation translation = Translation.getInstance();
    private Map<String, Color> colorsMap = new TreeMap<>();
    private Map<String, EmotionalState> emotionMap = new TreeMap<>();
    private List<String> agents = new LinkedList<>();

    public AgentsAffectiveModelChartGui(String title) {
        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                agentsAffectiveModelChart.stop();
                dispose();
            }
        });

        agentsAffectiveModelChart = new AgentsAffectiveModelChart(colorsMap, emotionMap);
        add(agentsAffectiveModelChart, BorderLayout.CENTER);

        agentsNamePanel = new JPanel(new MigLayout("insets 3"));

        JPanel west = new JPanel(new MigLayout("insets 5"));
        west.add(new JLabel(translation.get("gui.agents")), "w 150, wrap 10");
        add(west, BorderLayout.WEST);

        JScrollPane collaboratorNames = new JScrollPane(agentsNamePanel);

        west.add(collaboratorNames, "w 150");
    }

    public void start() {
        setVisible(true);
        agentsAffectiveModelChart.start();
    }

    public void stop() {
        agentsAffectiveModelChart.stop();
        dispose();
    }

    public void addAgent(String agent) {
        if (agents.contains(agent)) {
            return;
        }
        agents.add(agent);
        Color color = Colors.getColor(colorsMap.size());
        colorsMap.put(agent, color);
        JLabel agentName = new JLabel(agent);
        agentName.setForeground(color);
        agentsNamePanel.add(agentName, "wrap");
        revalidate();
        repaint();
    }

    public void addEmotionalState(String agent, EmotionalState emotionalState) {
        emotionMap.put(agent, emotionalState);
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
        int widthAgents = (int) (columnWidth * Math.ceil((double) agents.size() / columnContentSize));
        BufferedImage imageAgents = new BufferedImage(widthAgents, height, 1);
        Graphics2D graphicsAgents = (Graphics2D) imageAgents.getGraphics();
        graphicsAgents.setRenderingHints(renderingHints);
        graphicsAgents.setBackground(Color.WHITE);
        graphicsAgents.clearRect(0, 0, widthAgents, height);

        Iterator<String> iteratorAgents = agents.iterator();

        int x;
        int y = 7;
        int i = 1;

        while (iteratorAgents.hasNext()) {
            x = (10 + (int) (Math.ceil((double) i / columnContentSize) - 1) * columnWidth);

            String agentName = iteratorAgents.next();

            graphicsAgents.setColor(colorsMap.get(agentName));

            if (iteratorAgents.hasNext()) {
                graphicsAgents.fillOval(x, y, 11, 11);
            } else {
                graphicsAgents.fillRect(x, y, 11, 11);
            }

            graphicsAgents.setColor(Color.BLACK);

            graphicsAgents.drawString(agentName, x + 18, y + 10);

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

    private class AgentsAffectiveModelChart extends Canvas implements Runnable {

        private final String HAPPINESS = "happiness";
        private final String JOY = "joy";
        private final String COMPASSION = "compassion";
        private final String ADMIRATION = "admiration";
        private final String DEPRESSION = "depression";
        private final String SADNESS = "sadness";
        private final String REJECTION = "rejection";
        private final String ANGER = "anger";
        private final Color GRAY_COLOR = new Color(235, 235, 235);
        private final int FPS = 5;

        private Map<String, Color> colorsMap;
        private Map<String, EmotionalState> emotionMap;

        private boolean stop;

        private Translation translation;
        private Font font;
        private Thread thread;
        private BufferedImage image;
        private Graphics2D graphics;

        public AgentsAffectiveModelChart(Map<String, Color> colorsMap, Map<String, EmotionalState> emotionMap) {
            this.colorsMap = colorsMap;
            this.emotionMap = emotionMap;
            thread = new Thread(this);
            font = new Font("Arial", Font.PLAIN, 10);
            translation = Translation.getInstance();
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    makeImage();
                }
            });
        }

        protected void render(int width, int height, Graphics2D graphics) {
            renderBackground(width, height, graphics);
            renderExternalBox(width, height, graphics);
            renderInnerBox(width, height, graphics);
            renderLines(width, height, graphics);
            renderIntervals(width, height, graphics);
            renderTexts(width, height, graphics);
            renderEmotions(width, height, graphics);
        }

        protected void drawImage() {
            getGraphics().drawImage(image, 0, 0, this);
        }

        private void renderEmotions(int width, int height, Graphics2D graphics) {
            if (colorsMap.isEmpty()) {
                return;
            }

            Iterator<String> iterator = agents.iterator();

            while (iterator.hasNext()) {
                String agentName = iterator.next();

                graphics.setColor(colorsMap.get(agentName));

                EmotionalState emotionalState = emotionMap.get(agentName);

                if (emotionalState == null) {
                    continue;
                }

                int x = xCanvas(emotionalState.getActivation(), width);
                int y = yCanvas(emotionalState.getSatisfaction(), height);

                if (iterator.hasNext()) {
                    graphics.fillOval(x - 3, y - 3, 6, 6);
                } else {
                    graphics.fillRect(x - 4, y - 4, 8, 8);
                }

            }
        }

        private void renderTexts(int width, int height, Graphics2D graphics) {
            graphics.setColor(Color.BLACK);
            graphics.drawString(getEmotionTranslation(COMPASSION), xCanvas(-.8, width), yCanvas(.7, height));
            graphics.drawString(getEmotionTranslation(HAPPINESS), xCanvas(.5, width), yCanvas(.7, height));
            graphics.drawString(getEmotionTranslation(ANGER), xCanvas(.5, width), yCanvas(-.8, height));
            graphics.drawString(getEmotionTranslation(DEPRESSION), xCanvas(-.8, width), yCanvas(-.8, height));

            graphics.drawString(getEmotionTranslation(ADMIRATION), xCanvas(-.45, width), yCanvas(.40, height));
            graphics.drawString(getEmotionTranslation(JOY), xCanvas(.05, width), yCanvas(.40, height));
            graphics.drawString(getEmotionTranslation(SADNESS), xCanvas(-.45, width), yCanvas(-.45, height));
            graphics.drawString(getEmotionTranslation(REJECTION), xCanvas(.05, width), yCanvas(-.45, height));
        }

        private void renderIntervals(int width, int height, Graphics2D graphics) {
            graphics.setColor(Color.DARK_GRAY);
            graphics.drawString("1", xCanvas(.05, width), yCanvas(.9, height));
            graphics.drawString("1", xCanvas(.9, width), yCanvas(-.1, height));
            graphics.drawString("-1", xCanvas(.05, width), yCanvas(-.9, height));
            graphics.drawString("-1", xCanvas(-.95, width), yCanvas(-.1, height));
        }

        private void renderLines(int width, int height, Graphics2D graphics) {
            graphics.setColor(Color.BLACK);
            graphics.drawLine(width / 2, 0, width / 2, height);
            graphics.drawLine(0, height / 2, width, height / 2);
            graphics.drawRect(width / 4, height / 4, width / 2, height / 2);
        }

        private void renderInnerBox(int width, int height, Graphics2D graphics) {
            graphics.setColor(GRAY_COLOR);
            graphics.fillRect(width / 4, height / 4, width / 2, height / 2);
        }

        private void renderExternalBox(int width, int height, Graphics2D graphics) {
            graphics.setColor(Color.BLACK);
            graphics.drawRect(0, 0, width - 1, height - 1);
        }

        private void renderBackground(int width, int height, Graphics2D graphics) {
            graphics.setBackground(Color.WHITE);
            graphics.setFont(font);
            graphics.clearRect(0, 0, width, height);
        }

        private String getEmotionTranslation(String key) {
            return translation.get(key).toUpperCase();
        }

        private int xCanvas(double value, int width) {
            int sign = 1;
            if (value < 0) {
                sign = -1;
            }
            return (int) ((width / 2 + abs(value) * width / 2 * sign));
        }

        private int yCanvas(double value, int height) {
            int sign = 1;
            if (value >= 0) {
                sign = -1;
            }
            return (int) ((height / 2 + abs(value) * height / 2 * sign));
        }

        public void start() {
            if (!thread.isAlive()) {
                makeImage();
                thread.start();
                stop = false;
            }
        }

        public void stop() {
            stop = true;
            try {
                thread.join(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void makeImage() {
            image = new BufferedImage(getWidth(), getHeight(), 1);
            graphics = (Graphics2D) image.getGraphics();
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHints(renderingHints);
        }

        @Override
        public void run() {
            while (!stop) {
                render(getWidth(), getHeight(), graphics);
                drawImage();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
