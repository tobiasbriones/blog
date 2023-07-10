// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.drawing.ai;

import javafx.geometry.BoundingBox;

import java.util.Optional;

final class States {
    static class WordSelectionState implements Stateful<BoundingBox, AIShape.State> {
        private BoundingBox shape;
        private AIShape.State state;

        @Override
        public void set(BoundingBox newShape, AIShape.State newState) {
            if (newState == AIShape.State.Normal) {
                unset();
                return;
            }
            shape = newShape;
            state = newState;
        }

        private void unset() {
            shape = null;
            state = null;
        }

        public Optional<Focus<BoundingBox, AIShape.State>> get() {
            return shape != null && state != null
                ? Optional.of(new Focus<>(shape, state))
                : Optional.empty();
        }
    }

    private States() {}
}
