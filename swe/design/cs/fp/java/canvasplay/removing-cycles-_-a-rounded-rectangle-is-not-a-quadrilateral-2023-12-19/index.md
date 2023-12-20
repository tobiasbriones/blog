<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Removing Cycles: A Rounded Rectangle is Not a Quadrilateral (2023/12/19)

![](removing-cycles-_-a-rounded-rectangle-is-not-a-quadrilateral-2023-12-19.png)

---

## This Quadrilateral Sum Type is not a Partition

As an initial design in the canvas playground, I sent the rounded shapes like
rounded rectangles as part of their corresponding polygon. So, a round rect is
vaguely considered a rect, while a round triangle is vaguely considered a
triangle, etc.

That way, a 4-gon/4-side polygon/quadrilateral can be designed as the
union `Rectangle | RoundRectangle`, but it must not be defined so. The reason
to stay away from this general design flaw has analytical roots and can silently
occur in languages like Java.

`A Rounded Rectangle must not be a Quadrilateral | An Initial Design of 
Quadrilateral Shapes`

```java
public sealed interface Quadrilateral extends Shape {
    record Rectangle(
        double width,
        double height,
        double cx,
        double cy
    ) implements Quadrilateral {
        @Override
        public double area() { return width * height; }
    }

    // TODO wrong design 👎🏻
    record RoundedRectangle(
        Rectangle rectangle,
        double arcX,
        double arcY
    ) implements Quadrilateral {
        @Override
        public double area() {
            // TODO take borders into account if necessary, not a useful
            // feature so far
            return rectangle.area();
        }
    }
}
```

I didn't implement the `area` property because, with proper composition,
everything gets pretty trivial, so I save the effort on these occasions when I
know I'll perform major refactorizations and redesigns.

Even though, as said in
[Ensuring Principle Compliance: This Line Sum Type is Not a Partition](/ensuring-principle-compliance-_-this-line-sum-type-is-not-a-partition-2023-12-07),
we can show that the above `Quadrilateral` sum type *seems to be a partition
(but it's not)*[^1], there are always more design principles to comply like
**acyclicity**. The fact that it's not a partition leads to cycles.

[^1]: A rectangle can't be rounded, and a rounded rectangle can't be straight,
    thus the subsets of our `Quadrilateral` sum type are(?) disjoint and induce
    a partition of our quadrilateral shapes in this case, **if it weren't by the
    issue that `RoundRectangle` is *composed* of a `Rectangle`**

**The `Quadrilateral` sum type is not a partition** since `RoundRectangle` is
composed of `Rectangle`, which is a "sibling" product type of the union, so
**the records of the sum type are coupled, making them non-orthogonal or
dependent**.

The failure of `Quadrilateral` not being a partition is **the first flaw that
must be immediately redesigned for fixing** or else brings interesting issues
like cycles.

Even if it could make sense the idea to put a rounded shape close to its
original shape, it's a fake friend insight.

First, **a rounded rectangle is not a rectangle** as said in
[Designing a Rounded Rectangle Against Pragmatic Misconceptions](/designing-a-rounded-rectangle-against-pragmatic-misconceptions)
—where I also write about these fake friends— and **it's neither a
quadrilateral**. It doesn't have exactly "four sides" so it doesn't belong to
the `Quadrilateral` sum type. It has to be **a different type of composed 
shape**.

The above theory and articles show us not only how a rounded rectangle is not a
rectangle, but further, even if we're fool enough to think so, **the FP design
will tell us it's wrong and how to fix it** (something impossible in ordinary
poorly-engineered paradigms like imperative or Java OOP).

From the pre-initial (experimental) design of `Quadrilateral` biased with fake
friend popular concepts, I worked out the theoretical framework in various
articles to sustain design flaws that also happen generally, such as failing to
design sum types correctly when they don't form a partition, and even further
consequences of this, like the creation of dependent records that can easily
become a cycle or sink.

### Incorrect Coproduct Design Leading to Cycles

The more advanced consequence of why this design is also wrong is a cycle that
has to be removed.

I defined a
[Rounded Rectangle](/designing-a-rounded-rectangle-against-pragmatic-misconceptions#rounded-rectangle)
to help clarify the design I'm facing here.

Imagine I want to employ the fake common sense idea by leaving the rounded
rectangle in `Quadrilateral`.

![](product-types-of-the-union-are-not-orthogonal.svg)

The flaw is clear: **`RoundedRectangle` is a `Quadrilateral` composed of a
`Rectangle`, which is also a `Quadrilateral`**. Therefore, we have a design
cycle. Records (i.e., `RoundRectangle` and `Rectangle`) of the sum type must be
orthogonal, but `RoundedRectangle` depends on `Rectangle`, so `Quadrilateral`
fails to be a partition by not having mutually disjoint subsets.

When not ensuring principle compliance, we can leave behind severe design
issues. In this case, having a record depending on its "sibling" proves how that
can lead to a cycle flaw, and you don't want to mess with cycles, as I've
addressed in other articles.

## Incoherent Design of Rounded Shapes

If we need to expand the support later, the `Rectangle` field
of `RoundedRectangle` will turn into a general `Quadrilateral` to have rounded
rhombuses and others. This recursive definition is similar to that of a `Tree`,
but that doesn't have anything to do with a rounded shape or
`RoundedQuadrilateral`.

`Matching a Quadrilateral`

```java
static QuadrilateralDrawing of(
    GraphicsContext ctx,
    Quadrilateral quadrilateral
) {
    return switch (quadrilateral) {
        case Rectangle rectangle -> new CanvasRectangleDrawing(
            ctx,
            rectangle.width(),
            rectangle.height(),
            rectangle.cx(),
            rectangle.cy()
        );
        // ... Other real Quadrilaterals ... //

        // This ↓ is not a Rectangle but "looked" like one according to //
        // "popular" opinion 🤪                                        //
        case RoundRectangle(var rectangle, var arcX, var arcY) ->
            //                    ↑                    //
            // If you were to pattern-match a general  //
            // Quadrilateral, you fall into a loop.    //
            //                                         //
            new CanvasRoundRectangleDrawing(
                ctx,
                rectangle.width(),
                rectangle.height(),
                rectangle.cx(),
                rectangle.cy(),
                arcX,
                arcY
            );
    };
}
```

Then, when matching the `rectangle` field of `RoundedRectangle`, we might as
well be matching a `Quadrilateral` again from the beginning, but this
second time will never make sense because we need *one* shape not a recursion of
them, *proving that `RoundedRectangle` must not belong to `Quadrilateral`, which
is then a flawed sum type*.

In short, making a definition where we want rounded quadrilaterals (not only
rectangles) like:

`The Initial "RoundedRectangle" Design Diverges when Scaling
| Incoherent Design of Rounded Shapes`

```
sealed interface Quadrilateral {
    record Rectangle(...) ...
    record RoundedQuadrilateral(Quadrilateral quadrilateral, ...) ...
}
```

Provides **a technically correct design** —unlike the one depicted in the
[diagram](#incorrect-coproduct-design-leading-to-cycles) which is incorrect.
Now, it kind of resembles the recursive structure of a tree, but **it's
incoherent to the domain** since the structures of a round shape and a tree have
nothing to do 😂. I might as well visualize this structure for fun to see how
it's represented —you know, I find mysterious patterns.

Getting aware that `Quadrilateral` is flawed is direct proof (from the first
statements above), and getting so advanced until this point is something I
wanted to emphasize to denote how the design flaws scale so much.

I'll say in advance that fixing this design is just about defining rounded
shapes in a more concrete package importing basic shapes. Rounded shapes are
compositions of basic ones, so a rounded rectangle is at the wrong level of
abstraction here.

Even if I know the answers, I keep finding out insights about these cycle flaws
and type systems to document conclusions of these design and learning
activities.

## FP-First Approach to Get the Design Right

In Haskell, this algebraic lack of principle compliance is much easier to
understand and get right quickly.

A sum type is a *type* constructor, but the records are *data* constructors 
(functions).

`Flawed Quadrilateral in Haskell`

```haskell
data Quadrilateral
    = Rectangle { width :: Double, height :: Double }
    | RoundedRectangle { rect :: Rectangle, arc :: Double }
```

Fails to compile with error:

`GHC Error of Flawed Quadrilateral`

```
Not in scope: type constructor or class ‘Rectangle’
Suggested fix:
  Perhaps you intended to use DataKinds
  to refer to the data constructor of that name?
```

So, `Quadrilateral` *is a type*, while `Rectangle` and `RoundedRectangle` *are
data* constructors. This way, `Rectangle` can't be defined as a field
of `RoundedRectangle` because it's a data constructor, not a type constructor (a
field of a record has to be a type, not a data constructor).

Data constructors are not types but values, while the sum type like
`Quadrilateral` is the type [1].

The error suggests to enable `DataKinds` since we know the data constructor
`Rectangle` (i.e., a function) has type `Double -> Double -> Quadrilateral`.
Recall **it has a type, but it isn't one**. If you enable it via the
pragma `{-# LANGUAGE DataKinds #-}`, you'll still get the error:

`Cyles Disallowed in the Same Group of Declarations`

```
Data constructor ‘Rectangle’ cannot be used here
  (it is defined and used in the same recursive group)
```

It means you're coupling (using) a data constructor (physical, not a type)
belonging to the same group of declarations (the sum type), thus
**leading to a cycle in your design you must fix**.

Notice how Haskell's strong **type system disallows compiling these cycles**
since a (soft) type constructor is required instead of a (hard) data constructor
for the record components.

Recursion per se is not the problem when we have design sinks or cycles. The
difference is that recursion *defines* something (like a `Tree`) softly with
relations, while **cycles are a problem because they're hard/physical**
(creating coupling), so they use recursiveness in *data* constructors or also
module/package circular dependencies as I said in
[Removing Cyclic Dependencies, Java vs Go (2023-05-28)](/removing-cyclic-dependencies--_--java-vs-go-2023-05-28).

Now, I'll go back to the tree example just to clarify that recursion is not the
problem with sinks (**do not fear recursion**) but *coupling*.

`Defining a Binary Tree Recursively in Haskell`

```haskell
data BinTree a = Leaf | Node a (BinTree a) (BinTree a)
```

Recursion defines complex abstractions declaratively via a *pattern*. The
`BinTree` is a (soft) *type*, so something potentially infinite like a tree can
be exactly enclosed in the above definition. If you start using imperative
recursion with hard constructs like data constructors, structs, modules, etc.,
you quickly create cycles *because of the hard coupling*. These hard constructs
have nothing to do with the recursive nature of math —which is declarative.

To complement the above idea, recall that *coupling disallows orthogonal
concepts, thus preventing partitions, etc.*

It's as if soft recursion "travels at infinite light speed" because "it's
weightless like light." On the other hand, when you "add weight," you trade
the "speed" to be "slower than light," so that's an analogy of how physical
coupling (or monoliths, too) "draw so much energy" and "break the pattern."
Then, broken patterns lead to imperative workarounds, etc.

Having an FP-first mindset enables us to wisely employ any other programming
language.

Haskell's type and data constructor concepts helped clarify the role of sum and
product types. Its type system also helped prevent the compilation of cycles,
leaving insight into how to proceed better in Java and further system designs.

### Purescript Follows the Same Principle

The sum type in Purescript is expected to behave the same as Haskell since
they're the same family of languages.

`Flawed Quadrilateral in Purescript`

```purescript
data Quadrilateral
    = Rectangle { width :: Number, height :: Number }
    | RoundedRectangle { rect :: Rectangle, arc :: Number }
```

Failing with error `Unknown type Rectangle`.

Recall that `Quadrilateral` is flawed because its product type
`RoundedRectangle` *depends* on `Rectangle`, which is another type of its sum
type. This coupling makes them dependent, so `Quadrilateral` is incorrectly
defined by not being a partition with mutually disjoint subsets. A robust
functional language won't even compile this nonsense, but in ordinary
languages, you have to be alert since it won't be so obvious.

The product types are not even normal types, but *variants* of *their* sum type,
while **the actual type is the sum type**.

### FSharp Experiment

After investigating the robustness of Haskell and Purescript, I tried another
functional language with ADT support, like F#.

`Flawed Quadrilateral in F#`

```fsharp
type Quadrilateral
    = Rectangle of width : double * height: double
    | RoundedRectangle of rectangle: Rectangle * arc: double
```

Failing with error `The type 'Rectangle' is not defined`.

The principle is the same as separating type constructors off data types: "The
case identifiers can be used as constructors for the discriminated union
type"[2].

The F# ADTs proved similar functioning principles, like the other functional
languages disallowing nonsense constructs at compile time that just pass
completely unnoticed in ordinary languages like JVM Java-biased languages (the
"competition").

## References

[1] [Constructor - HaskellWiki](https://wiki.haskell.org/Constructor).

[2] KathleenDollard. (2021, September 15).
[Discriminated Unions - F#. Microsoft Learn.](https://learn.microsoft.com/en-us/dotnet/fsharp/language-reference/discriminated-unions).
