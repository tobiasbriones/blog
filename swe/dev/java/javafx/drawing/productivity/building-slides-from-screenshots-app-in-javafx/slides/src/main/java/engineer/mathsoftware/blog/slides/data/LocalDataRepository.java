// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalDataRepository implements DataRepository {
    private final String root;

    public LocalDataRepository(String root) {
        this.root = root;
    }

    @Override
    public void createOrUpdateImage(Path imagePath) throws IOException {
        var imageName = imagePath.getFileName().toString();

        if (Files.exists(pathOf(imageName))) {
            updateImage(imagePath);
        }
        else {
            createImage(imagePath);
        }
    }

    @Override
    public void createImage(Path imagePath) throws IOException {
        requireFileExists(imagePath);
        requireValidFile(imagePath);
        requireLocalStorage();

        var imageName = imagePath.getFileName().toString();

        Files.copy(imagePath, pathOf(imageName));
    }

    @Override
    public Image readImage(String imageName) throws IOException {
        var path = pathOf(imageName);

        requireFileExists(path);
        requireLocalStorage();

        return new Image(Files.newInputStream(path));
    }

    @Override
    public void updateImage(Path imagePath) throws IOException {
        var imageName = imagePath.getFileName().toString();

        requireFileExists(imagePath);
        requireLocalStorage();

        deleteImage(imageName);
        createImage(imagePath);
    }

    @Override
    public void deleteImage(String imageName) throws IOException {
        var path = pathOf(imageName);

        requireFileExists(path);
        requireLocalStorage();

        Files.delete(path);
    }

    private void requireLocalStorage() throws IOException {
        var rootPath = pathOf("");

        if (!Files.exists(rootPath)) {
            Files.createDirectory(rootPath);
        }
    }

    private Path pathOf(String filename) {
        return Path.of(root, filename);
    }

    private static void requireFileExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("File " + path + " does not exist");
        }
    }

    private static void requireValidFile(Path path) throws IOException {
        if (!Data.isFileSupported(path)) {
            throw new IOException("File " + path + " is not supported");
        }
    }
}
