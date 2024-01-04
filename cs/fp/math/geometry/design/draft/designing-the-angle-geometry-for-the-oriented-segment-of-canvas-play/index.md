<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Designing the Angle Geometry for the Oriented Segment of Canvas Play

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

## Angle Definitions

When working with Haskell, I became eager to go further, so I wrote the
languages needed for the `Line` DSL regarding angle definitions. These
definitions are not requirements for me now, but they'll be useful in the future
for engineering math software.

First, I used the `DataKinds`, `GADTs`, and `TypeFamilies` language extensions,
which can be enabled via pragmas at the source file header. I also imported
the `Maybe` monad for an implementation until the end.

I defined the value types of angles there are so I can build up from these
domain truths.

`Initializing the Domain Design | Angle Value Types | Main.hs`

```haskell
newtype Angle = Angle Double
  deriving (Show, Num)

newtype Acute = Acute Angle -- (0-90)

newtype Obtuse = Obtuse Angle -- (90-180)

newtype ReflexObtuse = ReflexObtuse Angle -- (180-270)

newtype ReflexAcute = ReflexAcute Angle -- (180-360)
```

The value types allow understanding the semantics of the code, but it's still
not safe. One should use LiquidHaskell to define the subsets of the refinement
types. However, I'm not doing that since I only need the design, not the
production code, and this is impossible to do in Java (the targeting
language) anyway[^1].

[^1]: You can validate fields in any programming language, and even return
    `Optional` in Java (and exceptions are out) but that's barely a
    runtime check

These definitions can be defined for general angles for multiples of the base
angles in `[0, 360]` degrees if needed and provide a solid domain understanding
and inference options.

So, now we have the constant angles lying on the axes.

`Quadrantal Angle Sum Type | Main.hs`

```haskell
data QuadrantalAngle
  = Zero
  | Right
  | Straight
  | ReflexRight

angle :: QuadrantalAngle -> Angle
angle x = Angle $ case x of
  Zero -> 0
  Main.Right -> 90
  Straight -> 180
  ReflexRight -> 270
```

With the above definitions, I could model angles that cover the entire cartesian
plane and develop further DSL insights.
