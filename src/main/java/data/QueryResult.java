/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import java.sql.ResultSet;

public class QueryResult {

    private ResultSet resultSet;

    public QueryResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean next() {
        try {
            return resultSet.next();
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public String getString(String columnName) {
        try {
            return resultSet.getString(columnName);
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public String getString(int columnIndex) {
        try {
            return resultSet.getString(columnIndex);
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public void close() {
        try {
            resultSet.close();
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

}
