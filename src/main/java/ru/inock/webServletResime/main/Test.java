package ru.inock.webServletResime.main;

import ru.inock.webServletResime.config.Config;

import java.util.Properties;

public class Test {
    public static void main(String[] args) {
        Config c = Config.getInstance();
        System.out.println(c);
        Properties properties = c.getProps();
        System.out.println(properties);

    }
}
