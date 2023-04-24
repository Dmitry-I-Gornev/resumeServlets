/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  6 мая 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

// https://habr.com/ru/company/naumen/blog/228279/?

package ru.inock.webServletResime.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.inock.webServletResime.model.Resume;
import ru.inock.webServletResime.model.Section;
import ru.inock.webServletResime.storage.storageInterfaces.StreamSerializable;
import ru.inock.webServletResime.util.JsonExtendetsClassAdapter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonParser implements StreamSerializable {

    private static Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Section.class,new JsonExtendetsClassAdapter())
            .create();

    @Override
    public void doWrite(OutputStream os, Resume r) {
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);) {
            GSON.toJson(r, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Resume doReade(InputStream is) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);) {
            return GSON.fromJson(reader, Resume.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFileExc() {
        return ".json";
    }
}
