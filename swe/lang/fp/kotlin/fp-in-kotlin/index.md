<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# FP in Kotlin

## Pipe Operator

There's no support for the pipe operator `|>` in Kotlin, so we have to come by
with a custom and clean implementation for this function.

### Defining a Pipe Operator

Next, one consistent and clean implementation is given for a pipe operator in
Kotlin.

First, I wanted to use the `|>` pipe symbol commonly used in functional
languages, but `>` is not a supported character: `Name contains illegal
characters: >`.

Then, I tried to use the other common `|` pipe symbol with no full success:
`Name contains characters which can cause problems on Windows: |`.

To avoid issues, I either had to design a simple symbol or use a verbose `pipe`
operator name.

So, I took my design from
[How I Standardized Hyphen and Pipe Symbols on File Names](how-i-standardized-hypen-and-pipe-symbols-on-file-names)
where I designed the standards for (among others) the pipe operator on file
names. Notice that file systems also require simple symbols to work with, so
the standard from my previous article was what I was looking for.

Now that **the pipe operator symbol is established to `---`**, the
implementation is next.

It's required to employ:

- [Infix Functions](https://kotlinlang.org/docs/functions.html#infix-notation).
- [Lambdas](https://kotlinlang.org/docs/coding-conventions.html#lambdas).
- [Basic Generics](https://kotlinlang.org/docs/generics.html).

It's useful for the given example that shows the operator usage:

- [Data Classes](https://kotlinlang.org/docs/data-classes.html)
- [Value Classes](https://kotlinlang.org/docs/inline-classes.html)
- [String Interpolation](https://kotlinlang.org/docs/idioms.html#string-interpolation)
- [Basic Regex](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/to-regex.html)

First, the `---` operator is defined:

```kotlin
infix fun <X, Y> X.`---`(f: (X) -> Y): Y = f(this)
```

<figcaption>
<p align="center"><strong>Definition of the Pipe ("---") Operator</strong></p>
</figcaption>

Where:

- `---` is a special function name escaped within backticks "``" that
  **denotes the "pipe operator"** as described before.
- Two generics `X` and `Y` are defined to **represent the LHS and RHS** of the
  operator.
- The `infix` notation is used on the extension function `---` defined for
  all $$x \in X$$. There are two parts here:
    - The **extension function**.
    - The **infix which provides the syntax sugar**.
- $$\forall x \in X, y \in Y$$, the lambda `f: (X) -> Y` is defined as the
  **operator argument**. So, we have:
    - The constant (LHS) is given by $$X$$.
    - The function (RHS) to be applied is given by $$f:X \to Y$$.
- `---` **returns** `Y` —result of applying $$f$$ to some $$x$$.
- `---`'s **image is defined as `f(this)`** where `this` is an element of `X`.

That was the definition of the pipe operator.

Now, to address a concrete example to use this new feature, I implemented a
basic DSL for an `Article` domain type.

```kotlin
data class Article(val title: Title, val content: String)

@JvmInline
value class Title(val value: String) {
    override fun toString(): String = value
}

val title: (String) -> Title = { Title(it) }
```

<figcaption>
<p align="center"><strong>Definition of an "Article" DSL</strong></p>
</figcaption>

So, to test the code, I will add a user input title that is not cleaned, some
content, and I'll also define more functions with **transformations**, so we can
*pipe them*.

`fun main | Main.kt`

```kotlin
val inputTitle = "FP in Kotlin: Defining a Pipe   Operator  "
val inputContent = "Lorem ipsum dolor sit amet..."
```

<figcaption>
<p align="center"><strong>Sample User Input for Example Snippet</strong></p>
</figcaption>

