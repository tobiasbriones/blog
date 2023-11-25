// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

public final class Basic {
    public interface Stateful extends State, Eff {}

    public interface State extends Position.State, Transformation.State {}

    public interface Eff extends Position.Eff, Transformation.Eff {}

    private Basic() {}
}
