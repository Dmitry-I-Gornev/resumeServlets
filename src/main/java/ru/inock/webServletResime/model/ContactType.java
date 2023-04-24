/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  9 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

public enum ContactType {
    PHONE("Тел."),
    MOBILE("Мобильный"),
    HOME_PHONE("Домашний тел."),
    SKYPE("Skype"),
    MAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private final String title;
    ContactType(String title){this.title=title;}

    public String getTitle() {
        return title;
    }
}
