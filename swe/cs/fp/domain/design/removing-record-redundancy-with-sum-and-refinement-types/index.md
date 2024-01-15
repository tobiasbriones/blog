<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Removing Record Redundancy with Sum and Refinement Types

## Physical Redundancy

Creating repeating structures is a design flaw that should probably be removed
via expression simplification.

From the definitions of various kinds of line shapes, I've been writing about a
[problem](/ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07),
so we have something like the following design example:

`HSegment and VSegment are the Exact Same Data Representation | Inefficient Design with Redundant Types`

```haskell
data Point = Point Double Double

data Line
  = Segment { start :: Point, end :: Point }
  | HSegment { radius :: Double, center :: Point }
  | VSegment { radius :: Double, center :: Point }
```

I'll omit the fact that `Line` is not a partition[^1] and just focus on the 
repetition of the way the records were defined here.

[^1]: Line is a sum of three product types with overlap (i.e., `HSegment` and
    `VSegment` are a *refinement* of `Segment`, which is redundant in `Line`)

We have the variants `HSegment` and `VSegment` sharing exactly the same data
representation. Therefore, this design is flawed since repetition is a common
trait of bad programs —mostly in domain-specific programs where expertise is the
key to reaching the engineering grade.

In the above case, we notice (as boilerplate) a physical redundancy since two
records are defined the same way, so as a software engineer when reading that
code, you must obviously see it *factorizable* to simplify the code.

## Factorizing the Duplication with Sum Types

When something hard/physical is duplicated (thus inefficient), a common insight
as an engineer is to create relations to simplify the problem. Sum types are
relations since they create the disjoint union of variants, while product types
or records are hard because they define the actual data to be held.

I can state a design fixing some of the issues described before. I'll take the
geometric language design I gave in
[Designing the Angle Geometry for an Oriented Segment](/designing-the-angle-geometry-for-an-oriented-segment)
as a base. The repetition will be removed by softening the problem via a sum
type.

For a given concept, we can have **a main definition** out of all the equivalent
ones in our domain[^2].

[^2]: This is like mathematical equivalent definitions where you prove the
    double implication among all of them, so you choose one at your will that
    makes the most sense for the underlying application

`Main Segment Definition`

```haskell
data Segment = Segment { start :: Point, end :: Point }
```

Then, we won't have the overlap problem I stated in other articles, where
`Segment` "eats" the other variants when defined as a sum type. So, notice I
broke the flawed `Line` [sum type](#physical-redundancy), and now `Segment` is
directly a product type.

For an API for segments with common orientations, **an equivalent definition**
is what I did to fix the design issues of overlapping. You should immediately
notice *the sum type needed is for the orientation variants* (i.e., horizontal
or vertical).

` Introducing the Sum Type to Remove the Record Redundancy
| Equivalent Segment Definition`

```haskell
newtype SignedAcute = SignedAcute Angle -- (-90, 0) and (0, 90) deg

data QuadrantalOrientation = Horizontal | Vertical -- { 0, 90 } deg

-- Defines the angle to build an oriented segment which must be exactly in
-- (-90, 90]deg.
data Orientation
  = Quadrantal QuadrantalOrientation
  | Angled SignedAcute

data OrientedSegment = OrientedSegment
  { orientation :: Orientation
  , radius :: Double
  , cp :: Point
  }
```

The `OrientedSegment` is an equivalent definition of `Segment`
storing the segment angle and ball in $$R^2$$ for its length. Both are
**isomorphic types**.

I'm also taking the exact `(-90, 90]deg` angle *from the DSL* to populate the
whole plane, so any possible line segment can be defined via
`OrientedSegment` in only one way.

Any segment can be described with either data type I designed, and they don't
overlap since they're equivalent/independent definitions instead of belonging to
the same `Line` sum type given in [Physical Redundancy](#physical-redundancy).

The sum type removing the redundancy of the previous `HSegment` and
`VSegment` records is `QuadrantalOrientation`. That's what I mean by
*factorizing expressions to simplify them*.

The introduction of a sum type for the segment orientation eliminated the two
redundant records for horizontal and vertical *variants* by factorizing them
from two redundant records into a cohesive `OrientedSegment` record with sum
type variants.

## Enriching the Domain Types with Refinements

One important case when redundancy is shown is when we have a superset already
but want a smaller subset to create a specific type.

From the [above example](#physical-redundancy) of the `Line` sum type, we can
see that `Segment` covers all the other possible options, so we don't need to
over-engineer this with a sum type but create a simple subset or refinement type
of the universe set.

From [the simplification](#factorizing-the-duplication-with-sum-types) above
that removed the duplication, we have **our universe set as
`OrientedSegment`** since we want to take subsets like `HSegment` and
`VSegment` **without creating redundant types**. The simplification above is a
correct step to perform, but something else is still missing to fix the `Line`
design since we no longer have the `HSegment`
and `VSegment` types.

Notice a sum type is a universe consisting of a partition of its subsets[^4] or
variants, while a refinement type is a subset of some universe type. In both,
it's essential to know well the universe type to proceed with a design. You
cannot create refinements of a variant since they're not types[^5][^6]. They're
different abstractions, so use them wisely.

[^4]: So you can optimize for one of the disjoint subsets of your choice

[^5]: The type in the example [above](#physical-redundancy) is `Line`,
    not `Segment`, so you cannot refine `Segment` with the given ADT

[^6]: In Java, you can because of what I said in my
    [other article's footnote](/designing-the-angle-geometry-for-an-oriented-segment#fn:4),
    but of course, don't do it

`Refinements for Oriented Segment Subtypes
| Subsets of a Universe Set`

```haskell
-- Ensure to create the refinements via LiquidHaskell --

-- HSegment
--   = { (HSegment x _ _) in OrientedSegment | x is (Quadrantal Horizontal) }
newtype HSegment = HSegment OrientedSegment

-- VSegment
--   = { (VSegment x _ _) in OrientedSegment | x is (Quadrantal Vertical) }
newtype VSegment = VSegment OrientedSegment
```

First, you have to notice you have a universe set. Then you'll want specific
types for important subsets, so you know you should create refinements reusing
the universe type instead of creating more types again from scratch that'll
pollute the code with duplication. Therefore, we have a cohesive system,
eliminating design duplications, yet a powerful type system to cover all the
domain needs.
