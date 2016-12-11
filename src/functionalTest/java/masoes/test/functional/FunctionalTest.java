/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional;

import jade.core.AID;
import jade.core.Agent;
import masoes.app.logger.ApplicationLogger;
import masoes.util.string.StringGenerator;
import org.slf4j.LoggerFactory;
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
    private ApplicationLogger logger;
    private StringGenerator stringGenerator;

    public FunctionalTest() {
        hasError = false;
        agentsToKill = new ArrayList<>();
        logger = new ApplicationLogger(LoggerFactory.getLogger(FunctionalTest.class));
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
                logger.agentException(agent, e);
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

    public ApplicationLogger getLogger() {
        return logger;
    }

}
