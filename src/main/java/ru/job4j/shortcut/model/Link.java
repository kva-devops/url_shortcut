package ru.job4j.shortcut.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Model of link
 */
@Entity
@Table(name = "links")
@Getter
@Setter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id of site must be not null")
    private int id;

    /**
     * Original link for cutting
     */
    @NotNull(message = "Url of site must be not null")
    @NotBlank(message = "Url of site must be not empty")
    private String url;

    /**
     * Short identifier obtained after generating. Generated automatically when a new link is saved
     */
    @NotNull(message = "Shortcut url of site must be not null")
    @NotBlank(message = "Shortcut url of site must be not empty")
    private String shortcut;

    /**
     * Value denoting the total number of hits to the shortened link (default = 0)
     */
    private int total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public static Link of(String url, Site site) {
        Link link = new Link();
        link.url = url;
        link.shortcut = genString();
        link.total = 0;
        link.site = site;
        return link;
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
        Link link = (Link) o;
        return id == link.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
