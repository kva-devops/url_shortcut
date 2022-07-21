package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class StatLinkDTO {

    private String url;

    private int total;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatLinkDTO that = (StatLinkDTO) o;
        return total == that.total
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, total);
    }
}
