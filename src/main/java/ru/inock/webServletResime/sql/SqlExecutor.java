/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  23 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor<T> {
    T execute (PreparedStatement st) throws SQLException;
}
