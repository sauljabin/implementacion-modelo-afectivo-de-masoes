/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.component;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static test.ReflectionTestUtils.setFieldValue;


public class EmotionalSpaceChartTest {

    private EmotionalSpaceChart emotionalSpaceChart;
    private Thread threadMock;
    private EmotionalSpaceChart emotionalSpaceChartSpy;
    private Graphics graphicsMock;
    private BufferedImage bufferedImageMock;
    private Graphics2D graphics2DMock;

    @Before
    public void setUp() throws Exception {
        emotionalSpaceChart = new EmotionalSpaceChart();

        threadMock = mock(Thread.class);
        graphicsMock = mock(Graphics.class);
        bufferedImageMock = mock(BufferedImage.class);
        graphics2DMock = mock(Graphics2D.class);

        setFieldValue(emotionalSpaceChart, "thread", threadMock);
        setFieldValue(emotionalSpaceChart, "image", bufferedImageMock);
        setFieldValue(emotionalSpaceChart, "graphics", graphics2DMock);

        emotionalSpaceChartSpy = spy(emotionalSpaceChart);
        doNothing().when(emotionalSpaceChartSpy).makeImage();
        doReturn(graphicsMock).when(emotionalSpaceChartSpy).getGraphics();
    }

    @Test
    public void shouldRenderImage() {
        emotionalSpaceChartSpy.render();
        verify(graphicsMock).drawImage(bufferedImageMock, 0, 0, emotionalSpaceChartSpy);
    }

    @Test
    public void shouldStartThread() {
        emotionalSpaceChartSpy.start();
        verify(threadMock).start();
    }

    @Test
    public void shouldStopThread() throws Exception {
        emotionalSpaceChartSpy.stop();
        verify(threadMock).join(anyLong());
    }

}