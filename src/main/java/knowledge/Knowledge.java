/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import java.io.InputStream;
import java.nio.file.Path;

public class Knowledge {

    private StringBuilder knowledge;

    public Knowledge() {
        knowledge = new StringBuilder();
    }

    public Knowledge(Path knowledgePath) {
        this();
        add(knowledgePath);
    }

    public Knowledge(String knowledgeString) {
        this();
        add(knowledgeString);
    }

    public void add(String knowledgeString) {
        knowledge.append(knowledgeString);
        knowledge.append("\n");
    }

    public void add(Path knowledgePath) {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(knowledgePath.toString());
            byte[] info = new byte[stream.available()];
            stream.read(info);
            knowledge.append(new String(info));
            knowledge.append("\n");
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    @Override
    public String toString() {
        return knowledge.toString().trim();
    }

}
