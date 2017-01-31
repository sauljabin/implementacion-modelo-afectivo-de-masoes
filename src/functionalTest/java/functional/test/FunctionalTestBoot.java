/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

import application.ApplicationLogger;
import application.ApplicationSettings;
import environment.AgentParameter;
import jade.JadeBoot;
import jade.JadeSettings;
import org.slf4j.LoggerFactory;

public class FunctionalTestBoot {

    private static final String FUNCTIONAL_TEST_ENV = "functionalTest";
    private static final int STATUS_FAILURE = -1;
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
            setSettings();
            logger.startingApplication(args);
            jadeBoot.boot();
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(STATUS_FAILURE);
        }
    }

    private void setSettings() {
        applicationSettings.set(ApplicationSettings.MASOES_ENV, FUNCTIONAL_TEST_ENV);
        jadeSettings.set(JadeSettings.AGENTS, getAgentParameter());
        jadeSettings.set(JadeSettings.GUI, Boolean.FALSE.toString());
    }

    private String getAgentParameter() {
        return new AgentParameter(FunctionalTesterAgent.class.getSimpleName(), FunctionalTesterAgent.class).toJadeParameter();
    }

}
