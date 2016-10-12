/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JadeOptionTest {

    private JadeOption jadeOption;

    @Before
    public void setUp() {
        jadeOption = new JadeOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(jadeOption.getOpt(), is("j"));
        assertThat(jadeOption.getLongOpt(), is("jade"));
        assertThat(jadeOption.getDescription(), containsString("Starts JADE framework with arguments"));
        assertTrue(jadeOption.hasArg());
        assertThat(jadeOption.getOrder(), is(40));
    }

    @Test
    public void shouldInvokeStartJade() throws Exception {
        verifyInvokeJade("", "".split(""));
    }

    @Test
    public void shouldSplitStringToJadeFormat() {
        String stringArgs = "-gui -agents a1:agentClass";
        String[] expectedSplitArguments = {"-gui", "-agents", "a1:agentClass"};
        verifyInvokeJade(stringArgs, expectedSplitArguments);
    }

    @Test
    public void shouldSplitStringToJadeFormatAndAgentArguments() {
        String stringArgs = "-gui -agents a1:agentClass(arg1,arg2,arg3 and arg4)";
        String[] expectedSplitArguments = {"-gui", "-agents", "a1:agentClass(arg1,arg2,arg3 and arg4)"};
        verifyInvokeJade(stringArgs, expectedSplitArguments);
    }

    @Test
    public void shouldSplitStringToJadeFormatAgentArgumentsAndGui() {
        String stringArgs = "-agents a1:agentClass(arg1,arg2,arg3 and arg4) -gui";
        String[] expectedSplitArguments = {"-agents", "a1:agentClass(arg1,arg2,arg3 and arg4)", "-gui"};
        verifyInvokeJade(stringArgs, expectedSplitArguments);
    }

    @Test
    public void shouldSplitStringToJadeFormatAndMultipleAgents() {
        String stringArgs = "-gui -agents a1:agentClass(arg1,arg2,arg3 and arg4);a2:agentClass(arg1,arg2,arg3 and arg4)";
        String[] expectedSplitArguments = {"-gui", "-agents", "a1:agentClass(arg1,arg2,arg3 and arg4);a2:agentClass(arg1,arg2,arg3 and arg4)"};
        verifyInvokeJade(stringArgs, expectedSplitArguments);
    }

    private void verifyInvokeJade(String stringArgs, String[] expectedSplitArguments) {
        fail();
    }

}