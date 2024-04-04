<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New Cookie Customization Pane | MathSwe Com (2024/04/03)

---

**Implement component CookieCustomization with advanced options**

Apr 4: PR [#12](https://github.com/mathswe/mathswe.com/pull/12) merged into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It implements a configuration pane to compliment the `CookieBanner` component
with more personalization options. It's an expansion of the `CookieBanner`.

It shows more detailed information that the banner can't show. It's responsive
and supports most screen modes and sizes.

It includes an option to delete all (non-`httpOnly`) cookies.

It lists the cookie purposes (categories) the site uses and shows a switch to
opt-in to each category:

- **Essential**
- **Functional**
- **Analytical**
- **Targeting**

Each list item corresponding to a cookie category displays all its individual
cookie information in a responsive table:

- **Cookie:** Cookie name.
- **Description:** Brief cookie description and why it's used.
- **Provider:** Domain of the company or site emitting the cookie with a link to
its corresponding cookie policy to know how that provider uses cookies. The
provider can be first class, like *mathswe.com*.
- **Retention:** Retention period or duration of the cookie.

The new component `CookieCustomization` expands the functionality of the
`CookieBanner` with more detailed information and features.
---
