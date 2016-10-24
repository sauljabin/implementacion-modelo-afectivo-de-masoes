/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import masoes.app.setting.Setting;
import masoes.env.dummy.DummyEnvironment;
import masoes.env.wikipedia.WikipediaEnvironment;

public class EnvironmentFactory {

    public Environment createEnvironment() {
        if (isContains(DummyEnvironment.class)) {
            return new DummyEnvironment();
        } else if (isContains(WikipediaEnvironment.class)) {
            return new WikipediaEnvironment();
        } else {
            throw new InvalidEnvironmentException("Invalid environment name: " + Setting.MASOES_ENV.getValue());
        }
    }

    private boolean isContains(Class<? extends Environment> environmentClass) {
        return environmentClass.getSimpleName().toLowerCase().contains(Setting.MASOES_ENV.getValue().toLowerCase());
    }

}
