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

public class FunctionalTest extends Test {

    public static final int RANDOM_STRING_LENGTH = 10;
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
            ((FunctionalTesterAgent) agent).hasError();
        }
        agentsToKill.forEach(aid -> {
            try {
                TestUtility.killAgent(agent, aid);
                Thread.sleep(500);
            } catch (Exception e) {
                logger.agentException(agent, e);
            }
        });
    }

    public void assertEquals(String value, Object expected, Object actual) {
        if (actual.equals(expected)) {
            assertPass(String.format("Assert equals: %s", value));
        } else {
            assertError(String.format("Assert equals: %s\nExpected: %s \n and was: %s", value, expected, actual));
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

}
