<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# ML Dataset Symmetry: Relating an MIT Approach to my Recent Type System Work on Removing Redundancy

## How Symmetry Reduces Complexity

I was reading an MIT article about
[How symmetry can come to the aid of machine learning](https://news.mit.edu/2024/how-symmetry-can-aid-machine-learning-0205)[1].
It struck my attention due to the last works I wrote about removing redundancy
in records with functional abstractions while designing the API of Canvas Play.

The MIT approach takes an insight into Weyl’s law, which measures spectral
information complexity and how it can be related to measuring the complexity of
datasets to train a neural network.

NNs can then be trained efficiently (by removing the symmetric redundancies) and
keeping correctness with even smaller errors —**especially important for
scientific domains**.

Results for enhancing sample complexity are guaranteed by a theorem pair
providing the improvement with an algorithm, which is also the best possible
gain.

## How the Functional Type System Reduces Complexity

I recently worked on articles like
[Removing Record Redundancy with Sum and Refinement Types](/removing-record-redundancy-with-sum-and-refinement-types)[2]
while designing an initial DSL in Java for the Canvas Play project. I'm showing
now that [the symmetry approach from MIT](#how-symmetry-reduces-complexity)
is quite related to my work in FP.

I mentioned duplications like records that only differ in their name, the
creation of types already covered by a universe type that should be refined
instead and runtime memory waste.

As an engineer, you must be wise to apply powerful abstractions.

**Sum types** are ADTs (Algebraic Data Types) that can be used to soften
variations of a physical product type or record, thus removing redundant types.
That is, you need to create more types for the underlying variants, not for the
bigger product type.

Engineering a domain is more demanding than it looks, so to overcome design
flaws, I also suggested using **equivalent definitions** or **isomorphic types**
to enrich the domain capabilities while keeping the correct data types
efficiently.

For universe sets or types, you proceed to create refinements (that might be
liquid, or logical predicates), instead of creating new product types
redundantly.

As you read, engineering can get challenging. For example, when performance
optimizations are required, leaving the compiler behind. In these cases, the
engineer has to compromise between the DSL and implementation details. It's when
you find your **memory layout** is redundant in runtime and allocated
frequently, although the DSL was perfect.

I also state how **expression factorization** is the key skill to catch these
flaws. For example, if types are repeating, you grab the duplication and make
variants with sum types.

When designing sum types, the variants must be **orthogonal**, according to the
domain spec. If they're mildly linear, you'll still have symmetries requiring
duplications (more data, code, and technical debt). Therefore, the wrong
abstraction is over-engineering. In my words, "if you're not doing something
right[^1], don't do it at all," because doing it wrong turns more expensive than
refraining from doing it[^2].

[^1]: It's indeed pretty accurate, "right" and "orthogonal" in this context

[^2]: This is relative and also applies to taking risks, committing mistakes, or
    breaking something purposefully for testing stages, as long as you do it
    rightfully, so even when failing, you still have to do it right or fail
    successfully

Also, notice how **the symmetries and the engineering are *relative* to the
domain**. Engineers measure with respect to the domain of expertise.

There's a myriad of functional abstractions to simplify programs, others like
GADTs and type families. Since you must be wise when applying them, you'll need
mathematical and domain skills to catch symmetries and patterns first.

## Symmetries for the Angle DSL and ML

I also wrote
[Designing the Angle Geometry for an Oriented Segment](/designing-the-angle-geometry-for-an-oriented-segment)[4],
where I designed a draft DSL in Haskell to understand the domain problem
clearly. The language is about defining the angles of the cartesian plane
thoroughly from low-level to high-level representations.

The plane has four orthogonal or visually perpendicular subplanes bounded by two
axes. Of course, the unit vectors $$i,\, j$$ are orthogonal (from abstract
linear algebra) and perpendicular (from geometry), so you have the pleasure of
depicting these abstractions graphically.

I spotted the obvious symmetry in the plane and *partitioned* the data types
into the four quadrants `QI, QII, QIII, QIV`, and the four quadrantal
angles `0, 90, 180, 270`, composing any angle in `[0, 360)deg` in the plane
**without redundancies**.

Therefore, my design is correct and efficient as per domain. Not to say the
powerful abstractions like GADTs, type classes/families, etc., that remove more
code redundancies and make ill-code not compile. In other words, I'm describing
potential **engineering-grade** software.

Expanding these results further, notice the ML conclusion of the MIT side:

> ...the approach presented in the paper "diverges substantially from related
> previous works, adopting a geometric perspective and employing tools from
> differential geometry. This theoretical contribution lends mathematical
> support to the emerging subfield of 'Geometric Deep Learning,' which has
> applications in graph learning, 3D data, and more. The paper helps establish a
> theoretical basis to guide further developments in this rapidly expanding
> research area."
>
> Source: How symmetry can come to the aid of machine learning | MIT News [1]
> (under fair use)

Eventually, this concludes with **geometric perspectives**, **differential
geometry** (i.e., calculus), and the emerging **geometric deep learning** field.

Recall that generic tools and software will never have the engineering grade of
proper mathematical software. They manage to compute low-level math, but the
abstractions are what really matters.

Moreover, all scientific and engineering projects always rely on math. Will they
rely on general-purpose tricks or actual mathematical software in the coming
future? Certainly, the engineering of software must be standardized.

As I [said above](#how-the-functional-type-system-reduces-complexity),
**engineering is a function of its domain**, so abstractions will always matter
more than implementation details. MathSwe optimizes for the domain and the
engineering grade (whenever possible).

By engineering mathematical software, the latest theoretical results will open
extraordinary opportunities for MathSwe to undertake.

## Data Symmetries

Besides the previous conclusion of the MIT article about geometric affairs,
there are points to complement how these findings got my attention.

I list the other relevant points of data symmetries I found worth noting from
the MIT article.

> ➕ ...modifying Weyl’s law so that symmetry can be **factored** into the
> assessment of a dataset’s complexity...
>
> ➕ ...symmetries, or so-called "**invariances**," could benefit machine
> learning...
>
> ➕ How many fewer data are needed to train a machine learning model if the
> data contain symmetries?
>
> ➕ ...the image can be **partitioned** into 10 identical parts, you can get a
> factor of 10 improvement.
>
> ➕ ...symmetries of higher **dimension** are more important because they can
> give us an exponential gain...
>
> Source: *How symmetry can come to the aid of machine learning* \| MIT News [1]
> (under fair use)

It shows how you **factorize** patterns or symmetries. If you often read me,
you'll find I teach this by connecting the same polynomial or expression
factorization skills corresponding to elementary math courses to the
factorization of actual functional programming expressions corresponding to
engineering solutions.

For instance, I explained how records that only differ in their logical variants
are optimized by factorizing the common data representation and extracting the
changing variants into sum types:

> The sum type removing the redundancy of the previous `HSegment` and `VSegment`
> records is `QuadrantalOrientation`. That’s what I mean by what I mean by
> *factorizing expressions to simplify them*.
>
> Source: *Factorizing the Duplication with Sum Types* \| MSW Engineer Blog [3]

Any symmetry or pattern can be seen as an **invariant** —a mathematical property
that remains unchanged after certain transformations. Such properties are often
concerning, but not limited, to geometry and topology.

Then, they suggest we can require fewer data by leveraging the symmetry found in
a dataset, so if we **partition** it into as many symmetries found, we get
proportionate gains. Moreover, when the symmetry is **dimensional**, the gain is
clearly exponential.

The partition part is what I've emphasized in my latest articles to apply
orthogonality. It's clear if you have orthogonal concepts, you open new
dimensions mathematically. For example, when I mention:

> ...the sum types induce a partition of orthogonal products that simplifies the
> programs since all physical and logical redundancies are eliminated.
>
> Source: *High-Level Angles* \| MSW Engineer Blog [5]

> I've been enforcing the proper understanding of the theory with concepts like
> orthogonality to produce quality software.
>
> Source: *Engineering Geometry Languages in Haskell with Insights* \| MSW
> Engineer Blog [6]

All engineering fields require a major level of math. Particularly, SWE requires
the most math because it's logical, so there's nothing physical, so *you must
create the laws*. Even more particularly, MSWE requires the highest possible
grade in math, not only applied CS but the whole math specialization. In MSWE,
the SWE part is logical per se, but the underlying domain is also math. So, the
domain and engineering are both mathematical, unlike, for example, Medical SWE,
where you always need much math to *engineer* the software —like any other
software— but the domain requires little math.

SWE requires scientific research, which is a transitive property since requiring
much math implies research, too. The same applies to ML, so one more time, here
I am doing my work.

The collection of points about optimizing datasets with symmetries from the MIT
article helped articulate further the theoretical concepts I applied in the last
months to my works.

## Domain Optimization by Invariant Detection

I found an article in MIT News about symmetries in machine learning datasets and
how they can proportionally and exponentially optimize them by applying
mathematical principles I emphasized in my last works. This opened an
opportunity to expand on these essential concepts by relating the MIT article in
the ML field to my articles in FP terms.

I restated the importance of taking FP concepts like **sum/refinement/isomorphic
types, GADTs, and type families/classes** to leverage domain patterns as an
efficient design without redundancies.

I reemphasized the concepts of **expressions, factorization, orthogonality,
perpendicularity, dimensionality, partitions, symmetries/patterns/invariants,
and engineering-grade software**.

Whenever you find an invariant, you can apply its underlying transformations
without changing the property. Such pattern detection skills are leveraged for
optimizing domains, thus **helping reach the engineering grade since engineering
is a function of its domain**.

Newer extraordinary undertakings will always be available for MathSwe, like the
emerging geometric deep learning field, so the mathematical domain and the
engineering grade is always a commitment to keep the MSWE industry leadership.

## References

[1] [How symmetry can come to the aid of machine learning.](https://news.mit.edu/2024/how-symmetry-can-aid-machine-learning-0205)
(2024, February 5). MIT News | Massachusetts Institute of Technology.

[2] Briones, T. (2024, January 15).
[Removing Record Redundancy with Sum and Refinement Types.](/removing-record-redundancy-with-sum-and-refinement-types)
Blog | Math Software Engineer.

[3] Briones, T. (2024, January 15).
[Factorizing the Duplication with Sum Types.](/removing-record-redundancy-with-sum-and-refinement-types#factorizing-the-duplication-with-sum-types)
Removing Record Redundancy with Sum and Refinement Types | Blog | Math Software
Engineer.

[4] Briones, T. (2024, January 4).
[Designing the Angle Geometry for an Oriented Segment.](/designing-the-angle-geometry-for-an-oriented-segment)
Blog | Math Software Engineer.

[5] Briones, T. (2024, January 4).
[High-Level Angles.](/designing-the-angle-geometry-for-an-oriented-segment#high-level-angles)
Designing the Angle Geometry for an Oriented Segment | Blog | Math Software
Engineer.

[6] Briones, T. (2024, January 4).
[Engineering Geometry Languages in Haskell with Insights.](/designing-the-angle-geometry-for-an-oriented-segment#engineering-geometry-languages-in-haskell-with-insights)
Designing the Angle Geometry for an Oriented Segment | Blog | Math Software
Engineer.
