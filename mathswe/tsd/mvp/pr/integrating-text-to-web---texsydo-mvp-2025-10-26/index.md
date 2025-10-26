<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Integrating Text to Web | Texsydo MVP (2025/10/26)

---

**Found module web and txt**

Aug 31: PR [#10](https://github.com/texsydo/texsydo---mvp/pull/10) merged into
`tsd-web/web <- tsd-web/txt-to-web`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It migrates and implements the foundation of the `web` module regarding the
first part of Texsydo, Text, where everything is a Text Article.

It defines `Text.kt` with `Article` as the abstraction unit, along with the
`txt` module, allowing "Plain Text to Web" to be the first feature to ship.

---

**Add module system with cmd and fs migrations**

Aug 31: PR [#11](https://github.com/texsydo/texsydo---mvp/pull/11) merged into
`tsd-web/tsd-web <- tsd/ops`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the Gradle module `system` to the `tsd-web---mvp` project, along with
the migrations that enabled the Web Prototype to run system commands and operate
on the file system via a few utility methods.

---

**Provide txt-to-web API flow via DoRep for Jekyll**

Oct 26: PR [#12](https://github.com/texsydo/texsydo---mvp/pull/12) merged into
`tsd-web/web <- tsd-web/jekyll`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

Recent migrations from the Texsydo Web Prototype included base Jekyll
transformations to convert plain text into intermediate Jekyll Markdown and then
into a static website.

The new API flow integrates these migrations with DoRep for Jekyll, providing
the first feature in the web library to build a website for a plain-text
article.

---
