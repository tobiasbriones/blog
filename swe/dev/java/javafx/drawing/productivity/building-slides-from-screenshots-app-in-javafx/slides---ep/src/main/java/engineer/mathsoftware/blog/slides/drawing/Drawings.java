// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing;

import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

final class Drawings {
    static void clearRect(
        Group group,
        Color color,
        Rectangle background
    ) {
        background.setFill(color);
        group.getChildren().add(background);
    }

    static double getImageCornerRadius(Image image) {
        return getCornerRadius(image.getWidth(), image.getHeight());
    }

    static double getCornerRadius(double width, double height) {
        var minDim = Math.min(width, height);
        var unitDim = 48.0;
        var unitArc = 16.0 / 4.0;
        var proportion = minDim / unitDim;
        return proportion >= 1.0 ? proportion * unitArc : unitArc * 4.0;
    }

    static Image getRoundedImage(Image image, ImageView iv, double arc) {
        var clip = new Rectangle();
        var params = new SnapshotParameters();

        params.setFill(Color.TRANSPARENT);

        clip.setWidth(iv.getFitWidth());
        clip.setHeight(iv.getFitHeight());
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);

        iv.setImage(image);
        iv.setClip(clip);

        var roundedImage = iv.snapshot(params, null);

        iv.setClip(null);
        return roundedImage;
    }

    static DropShadow newShadow() {
        var shadow = new DropShadow();

        shadow.setColor(Color.web("#212121"));
        shadow.setRadius(48.0);
        return shadow;
    }

    private Drawings() {}
}
