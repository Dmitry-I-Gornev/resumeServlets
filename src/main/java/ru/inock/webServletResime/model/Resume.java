package ru.inock.webServletResime.model;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD) //работать с полями (по умолчанию работает только с set'ерами)
@XmlType(name = "resume")
@XmlRootElement
public class Resume implements Comparable<Resume>, Serializable {

    @XmlElementWrapper
    private final Map<ContactType, String> CONTACTS = new EnumMap<>(ContactType.class);
    @XmlElementWrapper
    private final Map<SectionType, Section> SECTIONS = new EnumMap<>(SectionType.class);

    @XmlElement
    private String uuid;
    private String fullName;


    public Resume() {
        this(UUID.randomUUID().toString());
    }

    public Resume(String uuid) {
        this.uuid = uuid;
        this.fullName = "New resume";
    }
    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }
    public String getUuid() {
        return uuid;
    }
    public void addContact(ContactType type, String value) {
        CONTACTS.put(type, value);
    }
    public void addSection(SectionType type, Section section) {
        SECTIONS.put(type,section);
    }

    public Map<ContactType, String> getCONTACTS() {
        return CONTACTS;
    }

    public Map<SectionType, Section> getSECTIONS() {
        return SECTIONS;
    }

    public String getContact(ContactType type) {
        return CONTACTS.get(type);
    }
    public Section getSection(SectionType type) {
        return SECTIONS.get(type);
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int compareTo(Resume r) {
        //расписываем natural order для сортировки по умалчанию (по uuid)

        return uuid.compareTo(r.getUuid());
    }
    public static Comparator<Resume> fullNameComparator = Comparator.comparing(Resume::getFullName);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

//    @Override
//    public String toString() {
//        return uuid;
//    }
//

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}

