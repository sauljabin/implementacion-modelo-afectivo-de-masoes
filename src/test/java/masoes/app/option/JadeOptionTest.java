/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(jade.Boot.class)
public class JadeOptionTest {

    private JadeOption jadeOption;

    @Before
    public void setUp() {
        jadeOption = new JadeOption();
    }

    @Test
    public void shouldGetShortCommand() {
        assertThat(jadeOption.getOpt(), nullValue());
    }

    @Test
    public void shouldGetLongCommand() {
        assertThat(jadeOption.getLongOpt(), is("jade"));
    }

    @Test
    public void shouldGetDescriptionCommand() {
        assertThat(jadeOption.getDescription(), containsString("Starts JADE framework with arguments"));
    }

    @Test
    public void shouldGetHasArgsCommand() {
        assertTrue(jadeOption.hasArg());
    }

    @Test
    public void shouldGetThirdOrder() {
        assertThat(jadeOption.getOrder(), is(3));
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
        PowerMockito.mockStatic(jade.Boot.class);
        jadeOption.exec(stringArgs);
        PowerMockito.verifyStatic();
        jade.Boot.main(expectedSplitArguments);
    }

}