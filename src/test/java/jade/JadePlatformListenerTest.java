/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import application.ApplicationLogger;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static test.ReflectionTestUtils.setFieldValue;

public class JadePlatformListenerTest {

    private JadePlatformListener jadePlatformListener;
    private ApplicationLogger applicationLoggerMock;

    @Before
    public void setUp() throws Exception {
        jadePlatformListener = new JadePlatformListener();
        applicationLoggerMock = mock(ApplicationLogger.class);
        setFieldValue(jadePlatformListener, "applicationLogger", applicationLoggerMock);
    }

    @Test
    public void shouldInvokeClosingApplication() {
        jadePlatformListener.killedPlatform(null);
        verify(applicationLoggerMock).closingApplication();
    }

}