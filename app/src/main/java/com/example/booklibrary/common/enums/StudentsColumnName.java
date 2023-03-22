package com.example.students.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StudentsColumnName {
    ID("id"),
    NAME("name"),
    SURNAME("surname"),
    COD("cod");

    @Getter
    private final String name;
}
