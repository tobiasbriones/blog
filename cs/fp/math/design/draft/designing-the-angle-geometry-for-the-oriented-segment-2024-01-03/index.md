<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Designing the Angle Geometry for the Oriented Segment (2024/01/03)

## The Line Type of Canvas Play

I'm correcting some design flaws in the Java Canvas Play for the Line sum type
[I described before](/ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07).
This way, I'll build a DSL to draw —among others— line segments easily. The
motivation for these DSLs, I have to say, is primarily about drawing fractals
for production at my will.

One of the primary concerns of the initial design is that a `Line` consisted
of `Segment`, `HSegment`, and `VSegment` products, while `Segment` made
redundant the other two. On top of that, the last two are redundant in
structure, so I suggested in the article to make the product types conceptually
orthogonal to induce a partition that fixes the design and introduce a soft sum
type (i.e., `enum`) to factorize the fields of both `HSegment` and
`VSegment` into one physical record with logical variants instead.

The ugliness of Java makes me go to Haskell to think clearly about exactly *what
I need to do* instead of loading an overhead of idiotic (and unnecessary)
language details.

So, I was drafting a more robust language to study the separate concepts
concerning the design I'm finishing in Java to go back with powerful insights
for the next project's PRs.
