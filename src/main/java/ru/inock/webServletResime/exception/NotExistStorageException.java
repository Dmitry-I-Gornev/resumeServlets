package ru.inock.webServletResime.exception;

public class NotExistStorageException extends StorageException{
    public NotExistStorageException(String uuid) {
        super("Резюме с uuid = " + uuid + " не существует!",uuid);
    }
}
