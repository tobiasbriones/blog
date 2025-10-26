<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Texsydo Web v0.1.0 | Texsydo MVP (2025/10/26)

Texsydo Web Prototype, a long-running internal project at MathSwe, enabled the
publication of technically rich MathSoftwareEngineer articles on the web. As
Texsydo transitions into a well-defined MVP, it migrates prototype features,
starting with plain text library support as the simplest article type.

## Texsydo Web Text-to-Web API

Texsydo Web MVP is integrating features from its MathSwe Prototype version with
ongoing migration. Reaching production faster is a project's priority.

- [Web MVP Initialization and Structure \| Texsydo MVP (2025/08/25)](https://blog.mathsoftware.engineer/web-mvp-initialization-and-structure---texsydo-mvp-2025-08-25).
- [Integrating Text to Web \| Texsydo MVP (2025/10/26)](https://blog.mathsoftware.engineer/integrating-text-to-web---texsydo-mvp-2025-10-26).

GitHub release
at [Texsydo Web MVP v0.1.0: Provides the Text-to-Web API](https://github.com/texsydo/texsydo---mvp/releases/tag/v0.2.0).

Texsydo Web MVP provides a Web module with an API that converts plain text files
into MSW Engineer static websites. Coming products, like Texsydo Web MVP CLI,
will integrate these Textual standards and automation tooling.

## Web Module

The module `web` of Texsydo Web provides the Textual standards and operational
APIs to create clients.

The new `txt-to-web` feature consists of a terse flow to convert plain text
articles into static websites.

```kotlin
val blogRoot = Path("tsd-blog")
val articleRelPath = Path("abstract-text.txt")
val entry = Entry(blogRoot, articleRelPath)

fun run() = either {
    jekyllArticleOpsFrom(entry)
        .loadIndex().bind()
        .buildEnvironment().bind()
        .installTempJekyllDoRepCli().bind()
        .build()
}
```

The API internally supports TXT articles for now. Running it will produce an
output like the following.

```
⚙ Setting up Jekyll environment
🛈 Source → /web/docs/integration/tsd-blog
🛈 Destination → /tmp/build---dorep-for-jekyll
✔ Copied metadata, navigation, and table of contents

⚙ Cloning DoRep for Jekyll (v0.1.0)
✔ Repository cloned and checked out

⚙ Building DoRep for Jekyll
✔ Gradle build completed successfully

⚙ Running Jekyll build
🛈 Output → /tmp/dorep-for-jekyll/_site
✅ Article site built successfully
```

GitHub project repository at
[Texsydo Web MVP v0.1.0](https://github.com/texsydo/texsydo---mvp/tree/v0.2.0/tsd-web---mvp).

Texsydo Web MVP clients will integrate the web module via Gradle to streamline
these standards and features to final users.

## Texsydo Web Reaching Production with Plain Text Support

By integrating a streamlined API migrated from Texsydo Web Prototype, the new
web module enables clients—including CLI and web server applications—to deliver
Texsydo Web MVP as a product, enhancing project value through well-balanced
development cycles.
