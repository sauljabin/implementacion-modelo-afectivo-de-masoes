/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment;

import masoes.environment.dummy.DummyEnvironment;
import masoes.exception.InvalidEnvironmentException;
import masoes.environment.wikipedia.WikipediaEnvironment;
import settings.application.ApplicationSettings;

public class EnvironmentFactory {

    private ApplicationSettings applicationSettings;

    public EnvironmentFactory() {
        applicationSettings = ApplicationSettings.getInstance();
    }

    public Environment createEnvironment() {
        if (isContains(DummyEnvironment.class)) {
            return new DummyEnvironment();
        } else if (isContains(WikipediaEnvironment.class)) {
            return new WikipediaEnvironment();
        } else {
            throw new InvalidEnvironmentException(String.format("Invalid environment name \"%s\"", applicationSettings.get(ApplicationSettings.MASOES_ENV)));
        }
    }

    private boolean isContains(Class<? extends Environment> environmentClass) {
        return environmentClass.getSimpleName().toLowerCase().contains(applicationSettings.get(ApplicationSettings.MASOES_ENV).toLowerCase());
    }

}
