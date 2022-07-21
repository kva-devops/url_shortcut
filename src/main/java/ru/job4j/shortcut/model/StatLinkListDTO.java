package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class StatLinkListDTO {

    private List<StatLinkDTO> statLinkDTOList;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatLinkListDTO that = (StatLinkListDTO) o;
        return Objects.equals(statLinkDTOList, that.statLinkDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statLinkDTOList);
    }
}
