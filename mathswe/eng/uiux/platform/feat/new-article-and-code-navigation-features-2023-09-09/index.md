<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New Article and Code Navigation Features (2023/09/09)

![New Article and Code Navigation Features (2023/09/09)](static/new-article-and-code-navigation-features-2023-09-09.png)

---

## Article Navigation

The features for navigating inside articles is in production and recently
designed.

Article contents navigation is fundamental to moving among the tree of
homogeneous[^x] articles I create. This way, all this high-quality content
can be put together[^x].

[^x]: My articles are homogeneous since they have the *same* design, so I can
    **scale up** the whole content as I add more articles by letting the
    platform design principles **constant** (i.e., relativity)

[^x]: Recall the cohesion principle to build greater wholes

I finished polishing the so-called **ToC (Table of Contents)**[^x] feature this
time.

[^x]: I don't like using those commonly used generic terms you see from older
    computing and academia, like "table" of "contents," because it makes the
    articles more heterogeneous by using those repetitive stereotypes

From design, we have that an article is the same as its navigation, so both have
a different level of abstraction.

That is, **the article and its navigation are the same, but the nav is a resumed
version with only the headings or sections**[^x][^x].

[^x]: It's important to see my design view to conceptualize and understand the
    principle of homogeneity —both article and nav are the *same*, with
    *different* levels of abstraction or purpose

[^x]: It could also include other major navigation links like image or table
    captions

This well-designed navigation will start allowing readers to move smoothly among
the platform articles, so besides having great qualities already, now they're
also simpler and nice to browse with.

### Desktop

The desktop design has the article navigation on the left, and the article is
focused in the center.

You can see in the screenshot how the "More Recursion" section is selected in
the navigation since the article is scrolled to that position.

![New Article Navigation](static/new-article-navigation.png)

<figcaption>
<p align="center"><strong>
New Article Navigation
</strong></p>
</figcaption>

So, what's on the right? I'll implement that later. I'll place the footnotes,
references, and others like the footer. That way, the design is from left to
right, with the article centered as the page's main content.

The navigation is updated whenever the reader scrolls over the article or loads
a URL with a section hash.

For example, the URL path for the selected section is
`/drawing-a-tree-on-canvas-with-xy-coordinates#more-recursion`, indicating that,
`more-recursion` is the section ID to select.

When you select a section from the navigation, this is also applied to the URL
hash, and you're smoothly led to the section via scroll.

This navigation allows users with greater displays, like a monitor or tablet, to
leverage greater power while enjoying the reading.

## Mobile

This is the mobile version of the responsive article navigation.

As you can see, from **the bottom menu —which is also a new feature 👾—** you can
open the navigation from the left, and it'll take the foreground of the screen.

**The navigation can be expanded or hidden via the "Navigation" menu button**
for either mobile or desktop, but this feature is more advantageous for mobile.

![Mobile Article Navigation](static/mobile-article-navigation.png)

<figcaption>
<p align="center"><strong>
Mobile Article Navigation
</strong></p>
</figcaption>

Then, when you select a section, like `Node Circle and Content`, you'll be led
to that section via smooth scroll:

![Mobile Article Reading](static/mobile-article-reading.png)

<figcaption>
<p align="center"><strong>
Mobile Article Reading
</strong></p>
</figcaption>

Similarly to how a PDF is read, articles have those features now. The difference
is that PDFs are binary files with no meaning 😐 while my articles are
domain-specific systems (i.e., actual engineering instead of mere text) 🦾.

This responsive mobile navigation will allow mobile readers to efficiently move
among articles, even on mobile devices that can only support one view at a time.
