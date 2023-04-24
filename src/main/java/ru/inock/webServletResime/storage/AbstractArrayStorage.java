package ru.inock.webServletResime.storage;

import ru.inock.webServletResime.exception.ExistStorageException;
import ru.inock.webServletResime.model.Resume;
import ru.inock.webServletResime.exception.NotExistStorageException;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;
import java.util.Arrays;


public abstract class AbstractArrayStorage implements Storage {
    protected int capacity = 25; // вместительность хранилища резюме
    protected int size = 0; // первый не занятый индекс
    protected Resume[] storage = new Resume[capacity];

    @Override
    public void save(Resume r) {
        if (size() == capacity) recreateCapacity(true);
        int resumeIndex = findResume(r.getUuid());
        if (resumeIndex >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        insertResume(resumeIndex, r);
        size++;
    }

    @Override
    public void update(Resume r) {
        int resumeIndex = findResume(r.getUuid());
        if (resumeIndex < 0) {
            throw new NotExistStorageException(r.getUuid());
        }else{
            storage[resumeIndex] = r;
        }
    }

    @Override
    public boolean delete(String uuid) {
        int resumeIndex = findResume(uuid);
        if (resumeIndex < 0) {
            throw new NotExistStorageException(uuid);
        }else if(resumeIndex == size-1){
            storage[resumeIndex] = null;
        }
        else{
            System.arraycopy(storage, resumeIndex + 1, storage, resumeIndex, (size - 1) - resumeIndex);
            storage[size - 1] = null;
        }
            size--;
            if ((size * 3) < capacity) recreateCapacity(false);
            return true;
        }


    abstract void insertResume(int index, Resume r);
    abstract int findResume(String uuid);



    @Override
    public int size() {
        // возвращает size
        return size;
    }

    @Override
    public Resume[] getAll() {
        Resume [] arrResumes = Arrays.copyOfRange(storage, 0, size);
        Arrays.sort(arrResumes,Resume.fullNameComparator);
        return arrResumes;
    }

    @Override
    public Resume get(String uuid) {
        int resumeIndex = findResume(uuid);
        if (resumeIndex < 0) {
            return null;
        }
        return storage[resumeIndex];
    }

    @Override
    public void clear() {
        size = 0;
        capacity = 25;
        storage = new Resume[capacity];
    }

    protected void recreateCapacity(boolean increase) {
        if (increase) {
            capacity = capacity * 2;
        } else{
            capacity = (capacity / 2) + 1;
            if (capacity < 25) capacity = 25;
            if (storage.length == capacity) return;

        }
        Resume[] tempStorage = new Resume[capacity];
        System.arraycopy(storage, 0, tempStorage, 0, size());
        storage = tempStorage;
    }
}
