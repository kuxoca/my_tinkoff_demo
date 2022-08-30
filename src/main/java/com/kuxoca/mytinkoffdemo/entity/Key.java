package com.kuxoca.mytinkoffdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Key {
    private String category;
    private Integer fromCode;
    private Integer toCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Objects.equals(category, key.category) && Objects.equals(fromCode, key.fromCode) && Objects.equals(toCode, key.toCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, fromCode, toCode);
    }
}

