/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import application.ApplicationLogger;
import jade.wrapper.PlatformController;
import jade.wrapper.PlatformEvent;

public class JadePlatformListener implements PlatformController.Listener {

    private ApplicationLogger applicationLogger;

    public JadePlatformListener() {
        applicationLogger = new ApplicationLogger(this);
    }

    @Override
    public void bornAgent(PlatformEvent platformEvent) {

    }

    @Override
    public void deadAgent(PlatformEvent platformEvent) {

    }

    @Override
    public void startedPlatform(PlatformEvent platformEvent) {

    }

    @Override
    public void suspendedPlatform(PlatformEvent platformEvent) {

    }

    @Override
    public void resumedPlatform(PlatformEvent platformEvent) {

    }

    @Override
    public void killedPlatform(PlatformEvent platformEvent) {
        applicationLogger.closingApplication();
    }

}
