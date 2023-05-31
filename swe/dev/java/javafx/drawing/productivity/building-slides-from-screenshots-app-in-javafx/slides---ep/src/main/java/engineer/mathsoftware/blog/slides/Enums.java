// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Enums {
    public static <T extends Enum<?>> Optional<T> from(T[] values, String str) {
        return Arrays
            .stream(values)
            .filter(x -> x.name().equals(str))
            .findFirst();
    }

    public static <T extends Enum<?>> List<String> strings(T[] values) {
        return Arrays.stream(values).map(Enum::name).toList();
    }

    private Enums() {}
}
