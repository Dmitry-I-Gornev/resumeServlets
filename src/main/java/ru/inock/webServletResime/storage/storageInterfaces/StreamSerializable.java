/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  28 апр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.storage.storageInterfaces;
import ru.inock.webServletResime.model.Resume;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializable {
    void doWrite(OutputStream os,Resume r);
    Resume doReade(InputStream is);
    String getFileExc();
}
