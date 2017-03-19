/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import org.junit.Test;

import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class KnowledgeTest {

    @Test
    public void shouldLoadKnowledgeFromPath() {
        Knowledge knowledge = new Knowledge(Paths.get("theories/behaviourManager.prolog"));
        assertThat(knowledge.toString(), containsString("emotionType(admiration, positive)."));
    }

    @Test
    public void shouldAddKnowledge() {
        Knowledge knowledge = new Knowledge("line1");
        knowledge.add("line2");
        assertThat(knowledge.toString(), is("line1\nline2"));
    }

}