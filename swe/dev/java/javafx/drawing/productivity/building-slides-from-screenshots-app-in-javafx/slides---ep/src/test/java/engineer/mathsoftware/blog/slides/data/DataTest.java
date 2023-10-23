// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {
    @Test
    void isFileSupported() {
        var pathPng = Path.of("data", "file.png");
        var pathJpg = Path.of("data", "file.jpg");
        var pathMultExtPng = Path.of("data", "file.snapshot.png");

        requireSupportedFile(pathPng, "PNG");
        requireSupportedFile(pathJpg, "JPG");
        requireSupportedFile(pathMultExtPng, "MULT_EXT_PNG");

        var pathPdf = Path.of("data", "file.pdf");
        var pathMultExt = Path.of("data", "file.png.md");
        var pathNoExt = Path.of("data", "file");
        var pathDir = Path.of("data");

        requireUnSupportedFile(pathPdf, "PDF");
        requireUnSupportedFile(pathMultExt, "MULT_EXT");
        requireUnSupportedFile(pathNoExt, "NO_EXT");
        requireUnSupportedFile(pathDir, "DIR");
    }

    @Test
    void areValidImageFiles() {
        var filesEmpty = List.<File>of();
        var filesPng = List.of(
            Path.of("data", "file1.png").toFile(),
            Path.of("data", "file2.png").toFile(),
            Path.of("data", "file3.png").toFile()
        );
        var filesJpg = List.of(
            Path.of("data", "file1.jpg").toFile(),
            Path.of("data", "file2.jpg").toFile(),
            Path.of("data", "file3.jpg").toFile()
        );
        var filesPngJpg = List.of(
            Path.of("data", "file1.png").toFile(),
            Path.of("data", "file2.jpg").toFile(),
            Path.of("data", "file3.jpg").toFile()
        );

        requireSupportedFiles(filesEmpty, "EMPTY");
        requireSupportedFiles(filesPng, "PNG");
        requireSupportedFiles(filesJpg, "JPG");
        requireSupportedFiles(filesPngJpg, "PNG_JPG");

        var filesPngHtml = List.of(
            Path.of("data", "file1.png").toFile(),
            Path.of("data", "file2.html").toFile(),
            Path.of("data", "file3.png").toFile()
        );
        var filesMixed = List.of(
            Path.of("data", "file1.png").toFile(),
            Path.of("data", "file2.html.png.md").toFile(),
            Path.of("data", "file3").toFile()
        );

        requireUnSupportedFiles(filesPngHtml, "PNG_HTML");
        requireUnSupportedFiles(filesMixed, "MIXED");
    }

    static void requireSupportedFile(Path path, String legend) {
        assertTrue(
            Data.isFileSupported(path),
            "Failed Data.isFileSupported, " + legend
        );
    }

    static void requireUnSupportedFile(Path path, String legend) {
        assertFalse(
            Data.isFileSupported(path),
            "Failed Data.isFileSupported, " + legend
        );
    }

    static void requireSupportedFiles(
        Collection<? extends File> files,
        String legend
    ) {
        assertTrue(
            Data.areValidImageFiles(files),
            "Failed Data.areValidImageFiles, " + legend
        );
    }

    static void requireUnSupportedFiles(
        Collection<? extends File> files,
        String legend
    ) {
        assertFalse(
            Data.areValidImageFiles(files),
            "Failed Data.areValidImageFiles, " + legend
        );
    }
}
