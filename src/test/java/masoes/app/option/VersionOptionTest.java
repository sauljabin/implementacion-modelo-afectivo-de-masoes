/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class VersionOptionTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    private VersionOption versionOption;

    @Before
    public void setUp() {
        versionOption = new VersionOption();
    }

    @Test
    public void shouldGetShortCommand() {
        assertThat(versionOption.getOpt(), is("v"));
    }

    @Test
    public void shouldGetLongCommand() {
        assertThat(versionOption.getLongOpt(), is("version"));
    }

    @Test
    public void shouldGetDescriptionCommand() {
        assertThat(versionOption.getDescription(), is("Shows the application version"));
    }

    @Test
    public void shouldGetHasArgsCommand() {
        assertFalse(versionOption.hasArg());
    }

    @Test
    public void shouldGetFirstOrder() {
        assertThat(versionOption.getOrder(), is(1));
    }

    @Test
    public void shouldPrintVersion() {
        String expectedStringMasoes = "APPNAME\nVersion: 1\nRevision: 1";
        String expectedStringJade = String.format("JADE\nVersion: %s\nRevision: %s\n", jade.core.Runtime.getVersion(), jade.core.Runtime.getRevision());
        versionOption.exec("");
        assertThat(systemOutRule.getLog(), containsString(expectedStringMasoes));
        assertThat(systemOutRule.getLog(), containsString(expectedStringJade));
    }


}