/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

import agent.AgentLogger;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class TesterAgent extends Agent {

    private static final int FAILURE = 1;
    private AgentLogger logger = new AgentLogger(LoggerFactory.getLogger(TesterAgent.class));
    private int totalTests;
    private int failedTests;

    @Override
    protected void setup() {
        totalTests = 0;
        failedTests = 0;
        try {
            loadTests();
        } catch (Exception e) {
            logException(e);
            doDelete();
        }
    }

    private void logException(Exception e) {
        log("");
        logger.exception(this, e);
        log("");
    }

    public void log(String message) {
        System.out.println(message);
    }

    private void loadTests() {
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour(this);

        Set<Class<?>> subTypes = getTestClasses();

        subTypes.forEach(behaviourClass -> {
            try {
                sequentialBehaviour.addSubBehaviour((Behaviour) behaviourClass.newInstance());
            } catch (Exception e) {
                throw new FunctionalTestException("Error adding FunctionalTest behaviour", e);
            }
        });

        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                doDelete();
            }
        });

        addBehaviour(sequentialBehaviour);
    }

    private Set<Class<?>> getTestClasses() {
        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<?>> classList = reflections.getSubTypesOf(FunctionalTest.class).stream().collect(Collectors.toSet());
        Set<String> args = Arrays.asList(getArguments()).stream().filter(arg -> !arg.toString().isEmpty()).map(Object::toString).collect(Collectors.toSet());

        if (args.isEmpty()) {
            return classList;
        }

        String pattern = String.join("|", args).replace(".", "\\.").replace("*", ".*");

        return classList.stream().filter(aClass -> aClass.getName().matches(pattern)).collect(Collectors.toSet());
    }

    public void registerTest(boolean passed) {
        totalTests++;
        if (!passed) {
            failedTests++;
        }
    }

    @Override
    protected void takeDown() {
        log("");
        if (failedTests > 0) {
            log(String.format("%d tests completed, %d failed", totalTests, failedTests));
            log("");
        }

        new Thread(() -> {
            try {
                if (failedTests > 0) {
                    System.exit(FAILURE);
                }
                getContainerController().kill();
            } catch (Exception e) {
                logException(e);
                System.exit(FAILURE);
            }
        }).start();
    }

}
