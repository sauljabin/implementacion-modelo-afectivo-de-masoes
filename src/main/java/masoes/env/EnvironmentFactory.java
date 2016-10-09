/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import masoes.app.setting.Setting;
import masoes.env.generic.GenericEnvironment;

public class EnvironmentFactory {

    public Environment createEnvironment() {
        if (isContains(GenericEnvironment.class)) {
            return new GenericEnvironment();
        } else {
            throw new InvalidEnvironmentException("Invalid environment name: " + Setting.MASOES_ENV.getValue());
        }
    }

    private boolean isContains(Class<GenericEnvironment> environmentClass) {
        return environmentClass.getSimpleName().toLowerCase().contains(Setting.MASOES_ENV.getValue().toLowerCase());
    }

}
