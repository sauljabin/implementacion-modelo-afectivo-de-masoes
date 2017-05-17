/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalSpace;
import translate.Translation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class GenerateEmotionsPolygonImage {

    private static final int SIZE = 400;
    private static final int PADDING = 50;
    private static final int POINT_SIZE = 8;
    private static final int SIZE_PADDING = SIZE + PADDING * 2;

    private static final String OUTPUT_OTHERS = "output/others";
    private static final String PNG = "png";

    private static final Color BLACK_COLOR = Color.BLACK;
    private static final Color DARKRED_COLOR = new Color(203, 71, 79);
    private static final Color BLUE_COLOR = new Color(158, 230, 237);

    private Font font = new Font("Arial", Font.PLAIN, 12);
    private BasicStroke stroke = new BasicStroke(1.1f);
    private Translation translation = Translation.getInstance();

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

    private void makeImage() {
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
        graphics.setStroke(stroke);

        drawLine(-1, 0, 1, 0);
        drawLine(0, -1, 0, 1);

        drawString("-1", -1.1, -.02);
        drawString("1", 1.1, -.02);

        drawString("-1", -.02, -1.1);
        drawString("1", -.02, 1.1);
    }

    private void writeImage(Emotion emotion) {
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

    private void drawLine(double x1, double y1, double x2, double y2) {
        graphics.drawLine(xCanvas(x1), yCanvas(y1), xCanvas(x2), yCanvas(y2));
    }

    private void drawString(String string, double x, double y) {
        graphics.drawString(string, xCanvas(x), yCanvas(y));
    }

    private void drawOval(double x, double y, int size) {
        graphics.drawOval(xCanvas(x) - size / 2, yCanvas(y) - size / 2, size, size);
    }

    private void fillOval(double x, double y, int size) {
        graphics.fillOval(xCanvas(x) - size / 2, yCanvas(y) - size / 2, size, size);
    }

    private void setColor(Color color) {
        graphics.setColor(color);
    }

    private void drawPolygon(Polygon polygon) {
        graphics.drawPolygon(polygon);
    }

    private void fillPolygon(Polygon polygon) {
        graphics.fillPolygon(polygon);
    }

    private String getEmotionTranslation(Emotion emotion) {
        return translation.get(emotion.getName().toLowerCase()).toUpperCase();
    }

    private Polygon emotionToPolygon(Emotion emotion) {
        Polygon polygon = new Polygon();
        Arrays.stream(emotion.getCoordinates())
                .collect(Collectors.toSet())
                .forEach(coordinate -> polygon.addPoint(xCanvas(coordinate.x), yCanvas(coordinate.y)));
        return polygon;
    }

    private void drawEmotion(Emotion emotion) {
        graphics.drawString(getEmotionTranslation(emotion), 20, 20);

        Polygon emotionToPolygon = emotionToPolygon(emotion);

        setColor(BLUE_COLOR);
        fillPolygon(emotionToPolygon);

        setColor(BLACK_COLOR);
        drawPolygon(emotionToPolygon);

        Alphabet alphabet = new Alphabet();

        Arrays.stream(emotion.getCoordinates())
                .collect(Collectors.toSet())
                .forEach(coordinate -> {
                    setColor(Color.RED);
                    String stringPoint = String.format("%s %s",
                            alphabet.next(),
                            toPointStringFormat(coordinate.x, coordinate.y)
                    );
                    drawString(stringPoint, coordinate.x - .18, coordinate.y + .03);

                    setColor(DARKRED_COLOR);
                    fillOval(coordinate.x, coordinate.y, POINT_SIZE);

                    setColor(BLACK_COLOR);
                    drawOval(coordinate.x, coordinate.y, POINT_SIZE);
                });
    }

    private String toPointStringFormat(double x, double y) {
        return String.format("(%s,%s)",
                String.format("%.1f", x).replace(",", "."),
                String.format("%.1f", y).replace(",", ".")
        );
    }

}
