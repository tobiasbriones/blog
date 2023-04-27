// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

public sealed interface State {
    record Petal(
        Basic.Stateful value
    ) implements State, Accessible<Basic.State, Basic.Stateful> {
        public static Petal newInstance() {
            return new Petal(new BasicStateful());
        }
    }

    @FunctionalInterface
    interface Accessible<R, S extends R> {
        S value();

        default S readonly() {
            return value();
        }
    }
}
