/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  9 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD) //работать с полями (по умолчанию работает только с set'ерами)
@XmlType(name = "ListSection")
@XmlRootElement

public class ListSection extends Section{
    private List<String> items;
    public ListSection() {
    }

    // private final List<String> items;

    public ListSection(List<String> items) {

        Objects.requireNonNull(items,"items must not be null");
        this.items = items;
    }
    public List<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return  items.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
