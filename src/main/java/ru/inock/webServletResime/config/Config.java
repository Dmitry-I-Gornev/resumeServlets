/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  19 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.config;

import ru.inock.webServletResime.storage.FileIoStorage;
import ru.inock.webServletResime.storage.SqlStorage;
import ru.inock.webServletResime.storage.XmlJaxbParser;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class Config {
    private static File currentClass;
    private static String cfgFileName = "webapp.ini";
    private String basePath;
    private File configFile;
    private static File storageDir;
    private static Properties props = new Properties();
    private static Config INSTANCE;
    private static Storage ARRAY_STORAGE;

    private Config() {
        try {
            basePath = getRootPath();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        configFile = new File(basePath + "/" + cfgFileName);
        if (!configFile.exists()) {
            throw new RuntimeException("Файл конфигурации " + configFile.getAbsolutePath() + " не найден");
        }

        try (InputStream is = new FileInputStream(configFile)) {
            props.load(is);
            storageDir = new File(basePath + "/" + props.getProperty("storage.dir"));
            if (storageDir.exists() && !storageDir.isDirectory()) {
                throw new RuntimeException("Файл " + storageDir.getAbsolutePath() + " не является каталогом");
            } else if (!storageDir.exists()) {
                storageDir.mkdir();
            }
        } catch (IOException e) {
            throw new IllegalStateException("config file not found");
        }
    }


    public Properties getProps() {
        return props;
    }

    public static Config getInstance() {
        synchronized (Config.class) {
            if (Config.INSTANCE == null) {
                System.out.println("INSTANCE = " + INSTANCE);
                Config.INSTANCE = new Config();
            }
        }
        return Config.INSTANCE;
    }

    public Storage getStorage() {
        if(ARRAY_STORAGE == null){
            switch (props.getProperty("storage.type").toLowerCase()) {
                case "file":
                    ARRAY_STORAGE = new FileIoStorage(storageDir.getAbsolutePath(), new XmlJaxbParser());
                    break;
                case "postgresql":
                    ARRAY_STORAGE = new SqlStorage();
                    break;
                default:
                    throw new RuntimeException("Config error! Не задан тип хранилища (возможные варианты - file или postgresql, для web версии следует указывать postgresql)");
            }
        }
        return ARRAY_STORAGE;
    }

    public static File getStorageDir() {
        return storageDir;
    }

    public static String getRootPath() throws UnsupportedEncodingException {
        currentClass = new File(URLDecoder.decode(Config.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath(), "UTF-8"));
        System.out.println(currentClass.getAbsolutePath());
        return currentClass.getAbsolutePath().replace("\\WEB-INF\\classes", "");
    }
}
