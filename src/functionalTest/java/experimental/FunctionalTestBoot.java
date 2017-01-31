/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental;

import application.ApplicationLogger;
import application.ApplicationSettings;
import environment.AgentParameter;
import environment.Environment;
import jade.JadeBoot;
import jade.JadeSettings;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FunctionalTestBoot {

    private static final String FUNCTIONAL_TEST_ENV = "functionalTest";
    private static final int STATUS_FAILURE = -1;
    private static final String TESTER_AGENT_NAME = "tester";
    private JadeSettings jadeSettings;
    private JadeBoot jadeBoot;
    private ApplicationLogger logger;
    private ApplicationSettings applicationSettings;

    public FunctionalTestBoot() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(FunctionalTestBoot.class));
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        jadeBoot = new JadeBoot();
    }

    public void boot(String[] args) {
        try {
            Environment environment = createEnvironment(args);
            setSettings(environment);
            logger.startingApplication(args);
            jadeBoot.boot();
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(STATUS_FAILURE);
        }
    }

    private void setSettings(Environment environment) {
        applicationSettings.set(ApplicationSettings.MASOES_ENV, environment.getEnvironmentName());
        jadeSettings.set(JadeSettings.AGENTS, environment.toJadeParameter());
        jadeSettings.set(JadeSettings.GUI, Boolean.FALSE.toString());
    }

    private Environment createEnvironment(String[] args) {
        Environment environment = new Environment();
        environment.setEnvironmentName(FUNCTIONAL_TEST_ENV);
        environment.add(new AgentParameter(TESTER_AGENT_NAME, TesterAgent.class, Arrays.asList(args)));
        return environment;
    }

}
