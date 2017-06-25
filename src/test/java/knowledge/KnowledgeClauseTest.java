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

public class KnowledgeClauseTest {

    private static final String EXPECTED_PREDICATE = "expectedPredicate";
    private KnowledgeClause knowledgeClause;

    @Before
    public void setUp() {
        knowledgeClause = new KnowledgeClause(EXPECTED_PREDICATE);
    }

    @Test
    public void shouldMakeHead() {
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "()."));
    }

    @Test
    public void shouldMakeBody() {
        String expectedBody1 = "expectedBody1";
        knowledgeClause.body(expectedBody1);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + "."));
    }

    @Test
    public void shouldMakeBodies() {
        String expectedBody1 = "expectedBody1";
        String expectedBody2 = "expectedBody2";
        knowledgeClause.body(expectedBody1);
        knowledgeClause.body(expectedBody2);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + ", " + expectedBody2 + "."));
    }

    @Test
    public void shouldMakeBodies2() {
        String expectedBody1 = "expectedBody1";
        String expectedBody2 = "expectedBody2";
        knowledgeClause.bodies(expectedBody1, expectedBody2);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "() :- " + expectedBody1 + ", " + expectedBody2 + "."));
    }

    @Test
    public void shouldMakeArgument() {
        String expectedArgument1 = "expectedArgument1";
        knowledgeClause.argument(expectedArgument1);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ")."));
    }

    @Test
    public void shouldMakeArguments() {
        String expectedArgument1 = "expectedArgument1";
        String expectedArgument2 = "expectedArgument2";
        knowledgeClause.argument(expectedArgument1);
        knowledgeClause.argument(expectedArgument2);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ", " + expectedArgument2 + ")."));
    }

    @Test
    public void shouldMakeArguments2() {
        String expectedArgument1 = "expectedArgument1";
        String expectedArgument2 = "expectedArgument2";
        knowledgeClause.arguments(expectedArgument1, expectedArgument2);
        assertThat(knowledgeClause.toString(), is(EXPECTED_PREDICATE + "(" + expectedArgument1 + ", " + expectedArgument2 + ")."));
    }

    @Test
    public void shouldGetKnowledge() {
        assertThat(knowledgeClause.toKnowledge().toString(), is(knowledgeClause.toString()));
    }

}