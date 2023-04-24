/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  9 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

import java.util.Objects;

public class TextSection extends Section{
    private final String content;

    public TextSection() {
        this("");
            }

    public TextSection(String content) {
        Objects.requireNonNull(content,"content must not be null");
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextSection that = (TextSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return content;
    }

    public String getContent() {
        return content;
    }
}
