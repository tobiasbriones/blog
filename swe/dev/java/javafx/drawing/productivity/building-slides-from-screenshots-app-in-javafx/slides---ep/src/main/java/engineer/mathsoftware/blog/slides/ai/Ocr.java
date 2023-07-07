// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.ai;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class Ocr {
    static List<BoundingBox> textBoxes(Image image) {
        var boxes = new ArrayList<BoundingBox>();
        var tesseract = new Tesseract();
        var bufferedImage = SwingFXUtils.fromFXImage(image, null);
        var dataRes = Ocr.class.getClassLoader().getResource("tessdata");

        if (dataRes == null) {
            throw new RuntimeException(
                "tessdata not found in the app resources directory"
            );
        }
        try {
            var dataPath = Path.of(dataRes.toURI()).toAbsolutePath().toString();

            tesseract.setLanguage("eng");
            tesseract.setDatapath(dataPath);
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_LSTM_ONLY);

            var words = tesseract.getWords(
                bufferedImage,
                ITessAPI.TessPageIteratorLevel.RIL_WORD
            );

            for (var word : words) {
                var rect = word.getBoundingBox();

                boxes.add(new BoundingBox(
                    rect.getMinX(),
                    rect.getMinY(),
                    rect.getWidth(),
                    rect.getHeight()
                ));
            }
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return boxes;
    }

    private Ocr() {}
}
