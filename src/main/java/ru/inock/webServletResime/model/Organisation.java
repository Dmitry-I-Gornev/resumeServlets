/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  9 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

import ru.inock.webServletResime.util.XmlLocalDateAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "organisation")
public class Organisation implements Serializable {
    private final Link homePage;
    private final String title;
    private final String description;
    private List <WorkPeriod> workPeriods = new ArrayList<>();

    public Organisation() {
       homePage = null;
       title= null;
       description = null;
    }
    public Organisation(String title, String url, String name, String description) {
        Objects.requireNonNull(title,"title must not be null");
        this.homePage = new Link(name,url);
        this.title = title;
        this.description = description;
    }

    public Organisation(String title,  String url, String name, String description, List<WorkPeriod> workPeriods) {
        Objects.requireNonNull(title,"title must not be null");
        this.homePage = new Link(name,url);
        this.title = title;
        this.description = description;
        this.workPeriods = workPeriods;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<WorkPeriod> getWorkPeriods() {
        return workPeriods;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organisation that = (Organisation) o;
        return homePage.equals(that.homePage) && workPeriods.toString().equals(that.workPeriods.toString()) && title.equals(that.title) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, workPeriods.toString(), title, description);
    }

    @Override
    public String toString() {
        return "Organisation{" + homePage + ";" +
                title + ";" +
                description +
                workPeriods.toString() +
                "}";
    }
    public void addPeriod(LocalDate startDate,LocalDate endDate,String description){
        if (startDate != null & endDate != null) {
            workPeriods.add(new WorkPeriod(startDate, endDate, description));
        }else {
            System.out.println("Проверьте корректность даты. Период не добавлен.");
        }
    }
    private LocalDate stringToDate(String stDate){
        return null;
    }


    // ==========================================================================
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "WorkPeriod")
    public static class WorkPeriod implements Serializable {
        @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
        LocalDate startDate;
        @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
        LocalDate endDate;
        String description;

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getDescription() {
            return description;
        }

        public WorkPeriod() {
        }

        public WorkPeriod (LocalDate startDate, LocalDate endDate, String description){
            this.startDate = startDate;
            this.endDate = endDate;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WorkPeriod that = (WorkPeriod) o;
            return startDate.equals(that.startDate) && endDate.equals(that.endDate) && description.equals(that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, description);
        }

        @Override
        public String toString() {
            return "WorkPeriod{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    // ==============================================================================
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "Link")
    public static class Link implements Serializable {
        private String name;
        private String url;

        public Link() {
        }

        public Link(String name, String url) {
            Objects.requireNonNull(url,"name must not be null");
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Link link = (Link) o;
            return name.equals(link.name) && Objects.equals(url, link.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, url);
        }

        @Override
        public String toString() {
            return name + ';' + url ;
        }
    }
}



