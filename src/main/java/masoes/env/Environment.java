/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import java.util.List;

public abstract class Environment {

    public abstract List<EnvironmentAgentInfo> getEnvironmentAgentInfoList();

    public abstract void setup();

}
