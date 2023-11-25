// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing.ai;

import java.util.Optional;

/**
 * Defines an object that has state, thus the focus in a list of other objects.
 */
public interface Stateful<T, S> {
    record Focus<T, S>(T object, S state) {}

    void set(T newObject, S newState);

    Optional<Focus<T, S>> get();
}
