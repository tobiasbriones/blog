// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Enums {
    public static <T extends Enum<?>> Optional<T> from(Class<T> e, String str) {
        return Arrays
            .stream(e.getEnumConstants())
            .filter(x -> x.name().equals(str))
            .findFirst();
    }

    public static <T extends Enum<?>> List<String> strings(Class<T> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toList();
    }

    private Enums() {}
}
