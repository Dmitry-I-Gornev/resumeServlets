package ru.inock.webServletResime.main;
/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  28 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

import ru.inock.webServletResime.config.Config;
import ru.inock.webServletResime.model.*;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;
import ru.inock.webServletResime.util.ModelUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        Storage ARRAY_STORAGE = Config.getInstance().getStorage();

        Resume r1 = new Resume();
        r1.setFullName("Dmitry I. Gornev");

        r1.addContact(ContactType.MAIL, "r1@mail.ru");
        r1.addContact(ContactType.HOME_PHONE, "123-32-34");
        r1.addContact(ContactType.MOBILE, "+7-852-933-01-01");

        TextSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        r1.addSection(SectionType.PERSONAL, personal);
        TextSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        r1.addSection(SectionType.OBJECTIVE, objective);

        List<String> qualificationList = new ArrayList<>();
        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2 ");
        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce ");
        qualificationList.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, ");
        qualificationList.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy, ");
        qualificationList.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts, ");
        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        ListSection qualification = new ListSection(qualificationList);
        r1.addSection(SectionType.QUALIFICATION, qualification);

        List<String> arhievementList = new ArrayList<>();
        arhievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        arhievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk. ");
        arhievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        arhievementList.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        arhievementList.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        arhievementList.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        ListSection arhievement = new ListSection(arhievementList);
        r1.addSection(SectionType.ARHIEVEMENT, arhievement);

        // Прописываем организации (где работал)
        List<Organisation> workerOrganisations = new ArrayList<>();


        Organisation javaOnlineProjects = new Organisation("Java Online Projects",
                "https://www.javaOnlineProjects.ru",
                null, null);
        javaOnlineProjects.addPeriod(ModelUtils.stringToDate("03/2007"), ModelUtils.stringToDate("06/2008"), "Разработчик ПО\n" +
                "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)");
        workerOrganisations.add(javaOnlineProjects);


        Organisation wrike = new Organisation("Wrike",
                "https://www.wrike.com/",
                null, null);
        wrike.addPeriod(ModelUtils.stringToDate("01/2005"), ModelUtils.stringToDate("02/2007"), "Разработчик ПО\n" +
                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).");
        wrike.addPeriod(ModelUtils.stringToDate("10/2014"), ModelUtils.stringToDate("01/2016"), "Старший разработчик (backend)\n" +
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        workerOrganisations.add(wrike);

        OrganisationSection workOrganisation = new OrganisationSection(workerOrganisations);
        r1.addSection(SectionType.EXPERIENCE, workOrganisation);
        //=====================================================

        // Прописываем организации, где учился.
        List<Organisation> studyOrganisations = new ArrayList<>();

        Organisation mfti = new Organisation("Заочная физико-техническая школа при МФТИ", "http://www.school.mipt.ru/", null, null);
        mfti.addPeriod(ModelUtils.stringToDate("09/1984"), ModelUtils.stringToDate("06/1987"), "Закончил с отличием");
        studyOrganisations.add(mfti);

        Organisation itmo = new Organisation("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "https://itmo.ru/ru/",
                null, null);
        itmo.addPeriod(ModelUtils.stringToDate("09/1993"), ModelUtils.stringToDate("07/1996"), "Аспирантура (программист С, С++)");
        itmo.addPeriod(ModelUtils.stringToDate("09/1987"), ModelUtils.stringToDate("07/1993"), "Инженер (программист Fortran, C)");
        studyOrganisations.add(itmo);

        Organisation alcatel = new Organisation("Alcatel", "http://www.alcatel.ru/",
                null, null);
        alcatel.addPeriod(ModelUtils.stringToDate("09/1997"), ModelUtils.stringToDate("03/1998"), "6 месяцев обучения цифровым телефонным сетям (Москва)");
        studyOrganisations.add(alcatel);

        OrganisationSection studyOrganisation = new OrganisationSection(studyOrganisations);
        r1.addSection(SectionType.EDUCATION, studyOrganisation);

// ========================================================================

        Resume r2 = new Resume();
        r2.setFullName("Test User 2");
        r2.addContact(ContactType.HOME_PHONE, "789012");
        r2.addContact(ContactType.MAIL, "r2@mail.ru");

        Resume r3 = new Resume();
        r3.setFullName("Test User 3");
        r3.addContact(ContactType.HOME_PHONE, "345678");
        r3.addContact(ContactType.MAIL, "r23@mail.ru");

        ARRAY_STORAGE.clear();
        ARRAY_STORAGE.save(r1);

        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
    }
}
