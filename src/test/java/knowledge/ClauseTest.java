/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ClauseTest {

    private static final String EXPECTED_PREDICATE = "expectedPredicate";
    private Clause clause;

    @Before
    public void setUp() {
        clause = new Clause(EXPECTED_PREDICATE);
    }

    @Test
    public void shouldMakeHead() {
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "()."));
    }

    @Test
    public void shouldMakeBody() {
        String expectedBody1 = "expectedBody1";
        clause.body(expectedBody1);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + "."));
    }

    @Test
    public void shouldMakeBodies() {
        String expectedBody1 = "expectedBody1";
        String expectedBody2 = "expectedBody2";
        clause.body(expectedBody1);
        clause.body(expectedBody2);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + ", " + expectedBody2 + "."));
    }

    @Test
    public void shouldMakeBodies2() {
        String expectedBody1 = "expectedBody1";
        String expectedBody2 = "expectedBody2";
        clause.bodies(expectedBody1, expectedBody2);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + ", " + expectedBody2 + "."));
    }

    @Test
    public void shouldMakeArgument() {
        String expectedArgument1 = "expectedArgument1";
        clause.argument(expectedArgument1);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ")."));
    }

    @Test
    public void shouldMakeArguments() {
        String expectedArgument1 = "expectedArgument1";
        String expectedArgument2 = "expectedArgument2";
        clause.argument(expectedArgument1);
        clause.argument(expectedArgument2);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ", " + expectedArgument2 + ")."));
    }

    @Test
    public void shouldMakeArguments2() {
        String expectedArgument1 = "expectedArgument1";
        String expectedArgument2 = "expectedArgument2";
        clause.arguments(expectedArgument1, expectedArgument2);
        assertThat(clause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ", " + expectedArgument2 + ")."));
    }

    @Test
    public void shouldGetKnowledge() {
        assertThat(clause.toKnowledge().toString(), is(clause.toString()));
    }

}