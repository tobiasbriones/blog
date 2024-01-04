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

[^1]: You can validate fields in any programming language and even return
    `Optional` in Java (don't use exceptions as they're an ill-design), but
    that's barely a runtime check

These definitions can be defined for general angles for multiples of the base
angles in `[0, 360)` degrees if needed and provide a solid domain understanding
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

## Leveraging Powerful Functional Abstractions

From here, I started designing higher-level constructs. First, we can think in
terms of the cartesian quadrants.

`Quadrants of the Plane | Main.hs`

```haskell
data Quadrant = QI | QII | QIII | QIV
```

So, I can define a GADT for the four types of angles defined previously.

`Defining Angle Types by Quadrant`

```haskell
data QuadrantAngle (q :: Quadrant) where
  AngleI :: Acute -> QuadrantAngle 'QI
  AngleII :: Obtuse -> QuadrantAngle 'QII
  AngleIII :: ReflexObtuse -> QuadrantAngle 'QIII
  AngleIV :: ReflexAcute -> QuadrantAngle 'QIV
```

The above GADT starts employing advanced features, namely, `GADTs` itself
and `DataKinds`, where I use the *phantom parameter type* `q` to enforce the
type of `QuadrantAngle` to create from the data constructors. So, I can build
a `QuadrantAngle` *of* `'QI` (i.e., the *promoted data constructor* `QI` of
`Quadrant`), etc.

With the type safety above, I can enforce a function that requires an angle
belonging to the first quadrant, for example:

`Example Function Taking Angles in QI`

```haskell
fn :: QuadrantAngle 'QI -> Angle
fn (AngleI (Acute a)) = a + (angle Straight)
```

If you try to match nonsense
like:

`Matching for Other Cases is Nonsense`

```haskell
fn (AngleII (Obtuse a)) = a * (angle ReflexRight)
```

The program compilation will disallow to proceed with the output:

`Unable to Match Erroneous Logic Thanks to the Functional Type System`

```
• Inaccessible code in
    a pattern with constructor:
      AngleII :: Obtuse -> QuadrantAngle 'QII,
    in an equation for ‘fn’
  Couldn't match type ‘'QI’ with ‘'QII’
```

This is because I defined `fn` above to take only angles in the first quadrant,
so any other case like `AngleII` that belongs to other quadrants will fail to
compile. In other words, **if the program compiles, then it's already *mostly*
correct because the very application logic is *encoded* into the functional type
system**. Now, it depends on you as the engineer to *encode* that domain logic
correctly.

Of course, if you try this system further, like calling the function with
ill-types, results in:

```haskell
let valid = fn $ AngleI $ Acute 20
let wrong = fn $ AngleIV $ ReflexAcute 340
```

```
• Couldn't match type ‘'QIV’ with ‘'QI’
  Expected: QuadrantAngle 'QI
    Actual: QuadrantAngle 'QIV
• In the second argument of ‘($)’, namely
    ‘AngleIV $ ReflexAcute 340’
```

**The type system provided by functions (i.e., by FP) is one of the most
powerful *engineering* tools**, and you won't find it elsewhere in ordinary
programming languages. Provided only by FP, for example, type and data
constructors are functions, data constructors are promoted to type constructors,
type families are "functions for types," etc.

I learned and put into practice many concepts of Haskell and its top type
system, based on type theory, where everything is a function, including type and
data constructors and advanced abstractions like type families and GADTs. This
is in contrast to non-functional languages that can only be randomly designed
via a pragmatic variety of workarounds.

I also have to say this work is part of my end-of-year memories, where I
learned a lot more while doing related research for my next publication at MSW
Engineer, which will remind me of the past 2023/12/31.

With the previous work, I devised an engineered draft giving insight for further
math DSLs, by leveraging the value type angles defined first and creating
abstractions for the plane quadrants, leading to higher-level definitions.
