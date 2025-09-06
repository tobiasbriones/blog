<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Fix Client Ergonomics When Running Jekyll Internally | DoRep for Jekyll (2025/09/05)

---

**Recommend clients to build before running the script**

Aug 16: PR [#7](https://github.com/texsydo/dorep-for-jekyll/pull/7) merged into
`dorep/dev <- dorep/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The client (Texsydo Web MVP) should implement a more serious way of running the
script since it's a Kotlin app in the end, so it needs building and some
installation.

The recommended execution approach is to build via `gradle installDist` and
execute the binary from the `build/install` directory.

The building will copy the Jekyll website resources to the installation
directory. That way, client users can run the script from the binary to set up a
more professional or production experience.

---

**Add environment param to runCommand**

Sep 6: PR [#8](https://github.com/texsydo/dorep-for-jekyll/pull/8) merged into
`dorep/dev <- dorep/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

Some commands need shell environment variables to find scripts or paths (e.g.,
`jekyll` path).


---

**Drop cmd bundle and set Jekyll env vars**

Sep 6: PR [#9](https://github.com/texsydo/dorep-for-jekyll/pull/9) merged into
`dorep/dev <- jekyll` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The program doesn't find the `bundle` or `jekyll` binaries when running from
Kotlin, so it needs their paths to call them. Further, it doesn't find `gems`
when running `bundle` even after passing the environment paths, so removing it
and calling `jekyll` directly simplifies and fixes the execution. Third, Jekyll
will fail to build out of a Git repository because of a GitHub Pages plugin,
which is why the `PAGES_REPO_NWO` variable is set with a dummy value.

---

The Kotlin CLI program is meant to be used as a script, but it's technically
not. So, it doesn't make sense to run a program in development mode (i.e.,
`./gradlew run args=...`), in a production caller application (i.e., Texsydo
Web).

Running scripts or commands, like `bundle` and `jekyll`, brings more side effect
problems, so after (separately) testing how DoRep for Jekyll integrates into the
Texsydo Web MVP, I made the corresponding environment and usage fixes.

The recommended approach to running the "script" is to build the project and run
the binary, as stated in the documentation. On the other hand, I tested the
project in a realistic environment to ensure proper Jekyll integration by
setting process environment variables and calling Jekyll directly without
Bundle.
