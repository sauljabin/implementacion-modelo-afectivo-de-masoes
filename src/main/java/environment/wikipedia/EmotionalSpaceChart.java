/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import masoes.behavioural.EmotionalState;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class EmotionalSpaceChart extends Canvas implements Runnable {

    private static int FPS = 10;
    private final Translation translation;
    private Font font;
    private Thread thread;
    private BufferedImage image;
    private Graphics2D graphics;
    private List<EmotionalState> emotionalStates;
    private boolean stop;
    private EmotionalState lastEmotionalState;

    public EmotionalSpaceChart() {
        emotionalStates = new ArrayList<>();
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

    public static void main(String[] args) {
        EmotionalSpaceChart spaceChart = new EmotionalSpaceChart();

        JFrame jFrame = new JFrame();
        jFrame.setSize(300, 300);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(spaceChart, BorderLayout.CENTER);
        jFrame.setLocationRelativeTo(jFrame);
        jFrame.setVisible(true);

        spaceChart.start();
        for (int i = 0; i < 5; i++) {
            spaceChart.addEmotionalState(new EmotionalState());
        }
    }

    public void addEmotionalState(EmotionalState emotionalState) {
        if (lastEmotionalState != null) {
            if (lastEmotionalState.getActivation() == emotionalState.getActivation()
                    && lastEmotionalState.getSatisfaction() == emotionalState.getSatisfaction()) {
                return;
            }
        }
        emotionalStates.add(emotionalState);
        lastEmotionalState = emotionalState;
    }

    private void rendering() {
        graphics.setBackground(Color.WHITE);
        graphics.setFont(font);
        graphics.clearRect(0, 0, getWidth(), getHeight());

        // CUADRO EXTERIOR
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // CUADRO INTERIOR
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);

        // LINEAS
        graphics.setColor(Color.BLACK);
        graphics.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        graphics.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        graphics.drawRect(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);

        // RANGOS
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawString("1", convertXToCanvas(getWidth(), .05), convertYToCanvas(getHeight(), .9));
        graphics.drawString("1", convertXToCanvas(getWidth(), .9), convertYToCanvas(getHeight(), -.1));
        graphics.drawString("-1", convertXToCanvas(getWidth(), .05), convertYToCanvas(getHeight(), -.9));
        graphics.drawString("-1", convertXToCanvas(getWidth(), -.95), convertYToCanvas(getHeight(), -.1));

        // TEXTOS
        graphics.setColor(Color.BLACK);
        graphics.drawString(getEmotionTranslation("compassion"), convertXToCanvas(getWidth(), -.8), convertYToCanvas(getHeight(), .7));
        graphics.drawString(getEmotionTranslation("happiness"), convertXToCanvas(getWidth(), .5), convertYToCanvas(getHeight(), .7));
        graphics.drawString(getEmotionTranslation("anger"), convertXToCanvas(getWidth(), .5), convertYToCanvas(getHeight(), -.8));
        graphics.drawString(getEmotionTranslation("depression"), convertXToCanvas(getWidth(), -.8), convertYToCanvas(getHeight(), -.8));

        graphics.drawString(getEmotionTranslation("admiration"), convertXToCanvas(getWidth(), -.45), convertYToCanvas(getHeight(), .40));
        graphics.drawString(getEmotionTranslation("joy"), convertXToCanvas(getWidth(), .05), convertYToCanvas(getHeight(), .40));
        graphics.drawString(getEmotionTranslation("sadness"), convertXToCanvas(getWidth(), -.45), convertYToCanvas(getHeight(), -.45));
        graphics.drawString(getEmotionTranslation("rejection"), convertXToCanvas(getWidth(), .05), convertYToCanvas(getHeight(), -.45));

        // PUNTOS
        graphics.setColor(Color.RED);

        for (int i = 0; i < emotionalStates.size() - 1; i++) {
            EmotionalState emotionalState = emotionalStates.get(i);
            int x = convertXToCanvas(getWidth(), emotionalState.getActivation());
            int y = convertYToCanvas(getHeight(), emotionalState.getSatisfaction());
            graphics.fillRect(x - 2, y - 2, 4, 4);
        }

        if (emotionalStates.size() > 0) {
            graphics.setColor(Color.BLUE);
            EmotionalState lastEmotionalState = emotionalStates.get(emotionalStates.size() - 1);
            int x = convertXToCanvas(getWidth(), lastEmotionalState.getActivation());
            int y = convertYToCanvas(getHeight(), lastEmotionalState.getSatisfaction());
            graphics.fillRect(x - 3, y - 3, 6, 6);
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x - 3, y - 3, 6, 6);
        }

        getGraphics().drawImage(image, 0, 0, this);
    }

    private String getEmotionTranslation(String key) {
        return translation.get(key).toUpperCase();
    }

    private int convertXToCanvas(int size, double value) {
        int sign = 1;
        if (value < 0) {
            sign = -1;
        }
        return (int) ((size / 2 + abs(value) * size / 2 * sign));
    }

    private int convertYToCanvas(int size, double value) {
        int sign = 1;
        if (value >= 0) {
            sign = -1;
        }
        return (int) ((size / 2 + abs(value) * size / 2 * sign));
    }

    public void start() {
        if (!thread.isAlive()) {
            makeImage();
            thread.start();
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

    private synchronized void makeImage() {
        image = new BufferedImage(getWidth(), getHeight(), 1);
        graphics = (Graphics2D) image.getGraphics();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHints(renderingHints);
    }

    @Override
    public void run() {
        while (!stop) {
            rendering();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
