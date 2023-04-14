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
