/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  28 апр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.storage.storageInterfaces;

import ru.inock.webServletResime.model.Resume;

import java.io.IOException;

// lesson25.mp4

public interface Storage {

    void clear() throws IOException;

    void save(Resume r);

    void update(Resume r);

    Resume get(String uuid);

    boolean delete(String uuid);

    Resume[] getAll();

    int size();

}
