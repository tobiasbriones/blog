<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Ensuring Principle Compliance: This Line Sum Type is Not a Partition (2023/12/07)

![](ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07.png)

---

## Initial DSL for Drawing Shapes in Canvas Play

Lately, I designed a small math DSL in Java and JavaFX for drawing high-level 2D
shapes in my new project
[Canvas Play](https://github.com/tobiasbriones/canvas-play), which I'm currently
preparing for the first PR including the initial design of this DSL.

I don't plan to use Java in the long term (Kotlin, HTML5, Purescript, etc. are
coming next) since it's more of an exercise language for me. You know, the
"brain muscle."

Designing DSLs requires being a domain expert (i.e., mathematician) and granular
design effort.

Exploiting Java for anything out of the mundane "corporate" software is a severe
challenge, so it's on me. I'm always taking challenges. Of course, it'll never
be a good language for non-conservative programs, but I need the exercises,
hence canvas *play*.

The initial design is currently done, but there are several flaws in this stage
that I need to address.

I always have a paradigm vision in my head. I think about how to solve problems
properly (and trivially) in Haskell and then write the Java code. Since I'm
proficient in Java, I know how to do it "the Java way 😖," after first stating it
in an FP mindset.

This is when the adage of knowing FP turns you into a better programmer takes
place. Of course, I'm a math software engineer and take this to its finest.

So, the final phase of the initial DSL design in Canvas Play has left some
design concerns I should share.

## Sum Types must be Partitions

Complying with the principles is essential to leverage a powerful DSL that
inherits mathematical properties, such as following the potential partitions
created when we define sum types.

A sum type for the set of all triangles $$T$$ has to be a collection of subsets
of triangles, and the sum (union) of them is $$T$$, but they also have to create
a partition.

That is, if `T = Equilateral | Isosceles | Scalene`, then:

$$\bigcup_{A \in T} A = T$$

And they're disjoint subsets: $$\forall A,B \in T \implies A \cap B =
\emptyset$$.

Remember that, "Two sets are called disjoint if, and only if, they have no
elements in common"[1].

This is a form of polymorphism where the subsets of the partition (the product
types like `Equilateral`) share the same *physical*[^1] properties, so **we can
simplify (or homogenize) our problem** if we need to work with equilateral
triangles in specific, for instance.

[^1]: The same *physical* properties because they're *product types* or records

An equilateral triangle has its three sides equal, an isosceles two sides and a
scalene triangle has no side length equal. So, `T` is a partition of the set of
all triangles, because an `Equilateral` type can't be `Isosceles`, etc. That is,
the subsets or product types *are mutually disjoint*.

Unlike some kinds of open polymorphisms (like ad hoc), sum types must be created
**to partition a given set** you know well. I mention this because of
*exhaustive pattern matching*.

The class of equilateral triangles allows us to work with simplified equations
and assumptions. Similarly, we can define a triangle as a sum type
(partition) of triangles according to their angle:
`T' = Acute | Right | Obtuse`. Thus leveraging other properties induced by the
equivalence classes of the partition (e.g., can use the Pythagorean theorem for
the class of acute triangles).

They're all triangles whether you define them using `T` or `T'`. The difference
is **the property you need to optimize**, like side lengths or angles.

Now, I mention that sum types *should* be partitions or consist of mutually
disjoint subsets (assuming they sum up an underlying set) because the compiler
won't check for that. It's up to your definitions as the designer. So, **sum
types can be created with overlapping subsets, but you have to avoid it**.

If your subsets overlap, two of them may share elements in common, so
implementations (expressions) will be more generic and thus bloated. **If
they're disjoint, you can and know how to optimize since you leave the overhead
behind and decouple the problems**. Then decoupling leads to composition, etc.,
that is, engineering-grade software.

Simplifying by defining sum types for a given set makes implementations more
efficient as well, since equations become trivial for specific problems
(that could be executed many times), unlike general bloated equations.

Remember when the `sin` and `cos` become zero or one, and the expressions become
trivial from the math courses? That's exactly what I'm talking about. Now,
*imagine those simplifications have performance implications*. Hence, **we
engineer math software** —software that inherently comes from math.

If subsets are disjoint, the sum type creates a partition. From this, we can
also infer that *the sum type is an equivalence relation* since **all partitions
induce an equivalence relation** (reflexive, symmetric, and transitive) [2].
**All equivalent relations also induce a partition** on the underlying set [3].

The fact of being a partition (union of mutually disjoint subsets or product
types) **makes sum types Algebraic Data Types (ADTs): operations like sums
(unions), product (cartesian), and properties like equivalent classes are
leveraged to do the algebra**.

This is all basic theory all (competent) software engineers must know well. If
you have a math major and practice real-life SWE, you happen to be on top
theoretically, as that's the breading air of solving problems efficiently. Then
this is not an obstacle for you because you already understand the math
language[^2].

[^2]: Talking to someone who's not a mathematician (even a terrible one, at
    least) is never the same, as they will never speak the same language; between
    math majors, we understand each other because we are formally trained, so
    it's a feeling of being "in family," and I know this because math is a
    closure: you build math with math, you must be part of the closure

I highly recommend reading the references I left if you want to understand
Haskell classes or ad hoc polymorphism —study all about equivalent classes and
partitions.

Sum types are ADTs providing a kind of polymorphism that allows us to create
specific types of a given type with exhausting pattern matching. Even though its
product types can overlap, you must avoid it to create mutually disjoint
subsets, leading to leveraging the properties of partitions to apply ADTs
properly and employ their algebra more powerfully.

## References

[1] Epp, S. (2010). Discrete Mathematics with Applications (4th ed.).Section
6.1: Partitions of Sets.

[2] Epp, S. (2010). Discrete Mathematics with Applications (4th ed.).Section
8.3: Equivalence Relations. Theorem 8.3.1.

[2] Epp, S. (2010). Discrete Mathematics with Applications (4th ed.).Section
8.3: Equivalence Relations. Lemma 8.3.3.
