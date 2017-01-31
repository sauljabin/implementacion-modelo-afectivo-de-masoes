/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import environment.dummy.DummyEnvironment;
import environment.wikipedia.WikipediaEnvironment;

public class EnvironmentFactory {

    public Environment createEnvironment(String environment) {
        if (isContains(DummyEnvironment.class, environment)) {
            return new DummyEnvironment();
        } else if (isContains(WikipediaEnvironment.class, environment)) {
            return new WikipediaEnvironment();
        } else {
            throw new InvalidEnvironmentException(String.format("Invalid environment name \"%s\"", environment));
        }
    }

    private boolean isContains(Class<? extends Environment> environmentClass, String environment) {
        return environmentClass.getSimpleName().toLowerCase().contains(environment.toLowerCase());
    }

}
