/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package test;

import data.DataBaseConnection;
import data.DataBaseSettings;
import org.flywaydb.core.Flyway;

import java.io.File;

public final class PhoenixDatabase {

    private static final String DB_FILE = "data/test.sqlite3";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    private PhoenixDatabase() {
    }

    public static DataBaseConnection create() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(DB_URL, null, null);
        flyway.migrate();
        DataBaseSettings.getInstance().set(DataBaseSettings.URL, DB_URL);
        DataBaseConnection connection = DataBaseConnection.getConnection();
        connection.connect(true);
        return connection;
    }

    public static void destroy() {
        DataBaseConnection.getConnection().close();
        DataBaseSettings.getInstance().load();
        File file = new File(DB_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

}
