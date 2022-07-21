package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class SiteDTO {

    private boolean registered;

    private String login;

    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SiteDTO siteDTO = (SiteDTO) o;
        return registered == siteDTO.registered
                && Objects.equals(login, siteDTO.login)
                && Objects.equals(password, siteDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registered, login, password);
    }
}
