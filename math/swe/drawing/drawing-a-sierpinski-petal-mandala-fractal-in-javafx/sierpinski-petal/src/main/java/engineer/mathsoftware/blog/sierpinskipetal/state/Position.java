// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

public final class Position {
    public interface State {
        double getX();
        double getY();
    }

    public interface Eff {
        void setX(double value);
        void setY(double value);
        void translateX(double value);
        void translateY(double value);

        default void setPosition(double x, double y) {
            setX(x);
            setY(y);
        }
    }

    private Position() {}
}
