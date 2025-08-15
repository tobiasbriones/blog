<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Encapsulating Jekyll Tooling of MSW Engineer | DoRep for Jekyll (2025/08/14)

---

**Initialize project documentation**

Aug 11: PR [#1](https://github.com/texsydo/dorep-for-jekyll/pull/1) merged into
`dorep/dev <- dorep/docs` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It updates the license information according to the copyright years of the code
I'm migrating to this repository (from 2022), and fills the standard template in
the `README.md` project file.

---

**Fix oldest copyright year in project license**

Aug 11: PR [#2](https://github.com/texsydo/dorep-for-jekyll/pull/2) merged into
`dorep/dev <- dorep/docs` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

I double-checked the oldest code in the ongoing migration, which dates back to
2023 and not 2022 (to be pedantic). There were no custom styles and features in
MSW articles in 2022, so the copyright year should start from 2023.

---

**Initialize script project and Jekyll structure**

Aug 11: PR [#3](https://github.com/texsydo/dorep-for-jekyll/pull/3) merged into
`dorep/dev <- dorep/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds part of the ongoing migration (from prototype to MVP), Texsydo Web MVP,
to decouple the Jekyll code from TSD Web MVP by migrating the Jekyll code in TSD
Web MVP to DoRep Jekyll.

---

**Migrate MSW Engineer app logic and styles**

Aug 11: PR [#4](https://github.com/texsydo/dorep-for-jekyll/pull/4) merged into
`dorep/dev <- msw-engineer` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

I've developed and designed the JS logic and styles (from my Google MD
skillsets) for the MSW Engineer, which still utilizes Jekyll to generate the
static site.

They come from the "orphan" prototype I "live" developed in the `ops` branch of
the `blog` repository. Then, I migrated the "orphan" prototype to the Texsydo
Prototype in `mathswe/prototypes`.

The point is to deprecate Jekyll in favor of React, while keeping the same MSW
Engineer results, the logic, and style specifications.

Adding MSW Engineer logic and styles will allow DoRep for Jekyll to build the
static sites while migrating to React in the future.

---

**Set up Jekyll static project with site structure and configuration**

Aug 13: PR [#5](https://github.com/texsydo/dorep-for-jekyll/pull/5) merged into
`dorep/dev <- jekyll` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It provides the Jekyll project setup that's been running in MSW Engineer (i.e.,
Texsydo Web Prototype), adding data setup and docs aimed at final users.

---

**Implement CLI script for building static site**

Aug 14: PR [#6](https://github.com/texsydo/dorep-for-jekyll/pull/6) merged into
`dorep/dev <- cli` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The CLI tool requires a script to take ownership of Jekyll, ensuring that this
implementation detail remains uncoupled from Texsydo Web.

The Kotlin CLI reuses code from Texsydo Web Prototype, which allows me to
decouple it from its ongoing Texsydo Web MVP migration. I also added a testing
directory to ensure that I keep track of the usage flow while integrating the
tools.

---

All the designs in CSS and JS elements I wrote for publishing my articles at
MathSoftwareEngineer from the beginning are finally established in a formal
project. They had to pass through the "orphan" prototype
(throw-away branch `ops` of the `blog`), Text Ops incipient concept, Texsydo Web
Prototype (in MathSwe Prototypes), and are now migrating to the Texsydo Web MVP.

The migration from Texsydo Web Prototype to MVP led to decouple the
implementation of Jekyll to a separate project, DoRep for Jekyll.

DoRep for Jekyll will allow the migration of Texsydo MVP to happen smoothly by
focusing on Texsydo rather than a monolithic ball of mud that would be harder to
generalize when I implement React in Texsydo Web (as I've mentioned, the purpose
is to leave Jekyll behind). Further, the presented concepts bring new MathSwe
standards, like DoRep that connects Texsydo to Repsymo (there's more on this),
and the "for" keyword in repository names.
