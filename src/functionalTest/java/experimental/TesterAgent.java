/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental;

import jade.JadeLogger;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.lang.acl.ACLMessage;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TesterAgent extends Agent {

    private JadeLogger logger;

    @Override
    protected void setup() {
        logger = new JadeLogger(LoggerFactory.getLogger(FunctionalTest.class));
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec(0));

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
        ContainerID container = (ContainerID) here();
        KillContainer killContainer = new KillContainer();
        killContainer.setContainer(container);

        try {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setSender(getAID());
            message.addReceiver(getAMS());
            message.setOntology(JADEManagementOntology.NAME);
            message.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
            getContentManager().fillContent(message, new Action(getAMS(), killContainer));
            send(message);
        } catch (Exception e) {
            logger.exception(this, e);
            throw new FunctionalTestException(String.format("Error killing container \"%s\"", container), e);
        }
    }

}
