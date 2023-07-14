// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ui;

import engineer.mathsoftware.blog.slides.data.Env;
import engineer.mathsoftware.blog.slides.data.ImageItem;
import engineer.mathsoftware.blog.slides.data.LocalDataRepository;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.transform.Scale;

import java.io.IOException;

class AutoSave {
    private final SaveInvalidation saveInvalidation;
    private Group group;
    private ImageItem currentSlide;
    private BackgroundStatus status;

    AutoSave() {
        saveInvalidation = new SaveInvalidation(this::saveSlide);
        group = null;
        currentSlide = null;
        status = null;
    }

    void setDrawing(Group newGroup) {
        group = newGroup;
    }

    void setStatus(BackgroundStatus newStatus) {
        status = newStatus;
    }

    void onSlideChanged(ImageItem newItem) {
        // setDrawing has not been called yet for the newItem!
        saveInvalidation.slideChanged();
        saveInvalidation.validateNow();

        currentSlide = newItem;
        group = null;
    }

    void onDrawingChanged(ImageItem newItem) {
        currentSlide = newItem;
        saveInvalidation.slideChanged();

        saveInvalidation.validateLater();
    }

    private void saveSlide() {
        if (group == null) {
            return;
        }
        var repo = new LocalDataRepository(Env.SAVE_DIR);
        var params = new SnapshotParameters();
        var invScaleX = 1.0 / group.getScaleX();
        var invScaleY = 1.0 / group.getScaleY();
        var slideFilename = currentSlide.filename();

        status.setStatus("Saving " + slideFilename + "...");
        params.setTransform(new Scale(invScaleX, invScaleY));
        var snapshot = group.snapshot(params, null);
        var slide = new ImageItem(slideFilename, snapshot);

        Thread.startVirtualThread(() -> {
            try {
                repo.createOrUpdateImage(slide);
                Platform.runLater(() -> status.setStatus(slideFilename + " "
                    + "saved"));
            }
            catch (IOException e) {
                Platform.runLater(() -> status.setStatus(e.getMessage()));
            }
        });
    }

    private static class SaveInvalidation {
        static final long WAIT_TIME_MS = 2_000L;
        final Runnable validator;
        boolean isInvalid;
        volatile long lastTime;

        SaveInvalidation(Runnable validator) {
            this.validator = validator;
            this.isInvalid = true;
            this.lastTime = 0L;
        }

        void validateLater() {
            if (!isInvalid) {
                return;
            }

            Thread.startVirtualThread(() -> {
                var timeDiff = System.currentTimeMillis() - lastTime;

                if (timeDiff < WAIT_TIME_MS) {
                    try {
                        Thread.sleep(WAIT_TIME_MS - timeDiff);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }

                Platform.runLater(this::validateNow);
            });
        }

        void validateNow() {
            if (!isInvalid) {
                return;
            }
            validator.run();
            isInvalid = false;
            lastTime = System.currentTimeMillis();
        }

        void slideChanged() {
            isInvalid = true;
        }
    }
}
