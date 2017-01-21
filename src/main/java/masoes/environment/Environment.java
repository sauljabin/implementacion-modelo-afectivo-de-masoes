/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.environment;

import jade.command.AgentCommandFormatter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public abstract class Environment {

    public abstract List<AgentCommandFormatter> getAgentCommands();

    public String getEnvironmentName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getEnvironmentName())
                .toString();
    }

}
