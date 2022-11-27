package com.remousses.app.tennis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Score {
    DEFAULT("0", 0),
    FIFTEEN("15", 1),
    THIRTY("30", 2),
    FORTY("40", 3),
    OVER("END", 4);

    private final String score;
    private final int pos;
}