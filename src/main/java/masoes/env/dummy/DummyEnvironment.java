/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.env.Environment;
import masoes.env.EnvironmentAgentInfo;

import java.util.List;

public class DummyEnvironment extends Environment {

    @Override
    public List<EnvironmentAgentInfo> getEnvironmentAgentInfoList() {
        return null;
    }
}
