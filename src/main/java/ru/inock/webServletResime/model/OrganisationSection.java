/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  9 февр. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
@XmlRootElement(name = "organisationSection")
public class OrganisationSection extends Section{
    private final List<Organisation> organizations;

    public OrganisationSection() {
        this.organizations = null;
    }

    public OrganisationSection(List<Organisation> organizations) {
        Objects.requireNonNull(organizations,"organisations must not be null");
        this.organizations = organizations;
    }

    public List<Organisation> getOrganizations() {
        return organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationSection that = (OrganisationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        return organizations.toString();
    }

}
