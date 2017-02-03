/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental;

import agent.AgentLogger;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TesterAgent extends Agent {

    private static final int FAILURE = -1;
    private AgentLogger logger = new AgentLogger(LoggerFactory.getLogger(TesterAgent.class));

    @Override
    protected void setup() {
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
        Optional<Object[]> optionalArguments = Optional.ofNullable(getArguments());
        if (optionalArguments.isPresent() && optionalArguments.get().length > 0) {
            return Arrays.asList(optionalArguments.get()).stream().map(arg -> {
                String className = (String) arg;
                try {
                    return Class.forName(className);
                } catch (Exception e) {
                    throw new FunctionalTestException("Error adding FunctionalTest behaviour", e);
                }
            }).collect(Collectors.toSet());
        } else {
            Reflections reflections = new Reflections("experimental");
            return reflections.getSubTypesOf(FunctionalTest.class).stream().collect(Collectors.toSet());
        }
    }

    @Override
    protected void takeDown() {
        new Thread(() -> {
            try {
                getContainerController().kill();
            } catch (Exception e) {
                logger.exception(this, e);
                System.exit(FAILURE);
            }
        }).start();
    }

}
