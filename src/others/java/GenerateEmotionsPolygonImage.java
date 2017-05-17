/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalSpace;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class GenerateEmotionsPolygonImage {

    private static final String OUTPUT_OTHERS = "output/others";
    private static final int PADDING = 20;
    private static final int SIZE = 400;
    private static final int SIZE_PADDING = SIZE + PADDING * 2;
    private static final String PNG = "png";

    private Font font = new Font("Arial", Font.PLAIN, 12);
    private BufferedImage image;
    private Graphics2D graphics;

    public static void main(String[] args) {
        new GenerateEmotionsPolygonImage();
    }

    public GenerateEmotionsPolygonImage() {
        makeOutputFolder();
        makeImage();
        emotionIteration();
    }

    private void emotionIteration() {
        new EmotionalSpace().getEmotions()
                .forEach(emotion -> {
                    renderBackground();
                    drawEmotion(emotion);
                    writeImage(emotion);
                    logEmotion(emotion);
                });
    }

    private void logEmotion(Emotion emotion) {
        System.out.println("Complete: " + emotion.getName());
    }

    private void makeOutputFolder() {
        File folder = new File(OUTPUT_OTHERS);
        folder.mkdirs();
    }

    public void makeImage() {
        image = new BufferedImage(SIZE_PADDING, SIZE_PADDING, 1);
        graphics = image.createGraphics();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHints(renderingHints);
    }

    private void renderBackground() {
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.clearRect(0, 0, SIZE_PADDING, SIZE_PADDING);

        drawLine(-1, 0, 1, 0);
        drawLine(0, -1, 0, 1);

        drawString("-1", -.94,-.1);
        drawString("1", .9,-.1);

        drawString("-1", .06,-.95);
        drawString("1", .06,.9);
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        graphics.drawLine(xCanvas(x1), yCanvas(y1), xCanvas(x2), yCanvas(y2));
    }

    private void drawEmotion(Emotion emotion) {

    }

    private void drawString(String string, double x, double y) {
        graphics.drawString(string, xCanvas(x), yCanvas(y));
    }

    public void writeImage(Emotion emotion) {
        File file = new File(OUTPUT_OTHERS, String.format("%s.%s", emotion.getName(), PNG));
        try {
            ImageIO.write(image, PNG, file);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    private int xCanvas(double value) {
        int sign = 1;
        if (value < 0) {
            sign = -1;
        }
        return (int) (SIZE_PADDING / 2 + abs(value) * SIZE / 2 * sign);
    }

    private int yCanvas(double value) {
        int sign = 1;
        if (value >= 0) {
            sign = -1;
        }
        return (int) (SIZE_PADDING / 2 + abs(value) * SIZE / 2 * sign);
    }

}
