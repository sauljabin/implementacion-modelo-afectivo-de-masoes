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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.abs;

public class AgentsEmotionalSpaceChartGui extends JFrame {

    private AgentsEmotionalSpaceChart emotionalSpaceChart;
    private JPanel agentsNamePanel;
    private Translation translation = Translation.getInstance();
    private Map<String, Color> colorsMap = new TreeMap<>();
    private Map<String, EmotionalState> emotionMap = new TreeMap<>();

    public AgentsEmotionalSpaceChartGui(String title) {
        setSize(560, 400);
        setTitle(title);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                emotionalSpaceChart.stop();
                dispose();
            }
        });

        emotionalSpaceChart = new AgentsEmotionalSpaceChart(colorsMap, emotionMap);
        add(emotionalSpaceChart, BorderLayout.CENTER);

        agentsNamePanel = new JPanel(new MigLayout("insets 3"));

        JPanel west = new JPanel(new MigLayout("insets 5"));
        west.add(new JLabel(translation.get("gui.agents")), "w 150, wrap 10");
        add(west, BorderLayout.WEST);

        JScrollPane collaboratorNames = new JScrollPane(agentsNamePanel);

        west.add(collaboratorNames, "w 150");
    }

    public void start() {
        setVisible(true);
        emotionalSpaceChart.start();
    }

    public void stop() {
        emotionalSpaceChart.stop();
        dispose();
    }

    public void addAgent(String agent) {
        if (emotionMap.containsKey(agent)) {
            return;
        }
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

    private class AgentsEmotionalSpaceChart extends Canvas implements Runnable {

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

        public AgentsEmotionalSpaceChart(Map<String, Color> colorsMap, Map<String, EmotionalState> emotionMap) {
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

        protected void render() {
            renderBackground();
            renderExternalBox();
            renderInnerBox();
            renderLines();
            renderIntervals();
            renderTexts();
            renderEmotions();
            getGraphics().drawImage(image, 0, 0, this);
        }

        private void renderEmotions() {
            if (colorsMap.isEmpty()) {
                return;
            }

            Iterator<String> iterator = colorsMap.keySet()
                    .stream()
                    .sorted()
                    .iterator();

            while (iterator.hasNext()) {
                String agentName = iterator.next();

                graphics.setColor(colorsMap.get(agentName));

                EmotionalState emotionalState = emotionMap.get(agentName);

                if (emotionalState == null) {
                    continue;
                }

                int x = xCanvas(emotionalState.getActivation());
                int y = yCanvas(emotionalState.getSatisfaction());

                if (iterator.hasNext()) {
                    graphics.fillOval(x - 3, y - 3, 6, 6);
                } else {
                    graphics.fillRect(x - 4, y - 4, 8, 8);
                }

            }
        }

        private void renderTexts() {
            graphics.setColor(Color.BLACK);
            graphics.drawString(getEmotionTranslation(COMPASSION), xCanvas(-.8), yCanvas(.7));
            graphics.drawString(getEmotionTranslation(HAPPINESS), xCanvas(.5), yCanvas(.7));
            graphics.drawString(getEmotionTranslation(ANGER), xCanvas(.5), yCanvas(-.8));
            graphics.drawString(getEmotionTranslation(DEPRESSION), xCanvas(-.8), yCanvas(-.8));

            graphics.drawString(getEmotionTranslation(ADMIRATION), xCanvas(-.45), yCanvas(.40));
            graphics.drawString(getEmotionTranslation(JOY), xCanvas(.05), yCanvas(.40));
            graphics.drawString(getEmotionTranslation(SADNESS), xCanvas(-.45), yCanvas(-.45));
            graphics.drawString(getEmotionTranslation(REJECTION), xCanvas(.05), yCanvas(-.45));
        }

        private void renderIntervals() {
            graphics.setColor(Color.DARK_GRAY);
            graphics.drawString("1", xCanvas(.05), yCanvas(.9));
            graphics.drawString("1", xCanvas(.9), yCanvas(-.1));
            graphics.drawString("-1", xCanvas(.05), yCanvas(-.9));
            graphics.drawString("-1", xCanvas(-.95), yCanvas(-.1));
        }

        private void renderLines() {
            graphics.setColor(Color.BLACK);
            graphics.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
            graphics.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            graphics.drawRect(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        }

        private void renderInnerBox() {
            graphics.setColor(GRAY_COLOR);
            graphics.fillRect(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        }

        private void renderExternalBox() {
            graphics.setColor(Color.BLACK);
            graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        private void renderBackground() {
            graphics.setBackground(Color.WHITE);
            graphics.setFont(font);
            graphics.clearRect(0, 0, getWidth(), getHeight());
        }

        private String getEmotionTranslation(String key) {
            return translation.get(key).toUpperCase();
        }

        private int xCanvas(double value) {
            int sign = 1;
            if (value < 0) {
                sign = -1;
            }
            return (int) ((getWidth() / 2 + abs(value) * getWidth() / 2 * sign));
        }

        private int yCanvas(double value) {
            int sign = 1;
            if (value >= 0) {
                sign = -1;
            }
            return (int) ((getHeight() / 2 + abs(value) * getHeight() / 2 * sign));
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
                render();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
