/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  23 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.sql;

import ru.inock.webServletResime.exception.ExistStorageException;

public class ExistSqlException extends ExistStorageException {
    public ExistSqlException(String uuid) {
        super(uuid);
    }
}
