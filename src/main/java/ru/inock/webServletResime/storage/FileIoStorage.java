/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  8 апр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.storage;

import ru.inock.webServletResime.exception.ExistStorageException;
import ru.inock.webServletResime.exception.NotExistStorageException;
import ru.inock.webServletResime.exception.StorageException;
import ru.inock.webServletResime.model.Resume;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;
import ru.inock.webServletResime.storage.storageInterfaces.StreamSerializable;

import java.io.*;
import java.util.Objects;

public class FileIoStorage implements Storage {
    File directory;
    private StreamSerializable streamSerializable;

    public FileIoStorage(String dir, StreamSerializable streamSerializable) {
        File directory = new File(dir);
        Objects.requireNonNull(directory, "directory must not bee null");

        this.streamSerializable = streamSerializable;
        if (!directory.isDirectory() || !directory.exists()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory or not found.");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " filed access permission!");
        }
        this.directory = directory;
    }

    private BufferedInputStream toBIS(File f) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            return bis;
        } catch (FileNotFoundException e) {
            throw new StorageException("File reading error! File name is: ", f.getAbsolutePath(), e);
        }
    }

    private BufferedOutputStream toBOS(File f) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(f));
            return bos;
        } catch (FileNotFoundException e) {
            throw new StorageException("File writing error! File name is: ", f.getAbsolutePath(), e);
        }
    }

    @Override
    public void clear() {
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }

    @Override
    public void save(Resume r) {
        File fileResume = new File(directory.getAbsoluteFile() + "/" + r.getUuid() + streamSerializable.getFileExc());
        if (fileResume.exists()) {
            System.out.println("Файл с таким UUID уже существует!");
            throw new ExistStorageException(r.getUuid());
        }
        createFile(fileResume);
        try (BufferedOutputStream bos = toBOS(fileResume)) {
            streamSerializable.doWrite(bos, r);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void update(Resume r) {

        File fileResume = new File(directory.getAbsoluteFile() + "/" + r.getUuid() + streamSerializable.getFileExc());
        if (!fileResume.exists()) {
            System.out.println("Файл с таким UUID не существует! Файл " + fileResume.getAbsolutePath() + " не найден! Обновление резюме невозможно");
            return;
        }
        fileResume.delete();
        createFile(fileResume);
        try (BufferedOutputStream bos = toBOS(fileResume)) {
            streamSerializable.doWrite(bos, r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resume get(String uuid) {
        File fileResume = new File(directory.getAbsoluteFile() + "/" + uuid + streamSerializable.getFileExc());
        if (!fileResume.exists()) {
            System.out.println("Файл с таким UUID не существует! Резюме отсутствует");
            return null;
        }

        try (BufferedInputStream bis = toBIS(fileResume)) {
            return streamSerializable.doReade(bis);
        } catch (IOException e) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public boolean delete(String uuid) {
        File fileResume = new File(directory.getAbsoluteFile() + "/" + uuid + streamSerializable.getFileExc());
        if (!fileResume.exists()) {
            System.out.println("Файл с таким UUID не существует! Резюме отсутствует");
            throw new NotExistStorageException(uuid);
        }
        return fileResume.delete();
    }

    @Override
    public Resume[] getAll() {
        File[] files = directory.listFiles();
        Resume[] resumeArray = new Resume[files.length];
        for (int i = 0; i < files.length; i++) {

          //  BufferedInputStream bis = toBIS(files[i].getAbsoluteFile());
            try (BufferedInputStream bis = toBIS(files[i].getAbsoluteFile())) {
            resumeArray[i] = streamSerializable.doReade(bis);} catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resumeArray;
    }

    @Override
    public int size() {
        return directory.list().length;
    }

    private void createFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


