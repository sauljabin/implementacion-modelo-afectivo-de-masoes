/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.core;

import jade.core.AID;
import jade.core.Agent;
import junit.framework.AssertionFailedError;
import logger.jade.JadeLogger;
import org.slf4j.LoggerFactory;
import org.unitils.reflectionassert.ReflectionAssert;
import string.random.StringGenerator;
import test.common.Test;
import test.common.TestException;
import test.common.TestUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionalTest extends Test {

    protected static final int TIMEOUT_DEFAULT = 6000;
    private static final int RANDOM_STRING_LENGTH = 20;
    private static final int SLEEP_KILL_AGENT_MILLIS = 100;
    private List<AID> agentsToKill;
    private boolean hasError;
    private JadeLogger logger;
    private StringGenerator stringGenerator;

    public FunctionalTest() {
        hasError = false;
        agentsToKill = new ArrayList<>();
        logger = new JadeLogger(LoggerFactory.getLogger(FunctionalTest.class));
        stringGenerator = new StringGenerator();
    }

    @Override
    public void clean(Agent agent) {
        if (agent instanceof FunctionalTesterAgent && hasError) {
            ((FunctionalTesterAgent) agent).setHasError();
        }
        agentsToKill.forEach(aid -> {
            try {
                TestUtility.killAgent(agent, aid);
                Thread.sleep(SLEEP_KILL_AGENT_MILLIS);
            } catch (Exception e) {
                logger.exception(agent, e);
            }
        });
    }

    public void assertNotNull(String message, Object actual) {
        if (Optional.ofNullable(actual).isPresent()) {
            assertPass(String.format("Assert not null: %s", message));
        } else {
            assertError(String.format("Assertion error: Expected not null for %s and was null", message));
        }
    }

    public void assertEquals(String message, Object expected, Object actual) {
        if (actual.equals(expected)) {
            assertPass(String.format("Assert equals: %s", message));
        } else {
            assertError(String.format("Assertion error: %s\nExpected: %s \n and was: %s", message, expected, actual));
        }
    }

    public void assertReflectionEquals(String message, Object expected, Object actual) {
        try {
            ReflectionAssert.assertReflectionEquals(expected, actual);
            assertPass(String.format("Assert equals: %s", message));
        } catch (AssertionFailedError e) {
            assertError(String.format("Assertion error: %s\n%s", message, e.getMessage()));
        }
    }

    public void assertReflectionNotEquals(String message, Object expected, Object actual) {
        try {
            ReflectionAssert.assertReflectionEquals(expected, actual);
            assertError(String.format("Assertion error: Expected different to %s and was equal", message));
        } catch (AssertionFailedError e) {
            assertPass(String.format("Assert not equals: %s", message));
        }
    }

    public void assertNotEquals(String message, Object first, Object second) {
        if (!first.equals(second)) {
            assertPass(String.format("Assert not equals: %s", message));
        } else {
            assertError(String.format("Assertion error: Expected different to %s and was equal", message));
        }
    }

    private void assertPass(String message) {
        if (!hasError) {
            passed(message);
        }
    }

    private void assertError(String message) {
        if (!hasError) {
            hasError = true;
            failed(message);
        }
    }

    public AID createAgent(Agent tester, String agentClass) throws TestException {
        AID aid = TestUtility.createAgent(tester, stringGenerator.getString(RANDOM_STRING_LENGTH), agentClass, null);
        agentsToKill.add(aid);
        return aid;
    }

    public AID createAgent(Agent tester) throws TestException {
        AID aid = TestUtility.createAgent(tester, stringGenerator.getString(RANDOM_STRING_LENGTH), TestUtility.CONFIGURABLE_AGENT, null);
        agentsToKill.add(aid);
        return aid;
    }

    public AID createAgent(Agent tester, String[] parameters) throws TestException {
        AID aid = TestUtility.createAgent(tester, stringGenerator.getString(RANDOM_STRING_LENGTH), TestUtility.CONFIGURABLE_AGENT, parameters);
        agentsToKill.add(aid);
        return aid;
    }

    public void addBehaviour(Agent tester, AID agent, String behaviourClass) throws TestException {
        TestUtility.addBehaviour(tester, agent, behaviourClass);
    }

    public JadeLogger getLogger() {
        return logger;
    }

}
