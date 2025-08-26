<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Texsydo Web MVP Initialization and Structure (2025/08/25)

---

**Initialize Texsydo Web MVP project operations**

Aug 10: PR [#6](https://github.com/texsydo/texsydo---mvp/pull/6) merged into
`tsd/dev <- tsd/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It begins the project migration from MathSwe Prototypes by creating the project
structure.

The structure consists of a Gradle, Kotlin non-modular project with default
libraries, such as `ArrowKt`. It includes `MathSweKt` with the pipe,
application, and composition operators.

The MVP migration will have a **product focus**, unlike the prototype, which was
*a development one*. This initialization opened more requirements for the MVP.
For example, separating the Jekyll static files to a separate repository to
decouple the *output* (static site) from *the tool* (CLI app), as well as their
licenses (user sites must not be affected by the AGPL license of the tool).

It still doesn't add deploying support with `jpackage`, like the prototype (on
Gradle 8) did, since `beryx` is not compatible with Gradle 9, the version used
by the new MVP. The branch `tsd/ops-_-beryx-jpackage-on-gradle-9` is pending
completion of the deployment support.

Creating the project structure initiated the migration process from Texsydo Web
Prototype to MVP, while identifying important structural changes. The migration
from prototype to MVP will consist of a product approach, as I've developed and
validated most of the code concerning this migration.

---

**Add docs for project Texsydo Web MVP**

Aug 18: PR [#7](https://github.com/texsydo/texsydo---mvp/pull/7) merged into
`tsd/dev <- tsd/docs` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

Texsydo Web MVP is being migrated from MathSwe Prototypes, which contains
implementations dating from 2023, and this PR reflects the subproject docs in
the MVPs repository.

---

**Enable CLI and System APIs from Texsydo Web Prototype**

Aug 21: PR [#8](https://github.com/texsydo/texsydo---mvp/pull/8) merged into
`tsd-web/tsd-web <- tsd-web/cli`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It migrates the structure of the CLI code to allow commands to be implemented,
and adds the System API to run commands, such as DoRep for Jekyll. The next
features to migrate will require support from both APIs.

---

**Set up Gradle multimodule project tsd-web---mvp with Kotest**

Aug 24: PR [#9](https://github.com/texsydo/texsydo---mvp/pull/9) merged into
`tsd-web/tsd-web <- tsd/ops`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

Texsydo Web MVP and others, like FX (and Dev), are migrating from their
Prototype form to MVP, which requires a modular monolithic structure for each
major project (i.e., Web, FX, Dev).

The multimodule setup will allow TSD Web MVP to migrate its base code from the
prototype and enable interfaces, such as a CLI or Web App, to reach production
soon. Regarding testing, Kotest (unlike JUnit) fits FP better and is designed
for Kotlin.

The multimodule structure and Kotest integration will provide the right spot for
Texsydo Web MVP to reach production after finishing its migration, as well as
the next MVPs coming.

---
