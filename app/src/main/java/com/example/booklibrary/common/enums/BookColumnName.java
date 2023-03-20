package com.example.booklibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BookColumnName {
    ID("id"),
    TITLE("title"),
    AUTHOR("author"),
    PAGES("pages");

    @Getter
    private final String name;
}
