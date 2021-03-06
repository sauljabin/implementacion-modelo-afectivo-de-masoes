/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.chart.affectivemodel;

import masoes.MasoesSettings;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class AffectiveModelChart extends Canvas implements Runnable {

    private static final String HAPPINESS = "happiness";
    private static final String JOY = "joy";
    private static final String COMPASSION = "compassion";
    private static final String ADMIRATION = "admiration";
    private static final String DEPRESSION = "depression";
    private static final String SADNESS = "sadness";
    private static final String REJECTION = "rejection";
    private static final String ANGER = "anger";

    private static final Color GRAY_COLOR = new Color(235, 235, 235);
    private boolean stop;

    private Translation translation;
    private Font font;
    private Thread thread;
    private BufferedImage image;
    private Graphics2D graphics;
    private EmotionalState emotionalState;
    private AffectiveModel affectiveModel;

    public AffectiveModelChart() {
        thread = new Thread(this);
        font = new Font("Arial", Font.PLAIN, 10);
        translation = Translation.getInstance();
        affectiveModel = AffectiveModel.getInstance();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                makeImage();
            }
        });
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    protected void render() {
        renderBackground();
        renderExternalBox();
        renderInnerBox();
        renderLines();
        renderEmotionIntersection();
        renderIntervals();
        renderTexts();
        renderEmotionalState();
        getGraphics().drawImage(image, 0, 0, this);
    }

    private void renderEmotionalState() {
        if (emotionalState == null) {
            return;
        }
        graphics.setColor(Color.RED);
        int x = xCanvas(emotionalState.getActivation());
        int y = yCanvas(emotionalState.getSatisfaction());
        graphics.fillOval(x - 3, y - 3, 6, 6);
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

    private void renderEmotionIntersection() {
        if (emotionalState == null) {
            return;
        }

        graphics.setColor(Color.RED);

        String emotionName = affectiveModel.searchEmotion(emotionalState).getName();

        Polygon polygon = new Polygon();
        switch (emotionName) {
            case HAPPINESS:
                polygon.addPoint(getWidth() / 2, 0);
                polygon.addPoint(getWidth() - 1, 0);
                polygon.addPoint(getWidth() - 1, getHeight() / 2);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 4);
                polygon.addPoint(getWidth() / 2, getHeight() / 4);
                break;
            case JOY:
                polygon.addPoint(getWidth() / 2, getHeight() / 2);
                polygon.addPoint(getWidth() / 2, getHeight() / 4);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 4);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2);
                break;
            case COMPASSION:
                polygon.addPoint(0, 0);
                polygon.addPoint(getWidth() / 2, 0);
                polygon.addPoint(getWidth() / 2, getHeight() / 4);
                polygon.addPoint(getWidth() / 4, getHeight() / 4);
                polygon.addPoint(getWidth() / 4, getHeight() / 2);
                polygon.addPoint(0, getHeight() / 2);
                break;
            case ADMIRATION:
                polygon.addPoint(getWidth() / 2, getHeight() / 2);
                polygon.addPoint(getWidth() / 4, getHeight() / 2);
                polygon.addPoint(getWidth() / 4, getHeight() / 4);
                polygon.addPoint(getWidth() / 2, getHeight() / 4);
                break;
            case DEPRESSION:
                polygon.addPoint(0, getHeight() / 2);
                polygon.addPoint(0, getHeight() - 1);
                polygon.addPoint(getWidth() / 2, getHeight() - 1);
                polygon.addPoint(getWidth() / 2, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 4, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 4, getHeight() / 2);
                break;
            case SADNESS:
                polygon.addPoint(getWidth() / 2, getHeight() / 2);
                polygon.addPoint(getWidth() / 4, getHeight() / 2);
                polygon.addPoint(getWidth() / 4, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 2, getHeight() / 2 + getHeight() / 4);
                break;
            case REJECTION:
                polygon.addPoint(getWidth() / 2, getHeight() / 2);
                polygon.addPoint(getWidth() / 2, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2);
                break;
            case ANGER:
                polygon.addPoint(getWidth() / 2, getHeight() - 1);
                polygon.addPoint(getWidth() - 1, getHeight() - 1);
                polygon.addPoint(getWidth() - 1, getHeight() / 2);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2);
                polygon.addPoint(getWidth() / 2 + getWidth() / 4, getHeight() / 2 + getHeight() / 4);
                polygon.addPoint(getWidth() / 2, getHeight() / 2 + getHeight() / 4);
                break;
        }
        graphics.drawPolygon(polygon);
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
                Thread.sleep(1000 / getFPS());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long getFPS() {
        return Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.GUI_FPS));
    }

    public void clear() {
        emotionalState = null;
    }

}
