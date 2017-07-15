/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TextFileWriter {

    private File folder;
    private File file;
    private BufferedWriter output;

    public TextFileWriter(String path, String fileName) {
        this(path, fileName, false);
    }

    public TextFileWriter(String path, String fileName, boolean append) {
        folder = new File(path);
        folder.mkdirs();

        file = new File(folder, fileName);

        try {
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void append(String string) {
        try {
            output.write(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void append(String format, Object... args) {
        append(String.format(format, args));
    }

    public void appendln(String string) {
        try {
            output.write(string);
            output.write("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void appendln(String format, Object... args) {
        appendln(String.format(format, args));
    }

    public void newLine() {
        try {
            output.write("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void newLine(int count) {
        for (int i = 0; i < count; i++) {
            newLine();
        }
    }

    public void close() {
        try {
            output.flush();
            output.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
