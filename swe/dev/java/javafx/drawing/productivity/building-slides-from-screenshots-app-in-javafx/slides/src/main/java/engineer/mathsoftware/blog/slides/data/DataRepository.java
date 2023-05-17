// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Path;

public interface DataRepository {
    void createImage(Path imagePath) throws IOException;

    Image readImage() throws IOException;

    void updateImage(Path imagePath) throws IOException;

    void deleteImage(String imageName) throws IOException;
}
