/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class EmotionalAgentArgumentsBuilderTest {

    @Test
    public void shouldSetActivationArgument() {
        List<String> arguments = new EmotionalAgentArgumentsBuilder()
                .activation(.2)
                .build();

        assertThat(arguments, contains("-a", "0.2"));
    }

    @Test
    public void shouldSetSatisfactionArgument() {
        List<String> arguments = new EmotionalAgentArgumentsBuilder()
                .satisfaction(.5)
                .build();

        assertThat(arguments, contains("-s", "0.5"));
    }

    @Test
    public void shouldSetKnowledgeArgument() {
        String expectedString = "expectedString";

        List<String> arguments = new EmotionalAgentArgumentsBuilder()
                .knowledge(expectedString)
                .build();

        assertThat(arguments, contains("-k", expectedString));
    }

    @Test
    public void shouldSetKnowledgePathArgument() {
        String expectedString = "expectedString";

        List<String> arguments = new EmotionalAgentArgumentsBuilder()
                .knowledgePath(expectedString)
                .build();

        assertThat(arguments, contains("-kp", expectedString));
    }

}