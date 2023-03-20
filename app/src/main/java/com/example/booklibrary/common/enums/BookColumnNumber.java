package com.example.booklibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum BookColumnNumber {
    ID(0),
    TITLE(1),
    AUTHOR(2),
    PAGES(3);

    @Getter
    private final int column;
}
