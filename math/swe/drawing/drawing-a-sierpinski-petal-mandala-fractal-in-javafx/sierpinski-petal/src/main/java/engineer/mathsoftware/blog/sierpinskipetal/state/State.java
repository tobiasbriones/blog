// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

public sealed interface State {
    record Petal(
        Basic.Stateful value
    ) implements State {
        public static Petal newInstance() {
            return new Petal(new BasicStateful());
        }
    }
}
