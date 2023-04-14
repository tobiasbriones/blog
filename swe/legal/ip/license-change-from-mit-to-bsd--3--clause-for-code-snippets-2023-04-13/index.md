<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# License Change from MIT to BSD-3-Clause for Code Snippets (2023/04/13)

In the beginning, I provided the MIT License to the code snippets of the
[Blog](/) and [mathsoftware.engineer](https://mathsoftware.engineer) as well.
This was because I thought articles would contain a vast amount of content but a
minority of code.

That meant the CC-BY-4.0 License would apply to most of my article content and
the (previous) MIT License to just the code snippets.

As the platform grows as a domain-specific system[^1], I've realized that
there's much more code involved[^2] besides mere snippets embedded into
articles.

[^1]: I apply the same MathSwe principles for both designing code and writing
    articles as I devise everything homogeneously

[^2]: Articles are a DSL as well, then notice that "Language" in "DSL" implies a
    lot of **code** that otherwise would be plain **text**

Both licenses —MIT and BSD-3-Clause— are pretty similar and permissive, but
BSD-3-Clause is the license par excellence I've frequently used for relevant
projects while leaving MIT for the most ordinary ones [^3].

[^3]: My preferred licenses to choose from are among: BSD-3-Clause, MIT, 
    Apache-2.0, and GPL-3.0-or-later for source code, and CC-BY-4.0 for content

So, I *usually* provide relevant dev projects (non-articles) with the
BSD-3-Clause License, and now that source code is getting relevant in article
projects, I'm getting rid of the MIT License for code snippets in articles, so
both dev and article projects use the same license for source code.

Notice that the existing CC-BY-4.0 License is not affected by any of this as it
only applies to content. Hence, in addition to the "content license" (e.g., CC),
I also provide a "source code license" (e.g., BSD).

Be aware that the user is always responsible for checking what license they're
given and complying with it and that **this article is not legal advice** but
an informative update.

The preference for BSD-3-Clause is because *it's more academically friendly*
regarding the **explicit** clauses about **attribution for both source code
and binary forms, as well as the non-endorsement clause**.

## Bibliography

- [Licenses \| Open Source Initiative](https://opensource.org/license)
- [The 3-Clause BSD License \| Open Source Initiative](https://opensource.org/license/bsd-3-clause)
- [The MIT License \| Open Source Initiative](https://opensource.org/license/mit)
- [Attribution 4.0 International (CC BY 4.0) \| Creative Commons](https://creativecommons.org/licenses/by/4.0)
- [PR: Add LICENSE-BSD (2023/04/13) \| BLOG \| GitHub Repository](https://github.com/tobiasbriones/blog/pull/31)
- [Commit: Add LICENSE-MIT (2022/01/01) \| BLOG \| GitHub Repository](https://github.com/tobiasbriones/blog/commit/81b2d2fb17493ea8ce7487f5c4f1132deb2c57d3)
- [PR: Add LICENSE-BSD (2023/04/13) \| ENGINEER \| GitHub Repository](https://github.com/mathsoftware/engineer/pull/6)
- [Commit: Initial commit (2021/12/29) \| ENGINEER \| GitHub Repository](https://github.com/mathsoftware/engineer/commit/6186cd2190cfd72937829be1d97585bacb6249f4)
