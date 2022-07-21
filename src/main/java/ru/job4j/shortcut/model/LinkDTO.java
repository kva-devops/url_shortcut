package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DTO for parameter of method for link shortening
 */
@Getter
@Setter
@AllArgsConstructor
public class LinkDTO {

    /**
     * Site ID
     */
    private int siteId;

    /**
     * URL to be shortened (format: "name.com/some/thing")
     * The root of this URL should be the site name you provided during registration.
     */
    @NotNull(message = "Url for convert must be not null")
    @NotEmpty(message = "Url for convert must be not empty")
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkDTO linkDTO = (LinkDTO) o;
        return siteId == linkDTO.siteId
                && Objects.equals(url, linkDTO.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteId, url);
    }
}
