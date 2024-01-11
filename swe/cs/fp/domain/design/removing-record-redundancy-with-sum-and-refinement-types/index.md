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
