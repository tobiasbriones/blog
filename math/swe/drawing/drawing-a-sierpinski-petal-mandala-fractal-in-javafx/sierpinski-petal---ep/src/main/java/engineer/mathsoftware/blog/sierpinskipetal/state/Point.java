// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

class Point implements Position.Stateful {
    private double x;
    private double y;

    Point() {
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double value) {
        x = value;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double value) {
        y = value;
    }

    @Override
    public void translateX(double value) {
        x += value;
    }

    @Override
    public void translateY(double value) {
        y += value;
    }
}
