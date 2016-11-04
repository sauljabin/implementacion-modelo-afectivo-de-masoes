/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class EmotionTest {

    private Emotion mockEmotion;
    private String expectedEmotionName;

    @Before
    public void setUp() throws Exception {
        mockEmotion = mock(Emotion.class);
        expectedEmotionName = "EmotionName";
        doReturn(expectedEmotionName).when(mockEmotion).getName();
        doCallRealMethod().when(mockEmotion).toString();
    }

    @Test
    public void shouldInvokeNameWhenToString() {
        assertThat(mockEmotion.toString(), is(expectedEmotionName));
    }

}