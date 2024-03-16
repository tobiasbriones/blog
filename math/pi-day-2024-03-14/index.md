<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Pi Day (2024/03/14)

---

The uppercase letter **Π (capital pi)** is particularly used for denoting the
product operator in math. Sets have the *cartesian product*, and we can apply
this set algebra to computer science via product types. The symbol Π appears in
math, and via type theory we can employ it in ATDs (Algebra Data Types) like
product types.

Similarly, we can denote the summation via the uppercase sigma letter (Σ). Thus,
we can denote sum types (like enums) under Σ and product types (like tuples or
records) under Π.

Let $$A, \, B$$ be types.

$$S = A + B = \sum_{\{ T_j \} \in \{ A, B \, \} } T_j$$

$$P = A * B = \prod_{\{ T_j \} \in \{ A, B \, \}} T_j$$

The disjoint union or sum type $$S$$ induces a partition of $$A, \, B$$. The
product type $$P$$ is a basic type that can be defined under the Π symbol and
defines the pairs $$(a, b)$$ where $$a \in A \land b \in B$$.

A program defining data types for `Color` components and values can depict basic
types like these.

![](sum-and-product-types.svg)

An `RGB` color can be partitioned into orthogonal components, `R`, `G`, and
`B`. So `S` defines a (useful) partition of how a `Color` can be created. On the
other hand, `Color` is a product type defining the set of all colors `(r, g, b)`
where $$r \in R, g \in G, B \in B$$.

The theoretical concepts above can be efficiently implemented in a purely
functional language like Haskell.

`Expressing Math Concepts like ADTs in Functional Languages | Defining Sum and Product Types in Haskell`

```haskell
newtype R = R Int  -- [0, 255]
newtype G = G Int  -- [0, 255]
newtype B = B Int  -- [0, 255]

data ColorComponent = Red R | Green G | Blue B

data Color = Color { red :: R, green :: Green, blue :: Blue }
```

The product type `Color` is defined as a record or nominal tuple to add fields
enhancing the underlying language semantics. Further, notice that a product type
is a trivial sum type with one only variant. The duality mentioned shows how Π
is ubiquitous in mathematics and computer science.

---

## References

[1] Johan Wästlund (2007) An Elementary Proof of the Wallis Product Formula for
pi, The American Mathematical Monthly, 114:10, 914-917, DOI:
10.1080/00029890.2007.11920484

[2] Bove, A., & Dybjer, P. (2009).
[Dependent Types at Work](https://doi.org/10.1007/978-3-642-03153-3_2). In
Lecture Notes in Computer Science (pp. 57–99).

[3] [Dependent Product Type](https://ncatlab.org/nlab/show/dependent+product+type).
nLab.

[4] [2.1 Product Types \| Lecture 04.1: Algebraic Data Types and General Recursion](https://stanford-cs242.github.io/f17/assets/slides/04.1-adt-recursion.pdf).
CS 242: Programming Languages, Fall 2017 | Stanford.
