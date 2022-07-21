package ru.job4j.shortcut.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO for logging
 */
@Getter
@Setter
public class LoginDTO {

    private String username;

    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginDTO loginDTO = (LoginDTO) o;
        return Objects.equals(username, loginDTO.username)
                && Objects.equals(password, loginDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
