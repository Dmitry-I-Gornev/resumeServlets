/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  23 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.sql;

import ru.inock.webServletResime.config.Config;
import java.sql.*;
import java.util.Properties;

public class SqlHelper {
    Config c = Config.getInstance();

    Properties prop = Config.getInstance().getProps();

    public <T> T execute(SqlTransaction<T> executor) throws SQLException {

        try (Connection conn = DriverManager.getConnection(prop.getProperty("db.url") +
                prop.getProperty("db.name"), prop.getProperty("db.user"), prop.getProperty("db.password"));
             ) {
            return executor.execute(conn);
        }
    }

    public <T> T transactionExecute(SqlTransaction<T> executor) throws SQLException {
        try (Connection conn = DriverManager.getConnection(prop.getProperty("db.url") + prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password"));) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
