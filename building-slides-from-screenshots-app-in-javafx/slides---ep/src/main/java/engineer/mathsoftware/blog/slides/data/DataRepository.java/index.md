---
permalink: building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/DataRepository.java.html
title: "building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/DataRepository.java"
---

# DataRepository.java
```java
// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.data;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DataRepository {
    void createOrUpdateImage(ImageItem item) throws IOException;

    void createOrUpdateImage(Path imagePath) throws IOException;

    void createImage(Path imagePath) throws IOException;

    List<ImageItem> readAllImages() throws IOException;

    Image readImage(String imageName) throws IOException;

    void updateImage(Path imagePath) throws IOException;

    void deleteImage(String imageName) throws IOException;

    void deleteAllImages() throws IOException;
}

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/javafx/drawing/productivity/building-slides-from-screenshots-app-in-javafx/slides---ep/src/main/java/engineer/mathsoftware/blog/slides/data/DataRepository.java" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>