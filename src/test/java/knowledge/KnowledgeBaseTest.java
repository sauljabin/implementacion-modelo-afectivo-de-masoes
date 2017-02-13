/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import alice.tuprolog.Theory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class KnowledgeBaseTest {

    private KnowledgeBase knowledgeBaseSpy;
    private ArgumentCaptor<Theory> theoryArgumentCaptor;

    @Before
    public void setUp() {
        theoryArgumentCaptor = ArgumentCaptor.forClass(Theory.class);
        knowledgeBaseSpy = spy(KnowledgeBase.class);
    }

    @Test
    public void shouldAddStringTheory() throws Exception {
        String expectedTheory = "expectedTheory(X).";
        knowledgeBaseSpy.addTheory(expectedTheory);
        verify(knowledgeBaseSpy).addTheory(theoryArgumentCaptor.capture());
        assertThat(theoryArgumentCaptor.getValue().toString(), is(expectedTheory));
    }

    @Test
    public void shouldAddFileTheory() throws Exception {
        String expectedTheory = "theories/behaviourManager.pl";
        knowledgeBaseSpy.addTheoryFromPath(expectedTheory);
        verify(knowledgeBaseSpy).addTheory(theoryArgumentCaptor.capture());
        assertThat(theoryArgumentCaptor.getValue().toString(), containsString("emotionType(admiration, positive)"));
    }

}