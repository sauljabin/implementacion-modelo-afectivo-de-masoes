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

import java.nio.file.Path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class KnowledgeBaseTest {

    private KnowledgeBase knowledgeBaseSpy;
    private ArgumentCaptor<Theory> theoryArgumentCaptor;

    @Before
    public void setUp() {
        theoryArgumentCaptor = ArgumentCaptor.forClass(Theory.class);
        knowledgeBaseSpy = spy(KnowledgeBase.class);
        doNothing().when(knowledgeBaseSpy).addTheory(any(Theory.class));
        doNothing().when(knowledgeBaseSpy).addTheory(any(Path.class));
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
        knowledgeBaseSpy.addTheory("testTheory.prolog");
        verify(knowledgeBaseSpy).addTheory(any(Theory.class));
    }

    @Test
    public void shouldAddKnowledge() throws Exception {
        String expectedTheory = "expectedTheory(X).";
        Knowledge knowledgeMock = mock(Knowledge.class);
        doReturn(expectedTheory).when(knowledgeMock).toString();
        knowledgeBaseSpy.addKnowledge(knowledgeMock);
        verify(knowledgeBaseSpy).addTheory(any(Theory.class));
    }

}