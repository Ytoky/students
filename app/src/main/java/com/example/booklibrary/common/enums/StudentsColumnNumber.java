package com.example.students.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StudentsColumnNumber {
    ID(0),
    NAME(1),
    SURNAME(2),
    COD(3);

    @Getter
    private final int column;
}
