/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ApplicationOptionTest {

    private ApplicationOption applicationOptionA;
    private ApplicationOption applicationOptionB;

    @Before
    public void setUp() {
        applicationOptionA = createDummyApplicationOption(1, "testA", "a", null, ArgumentType.NO_ARGS);
        applicationOptionB = createDummyApplicationOption(2, null, "a", null, ArgumentType.NO_ARGS);
    }

    @Test
    public void shouldCorrectCompareObject() {
        assertThat(applicationOptionA.compareTo(applicationOptionA), is(0));
        assertThat(applicationOptionB.compareTo(applicationOptionB), is(0));
        assertThat(applicationOptionA.compareTo(applicationOptionB), is(lessThan(0)));
        assertThat(applicationOptionB.compareTo(applicationOptionA), is(greaterThan(0)));
    }

    @Test
    public void shouldGetCorrectString() {
        assertThat(applicationOptionA.toString(), is("{option=[-a,--testA], order=1}"));
    }

    @Test
    public void shouldGetCorrectKeyWhenOptIsNull() {
        assertThat(applicationOptionA.getKeyOpt(), is(applicationOptionA.getLongOpt()));
    }

    @Test
    public void shouldGetCorrectKeyLongOptIsNull() {
        assertThat(applicationOptionB.getKeyOpt(), is(applicationOptionB.getOpt()));
    }

    @Test
    public void shouldCreateCorrectLongOption() throws Exception {
        String expectedLongOpt = "long";
        ApplicationOption applicationOption = createDummyApplicationOption(0, expectedLongOpt, null, null, ArgumentType.NO_ARGS);
        Option option = Option.builder().longOpt(expectedLongOpt).build();
        assertReflectionEquals(applicationOption.toOption(), option);
    }

    @Test
    public void shouldCreateCorrectOptOption() throws Exception {
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, null, ArgumentType.NO_ARGS);
        Option option = Option.builder(expectedOpt).build();
        assertReflectionEquals(applicationOption.toOption(), option);
    }

    @Test
    public void shouldCreateCorrectOptionWithDesc() throws Exception {
        String expectedDesc = "desc";
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, expectedDesc, ArgumentType.NO_ARGS);
        Option option = Option.builder(expectedOpt).desc(expectedDesc).build();
        assertReflectionEquals(applicationOption.toOption(), option);
    }

    @Test
    public void shouldCreateCorrectOptionWithArgs() throws Exception {
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, null, ArgumentType.UNLIMITED_ARGS);
        Option option = Option.builder(expectedOpt).hasArgs().build();
        assertReflectionEquals(applicationOption.toOption(), option);
    }

    private ApplicationOption createDummyApplicationOption(int order, String longOpt, String opt, String description, ArgumentType argumentType) {
        return new ApplicationOption() {
            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public String getLongOpt() {
                return longOpt;
            }

            @Override
            public String getOpt() {
                return opt;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public ArgumentType getArgType() {
                return argumentType;
            }

            @Override
            public void exec() {

            }
        };
    }

}