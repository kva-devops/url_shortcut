package ru.job4j.shortcut.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;


import javax.persistence.*;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * Model of site
 */
@Entity
@Table(name = "sites")
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id of site must be not null")
    private int id;

    /**
     * The name of the site that the user enters during registration
     */
    @NotNull(message = "Name of site must be not null")
    @NotBlank(message = "Name of site must be not empty")
    private String nameOfSite;

    @NotNull(message = "Username of site must be not null")
    @NotBlank(message = "Username of site must be not empty")
    private String username;

    @NotNull(message = "Password of site must be not null")
    @NotBlank(message = "Password of site must be not empty")
    private String password;

    /**
     * Method for creating new site. Username and password are generated automatically during model creation.
     * @param nameOfSite - String, name of site
     * @return Site model
     */
    public static Site of(String nameOfSite) {
        Site site = new Site();
        site.nameOfSite = nameOfSite;
        site.username = genString();
        site.password = genString();
        return site;
    }

    /**
     * Random value generation method
     * @return String value contains numbers and alphabetic characters
     */
    public static String genString() {
        int length = 7;
        return RandomStringUtils.random(length, true, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return id == site.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
