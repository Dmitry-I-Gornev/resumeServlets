/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  22 авг. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.storage;

import ru.inock.webServletResime.exception.ExistStorageException;
import ru.inock.webServletResime.exception.NotExistStorageException;
import ru.inock.webServletResime.exception.StorageException;
import ru.inock.webServletResime.model.*;
import ru.inock.webServletResime.sql.SqlHelper;
import ru.inock.webServletResime.storage.storageInterfaces.Storage;

import ru.inock.webServletResime.util.ModelUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.Objects.nonNull;


public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper = new SqlHelper();
    public SqlStorage() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clear() {
        try {
            sqlHelper.execute(conn -> {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM resume");
                ps.execute();
                return null;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void save(Resume r) {

        try {
            sqlHelper.transactionExecute(conn -> {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid,full_name) VALUES (?,?)");) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    ps.execute();
                }
                Map<ContactType, String> contacts = r.getCONTACTS();
                saveContacts(conn, contacts, r.getUuid());
                saveSections(conn, r);

                return null;
            });
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ExistStorageException(r.getUuid());
        }
    }
    @Override
    public void update(Resume r) {
        delete(r.getUuid());
        save(r);
    }
    @Override
    public Resume get(String uuid) {
        try {
            return sqlHelper.<Resume>execute(conn -> {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r LEFT JOIN contact c ON c.resume_uuid = r.uuid " + "WHERE r.uuid = ?");
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                Resume r = new Resume(uuid, rs.getString("full_name"));
                do {
                    if(nonNull(rs.getString("type"))){
                        r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }

                } while (rs.next());
                //r = addSections(conn, r);
                // r = addOrganisations(conn, r);
                addSections(conn, r);
                addOrganisations(conn, r);

                return r;
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public boolean delete(String uuid) {
        try {
            return sqlHelper.execute(conn -> {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM resume  WHERE uuid = ?");
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                } else {
                    return true;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Resume[] getAll() {
        return getResumes(null,null);
    }
    public Resume[] getResumes(String criteria, String value) {
        String sqlString;
        if (criteria == null){
            sqlString = "SELECT * FROM resume r LEFT JOIN contact c  ON c.resume_uuid = r.uuid ORDER BY uuid ASC";
        } else if (criteria.equals("name")) {
            sqlString = "SELECT * FROM resume r LEFT JOIN contact c  ON c.resume_uuid = r.uuid" +
                    " WHERE full_name ILIKE \'%" + value + "%\' ORDER BY uuid ASC";

        }else {
            sqlString = "SELECT * FROM resume r LEFT JOIN contact c  ON c.resume_uuid = r.uuid " +
                    "WHERE uuid ILIKE \'%" + value + "%\' ORDER BY uuid ASC";
        }


        try {
            Resume[] arr = sqlHelper.execute(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = ps.executeQuery();
                rs.last();
                int size = rs.getRow(); // получаем кол-во резюме в базе.
                if (size == 0) {
                    throw new StorageException("База данных пуста!");

                } else {

                    Resume[] arrResume = new Resume[size];//Создаем массив и заполняем его резюме
                    Resume r;
                    int j = 0; // текущая ячейка массива
                    String uuid; // формируем резюме, пока uuid не поменяется
                    rs.first();
                    uuid = rs.getString("uuid");
                    r = new Resume(uuid, rs.getString("full_name"));

                    if(nonNull(rs.getString("value"))) {
                        r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }

                    while (rs.next()) { // обходим результирующую таблицу
                        uuid = rs.getString("uuid");

                        if (!uuid.equals(r.getUuid())) {
                            arrResume[j] = r;
                            j++;
                            r = new Resume(uuid, rs.getString("full_name"));
                        }
                        if(nonNull(rs.getString("value"))) {
                            r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                        }
                    }

                    // строк в recordset'е не осталось, сохраняем резюме в массив, возвращаем массив из метода.
                    arrResume[j] = r;
                    Resume[] arrResumeTmp = new Resume[j + 1];
                    System.arraycopy(arrResume, 0, arrResumeTmp, 0, j + 1);
                    return arrResumeTmp;
                }

            });
            Arrays.sort(arr, Resume.fullNameComparator);
            //           System.out.println("Work time: " + (System.currentTimeMillis() - startTime));
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int size() {
        try {
            return sqlHelper.execute(conn -> {
                PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM resume");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void saveContacts(Connection conn, Map<ContactType, String> contacts, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid,type,value) VALUES (?,?,?)");) {
            for (ContactType c : contacts.keySet()) {
                ps.setString(1, uuid);
                ps.setString(2, c.toString());
                ps.setString(3, contacts.get(c));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    private void saveSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid,type,content) VALUES (?,?,?)");) {
            Map<SectionType, Section> sections = r.getSECTIONS();

            for (SectionType s : sections.keySet()) {
                switch (s.name()) {
                    case ("QUALIFICATION"):
                    case ("ARHIEVEMENT"):
                        JAXBContext context;
                        try {
                            context = JAXBContext.newInstance(OrganisationSection.class, ListSection.class);
                            Marshaller marshaller = context.createMarshaller();
                            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                            StringWriter sw = new StringWriter();

                            marshaller.marshal(sections.get(s), sw);
                            String sectionXml = sw.toString();
                            ps.setString(1, r.getUuid());
                            ps.setString(2, s.toString());
                            ps.setString(3, sectionXml);
                            ps.addBatch();
                            //System.out.println(sectionXml);
                        } catch (JAXBException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ("PERSONAL"):
                    case ("OBJECTIVE"):
                        ps.setString(1, r.getUuid());
                        ps.setString(2, s.toString());
                        ps.setString(3, sections.get(s).toString());
                        ps.addBatch();
                        break;
                    case ("EXPERIENCE"):
                    case ("EDUCATION"):

                        OrganisationSection os = (OrganisationSection) sections.get(s);
                        List<Organisation> orgs = os.getOrganizations();

                        try (PreparedStatement psOrg = conn.prepareStatement("INSERT INTO " + "organisation (resume_uuid,type,title,description,link_url,link_name) " + "VALUES (?,?,?,?,?,?)" + " RETURNING id");) {
                            for (Organisation o : orgs) {

                                psOrg.setString(1, r.getUuid());
                                psOrg.setString(2, s.toString());
                                psOrg.setString(3, o.getTitle());
                                psOrg.setString(4, o.getDescription());
                                psOrg.setString(5, o.getHomePage().getUrl());
                                psOrg.setString(6, o.getHomePage().getName());
                                ResultSet rs = psOrg.executeQuery();
                                rs.next();
                                int id = rs.getInt(1);
                                //    System.out.println(id);
                                //psOrg.addBatch();
                                try (PreparedStatement workPeriod = conn.prepareStatement("INSERT INTO " + "workperiod (organisation_uuid,start_date,end_date,description) VALUES (?,?,?,?)");) {
                                    List<Organisation.WorkPeriod> wpList = o.getWorkPeriods();
                                    for (Organisation.WorkPeriod wp : wpList) {
                                        workPeriod.setInt(1, id);
                                        workPeriod.setString(2, ModelUtils.dateToString(wp.getStartDate()));
                                        workPeriod.setString(3, ModelUtils.dateToString(wp.getEndDate()));
                                        workPeriod.setString(4, wp.getDescription());
                                        workPeriod.addBatch();
                                    }
                                    workPeriod.executeBatch();
                                }
                            }
                        }
                        break;
                }
            }
            ps.executeBatch();
        }
    }

    private void addSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r \n" +
                "LEFT JOIN section s ON s.resume_uuid = r.uuid \n" + "WHERE r.uuid = ?");) {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                //return r;
            }
            //не забыть обернуть это в цикл
            do {
                if (rs.getString("type") != null){
                    switch (rs.getString("type")) {
                        case ("PERSONAL"):
                        case ("OBJECTIVE"):
                            TextSection st = new TextSection(rs.getString("content"));
                            r.addSection(SectionType.valueOf(rs.getString("type")), st);
                            break;
                        case ("ARHIEVEMENT"):
                        case ("QUALIFICATION"):
                            JAXBContext context;
                            try {

                                StringReader reader = new StringReader(rs.getString("content"));
                                //  InputStreamReader is = new InputStreamReader(new StringReader(rs.getString("content"),"utf-8"));
                                context = JAXBContext.newInstance(OrganisationSection.class, ListSection.class);
                                Unmarshaller unmarshaller = context.createUnmarshaller();
                                ListSection ls = (ListSection) unmarshaller.unmarshal(reader);
                                r.addSection(SectionType.valueOf(rs.getString("type")), ls);
                            } catch (JAXBException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            System.out.println("default statement");
                    }}
            } while (rs.next());
        }
    }

    private void addOrganisations(Connection conn, Resume r) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement("SELECT o.id, o.type org_type, o.title org_title, " +
                "o.description org_description, o.link_url, o.link_name, w.start_date, w.end_date, " +
                "w.description wp_description \n" +
                "FROM organisation o  LEFT JOIN workperiod w\n" +
                "ON o.id = w.organisation_uuid \n" +
                "WHERE o.resume_uuid = ?\n" +
                "ORDER BY o.id\n");) {

            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return;
            }

            class MyHelper {
                Organisation createOrg(ResultSet rs, Organisation o, List<Organisation> studyList, List<Organisation> workList, String orgTypeSection) throws SQLException {
                    //сохраняем старую организацию в соответствующий лист
                    if (o != null) {
                        switch (orgTypeSection) {
                            case ("EXPERIENCE"):
                                workList.add(o);
                                break;
                            case ("EDUCATION"):
                                studyList.add(o);
                                break;
                        }
                    }
                    //возвращаем новую организацию
                    if (rs.getRow() != 0) {
                        return new Organisation(rs.getString("org_title"), rs.getString("link_url"),
                                rs.getString("link_name"), rs.getString("org_title"));
                    } else {
                        // если находимся за пределами ResultSet
                        // (последний вызов метода после цикла do while)
                        return null;
                    }
                }

                void addWorkPeriod(ResultSet rs, Organisation o) throws SQLException {
                    o.addPeriod(ModelUtils.stringToDate(rs.getString("start_date")),
                            ModelUtils.stringToDate(rs.getString("end_date")),
                            rs.getString("wp_description"));
                    // System.out.println(rs.getString("wp_description"));
                }
            }
            MyHelper h = new MyHelper();

            List<Organisation> workerOrganisations = new ArrayList<>();
            List<Organisation> studyOrganisations = new ArrayList<>();

            Organisation org = h.createOrg(rs, null, null, null, null);
            int orgId = rs.getInt("id");
            String orgTypeSection = rs.getString("org_type");
            do {
                if (orgId == rs.getInt("id")) {
                    h.addWorkPeriod(rs, org);
                    //Добавляем периоды
                } else {
                    // Создаем новую организация
                    org = h.createOrg(rs, org, studyOrganisations, workerOrganisations, orgTypeSection);
                    orgId = rs.getInt("id");
                    orgTypeSection = rs.getString("org_type");
                    h.addWorkPeriod(rs, org);
                }
            } while (rs.next());
            h.createOrg(rs, org, studyOrganisations, workerOrganisations, orgTypeSection);
            OrganisationSection workOrganisation = new OrganisationSection(workerOrganisations);
            r.addSection(SectionType.EXPERIENCE, workOrganisation);
            OrganisationSection studyOrganisation = new OrganisationSection(studyOrganisations);
            r.addSection(SectionType.EDUCATION, studyOrganisation);
        }
    }
}
