/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import java.util.ArrayList;
import java.util.List;

public class DummyEmotionalAgentArgumentsBuilder {

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

    public DummyEmotionalAgentArgumentsBuilder activation(double activation) {
        this.activation = activation;
        return this;
    }

    public DummyEmotionalAgentArgumentsBuilder satisfaction(double satisfaction) {
        this.satisfaction = satisfaction;
        return this;
    }

    public DummyEmotionalAgentArgumentsBuilder knowledgePath(String knowledgePath) {
        this.knowledgePath = knowledgePath;
        return this;
    }

    public DummyEmotionalAgentArgumentsBuilder knowledge(String knowledge) {
        this.knowledge = knowledge;
        return this;
    }

}
