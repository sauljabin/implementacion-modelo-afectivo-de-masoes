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

    private static final String HAPPINESS = "happiness";
    private static final String JOY = "joy";
    private static final String COMPASSION = "compassion";
    private static final String ADMIRATION = "admiration";
    private static final String DEPRESSION = "depression";
    private static final String SADNESS = "sadness";
    private static final String REJECTION = "rejection";
    private static final String ANGER = "anger";

    private static final int SIZE = 400;
    private static final int PADDING = 50;
    private static final int POINT_SIZE = 8;
    private static final int SIZE_PADDING = SIZE + PADDING * 2;

    private static final String OUTPUT_OTHERS = "output/others";
    private static final String PNG = "png";

    private static final Color BLACK_COLOR = Color.BLACK;
    private static final Color DARK_RED_COLOR = new Color(203, 71, 79);
    private static final Color BLUE_COLOR = new Color(158, 230, 237);
    private static final Color RED_COLOR = Color.RED;
    private static final Color WHITE_COLOR = Color.WHITE;

    private static final Font FONT_12 = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_10 = new Font("Arial", Font.PLAIN, 10);
    private static final BasicStroke STROKE = new BasicStroke(1.1f);

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
                    renderEmotionName(emotion);
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
        graphics.setBackground(WHITE_COLOR);
        graphics.clearRect(0, 0, SIZE_PADDING, SIZE_PADDING);
        graphics.setStroke(STROKE);

        setColor(BLACK_COLOR);
        setFont(FONT_12);

        drawLine(-1, 0, 1, 0);
        drawLine(0, -1, 0, 1);

        drawString("-1", -1.1, -.02);
        drawString("1", 1.1, -.02);

        drawString("-1", -.02, -1.1);
        drawString("1", -.02, 1.1);
    }

    private void writeImage(Emotion emotion) {
        File file = new File(OUTPUT_OTHERS, emotionToFileName(emotion));
        try {
            ImageIO.write(image, PNG, file);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    private String emotionToFileName(Emotion emotion) {
        String fileName = String.format("poligono-%s.%s", getEmotionTranslation(emotion.getName()), PNG).toLowerCase();
        return fileName.replace('ó', 'o');
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

    private void setFont(Font font) {
        graphics.setFont(font);
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

    private String getEmotionTranslation(String emotionName) {
        return translation.get(emotionName.toLowerCase()).toUpperCase();
    }

    private Polygon emotionToPolygon(Emotion emotion) {
        Polygon polygon = new Polygon();
        Arrays.stream(emotion.getCoordinates())
                .collect(Collectors.toSet())
                .forEach(coordinate -> polygon.addPoint(xCanvas(coordinate.x), yCanvas(coordinate.y)));
        return polygon;
    }

    private void drawEmotion(Emotion emotion) {
        Polygon emotionToPolygon = emotionToPolygon(emotion);

        setColor(BLUE_COLOR);
        fillPolygon(emotionToPolygon);

        setColor(BLACK_COLOR);
        drawPolygon(emotionToPolygon);

        Alphabet alphabet = new Alphabet();

        Arrays.stream(emotion.getCoordinates())
                .collect(Collectors.toSet())
                .forEach(coordinate -> {
                    setColor(RED_COLOR);
                    String stringPoint = String.format("%s %s",
                            alphabet.next(),
                            toPointStringFormat(coordinate.x, coordinate.y)
                    );
                    drawString(stringPoint, coordinate.x - .18, coordinate.y + .03);

                    setColor(DARK_RED_COLOR);
                    fillOval(coordinate.x, coordinate.y, POINT_SIZE);

                    setColor(BLACK_COLOR);
                    drawOval(coordinate.x, coordinate.y, POINT_SIZE);
                });
    }

    private void renderEmotionName(Emotion emotion) {
        setFont(FONT_10);
        setColor(BLACK_COLOR);

        switch (emotion.getName().toLowerCase().trim()) {
            case COMPASSION:
                drawString(getEmotionTranslation(COMPASSION), -.8, .7);
                break;
            case HAPPINESS:
                drawString(getEmotionTranslation(HAPPINESS), .5, .7);
                break;
            case ANGER:
                drawString(getEmotionTranslation(ANGER), .5, -.8);
                break;
            case DEPRESSION:
                drawString(getEmotionTranslation(DEPRESSION), -.8, -.8);
                break;
            case ADMIRATION:
                drawString(getEmotionTranslation(ADMIRATION), -.42, .24);
                break;
            case JOY:
                drawString(getEmotionTranslation(JOY), .14, .24);
                break;
            case SADNESS:
                drawString(getEmotionTranslation(SADNESS), -.36, -.26);
                break;
            case REJECTION:
                drawString(getEmotionTranslation(REJECTION), .13, -.26);
                break;
        }
    }

    private String toPointStringFormat(double x, double y) {
        return String.format("(%s,%s)",
                String.format("%.1f", x).replace(",", "."),
                String.format("%.1f", y).replace(",", ".")
        );
    }

}
