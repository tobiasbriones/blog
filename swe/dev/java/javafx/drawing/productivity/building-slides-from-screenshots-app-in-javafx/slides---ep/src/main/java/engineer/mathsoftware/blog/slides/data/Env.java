// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import java.nio.file.Path;

public final class Env {
    public static final String DATA_ROOT = "data";

    public static final String SAVE_DIR = Path
        .of(DATA_ROOT, "presentation")
        .toString();

    private Env() {}
}
