<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Cookie Policy Ready and Home Page | MathSwe Com (2024/10/22)

---

**Update cookies used and refactor cookies code**

Oct 21: PR [#20](https://github.com/mathswe/mathswe.com/pull/20) merged into
`dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the cookies used by Google Analytics on the Cookie Policy and
refactorizes the cookies code to keep integrating the policy and banner.

---

**Integrate PR#20 into branch mathswe/dev**

Oct 21: PR [#21](https://github.com/mathswe/mathswe.com/pull/21) merged into
`mathswe/dev <- dev` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The "dev" branch is not a first-class subdomain of the project by MathSwe
standards, so the branch name should be "mathswe/dev" (i.e., "repo-name/dev") if
the feature is not first-class, like MathSwe Legal (i.e., "legal" branch).

So, "legal" is an example of a first-class branch but "dev" is an implementation
detail; hence, the branch name should be "mathswe/dev" to organize development
details.

---

**Sync app with MathSwe Templates**

Oct 22: PR [#22](https://github.com/mathswe/mathswe.com/pull/22) merged into
`mathswe/dev <- mathswe/tsd` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It updates the React project with the latest shared code from MathSwe Templates
and tunes the styles for normal articles since templates only had styles for
centered presentation articles.

The migration performs significant refactorizations to stay up-to-date and
reusable but avoids more advanced steps involving the commercial code so
MathSweCom can reach production faster.

---

**Get cookie policy ready for initial release**

Oct 22: PR [#23](https://github.com/mathswe/mathswe.com/pull/23) merged into
`mathswe/dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It reviews the existing cookie policy and updates the new subdomains that will
use cookies and the revision date so it can become effective soon in the initial
release of MathSweCom.

---

**Add initial MathSweCom home page**

Oct 22: PR [#24](https://github.com/mathswe/mathswe.com/pull/24) merged into
`mathswe/dev <- ms` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the sections MSW and Engineer to showcase the current projects and be
minimally serviceable in production so the app can be deployed as a whole
product.

---

**Enhance handling environment variables and general app title case styles**

Oct 22: PR [#25](https://github.com/mathswe/mathswe.com/pull/25) merged into
`mathswe/dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It removes the environment variable files from the source code to apply secure
practices even though they currently don't contain any sensitive data, this
design will allow them to scale safely later. It also fixes some title cases in
the application according to updated standards.

---
