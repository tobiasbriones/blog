// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import javafx.util.StringConverter;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public final class Enums {
    public interface ToEnum<T extends Enum<T>> {
        Class<T> enumType();

        default T toEnum() {
            return Enum.valueOf(enumType(), getClass().getSimpleName());
        }
    }

    public static final class EnglishConverter<T extends Enum<T>> extends StringConverter<T> {
        private static final Pattern PATTERN = Pattern
            .compile("([A-Z]+[a-z]*)");
        private static final Pattern SPACED_PATTERN = Pattern.compile(" ");
        private final Class<T> type;

        public EnglishConverter(Class<T> type) {
            super();
            this.type = type;
        }

        @Override
        public String toString(T object) {
            return PATTERN
                .matcher(object.name())
                .results()
                .map(MatchResult::group)
                .reduce((word1, word2) -> word1 + " " + word2)
                .orElse("");
        }

        @Override
        public T fromString(String string) {
            var name = SPACED_PATTERN.matcher(string).replaceAll("");
            try {
                return Enum.valueOf(type, name);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private Enums() {}
}
