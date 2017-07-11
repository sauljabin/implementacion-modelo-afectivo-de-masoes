/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import java.util.ArrayList;
import java.util.List;

public class EmotionalAgentArgumentsBuilder {

    private Double activation;
    private Double satisfaction;
    private String knowledgePath;
    private String knowledge;

    public List<String> build() {
        ArrayList<String> arguments = new ArrayList<>();

        if (activation != null) {
            arguments.add("-a");
            arguments.add(activation.toString());
        }

        if (satisfaction != null) {
            arguments.add("-s");
            arguments.add(satisfaction.toString());
        }

        if (knowledgePath != null) {
            arguments.add("-kp");
            arguments.add(knowledgePath);
        }

        if (knowledge != null) {
            arguments.add("-k");
            arguments.add(knowledge);
        }

        return arguments;
    }

    public EmotionalAgentArgumentsBuilder activation(double activation) {
        this.activation = activation;
        return this;
    }

    public EmotionalAgentArgumentsBuilder satisfaction(double satisfaction) {
        this.satisfaction = satisfaction;
        return this;
    }

    public EmotionalAgentArgumentsBuilder knowledgePath(String knowledgePath) {
        this.knowledgePath = knowledgePath;
        return this;
    }

    public EmotionalAgentArgumentsBuilder knowledge(String knowledge) {
        this.knowledge = knowledge;
        return this;
    }

}
