// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.sierpinskipetal.state;

class BasicStateful implements Basic.Stateful {
    private final Position.Stateful point;
    private double scale;

    BasicStateful() {
        this.point = new Point();
        this.scale = 1.0;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public void setScale(double value) {
        scale = value;
    }

    // implements PositionState.Stateful by Point //
    @Override
    public double getX() {
        return point.getX();
    }

    @Override
    public void setX(double value) {
        point.setX(value);
    }

    @Override
    public double getY() {
        return point.getY();
    }

    @Override
    public void setY(double value) {
        point.setY(value);
    }

    @Override
    public void translateX(double value) {
        point.translateX(value);
    }

    @Override
    public void translateY(double value) {
        point.translateY(value);
    }
}
