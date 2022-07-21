package ru.job4j.shortcut.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
public class SiteOnlyNameDTO {
    @NotNull(message = "Name of site must be not null")
    @NotEmpty(message = "Name of site must be not empty")
    private String nameOfSite;

    public static SiteOnlyNameDTO of(String nameOfSite) {
        SiteOnlyNameDTO site = new SiteOnlyNameDTO();
        site.nameOfSite = nameOfSite;
        return site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteOnlyNameDTO that = (SiteOnlyNameDTO) o;
        return Objects.equals(nameOfSite, that.nameOfSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfSite);
    }
}
