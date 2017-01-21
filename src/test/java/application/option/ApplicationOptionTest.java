/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

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
        applicationOptionA = createDummyApplicationOption(1, "testA", "a", null, ArgumentType.NO_ARGS, false);
        applicationOptionB = createDummyApplicationOption(2, null, "a", null, ArgumentType.NO_ARGS, false);
    }

    @Test
    public void shouldCorrectCompareObject() {
        assertThat(applicationOptionA.compareTo(applicationOptionA), is(0));
        assertThat(applicationOptionB.compareTo(applicationOptionB), is(0));
        assertThat(applicationOptionA.compareTo(applicationOptionB), is(lessThan(0)));
        assertThat(applicationOptionB.compareTo(applicationOptionA), is(greaterThan(0)));
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
    public void shouldCreateCorrectLongOption() {
        String expectedLongOpt = "long";
        ApplicationOption applicationOption = createDummyApplicationOption(0, expectedLongOpt, null, null, ArgumentType.NO_ARGS, false);
        Option expectedOption = Option.builder().longOpt(expectedLongOpt).build();
        assertReflectionEquals(expectedOption, applicationOption.toOption());
    }

    @Test
    public void shouldCreateCorrectOptOption() {
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, null, ArgumentType.NO_ARGS, false);
        Option expectedOption = Option.builder(expectedOpt).build();
        assertReflectionEquals(expectedOption, applicationOption.toOption());
    }

    @Test
    public void shouldCreateCorrectOptionWithDesc() {
        String expectedDesc = "desc";
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, expectedDesc, ArgumentType.NO_ARGS, false);
        Option expectedOption = Option.builder(expectedOpt).desc(expectedDesc).build();
        assertReflectionEquals(expectedOption, applicationOption.toOption());
    }

    @Test
    public void shouldCreateCorrectOptionWithArgs() {
        String expectedOpt = "opt";
        ApplicationOption applicationOption = createDummyApplicationOption(0, null, expectedOpt, null, ArgumentType.UNLIMITED_ARGS, false);
        Option expectedOption = Option.builder(expectedOpt).hasArgs().build();
        assertReflectionEquals(expectedOption, applicationOption.toOption());
    }

    private ApplicationOption createDummyApplicationOption(int order, String longOpt, String opt, String description, ArgumentType argumentType, boolean isStopApplication) {
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
            public boolean isFinalOption() {
                return isStopApplication;
            }

            @Override
            public void exec() {

            }
        };
    }

}