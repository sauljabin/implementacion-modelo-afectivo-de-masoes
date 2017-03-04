/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import java.io.InputStream;
import java.nio.file.Path;

public class Knowledge {

    private String knowledge;

    public Knowledge(Path path) {
        try {
            InputStream stream = ClassLoader.getSystemResourceAsStream(path.toString());
            byte[] info = new byte[stream.available()];
            stream.read(info);
            knowledge = new String(info);
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    public Knowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    @Override
    public String toString() {
        return knowledge;
    }

}
