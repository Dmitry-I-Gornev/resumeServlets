package ru.inock.webServletResime.exception;

public class ExistStorageException  extends StorageException{
    public ExistStorageException(String uuid) {

        super("Резюме с uuid = " + uuid + " уже существует!",uuid);
    }
}
