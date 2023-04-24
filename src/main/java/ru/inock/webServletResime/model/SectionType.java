/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  2 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

public enum SectionType {
    PERSONAL("Личные качества"), // TextSection
    OBJECTIVE("Позиция"), // TextSection
    ARHIEVEMENT("Достижения"), // ListSection
    QUALIFICATION("Квалификация"), // ListSection
    EXPERIENCE("Опыт работы"), // OrganisationSection
    EDUCATION("Образование"); // OrganisationSection


    private String title;
   // private Section type;

    SectionType(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

}
