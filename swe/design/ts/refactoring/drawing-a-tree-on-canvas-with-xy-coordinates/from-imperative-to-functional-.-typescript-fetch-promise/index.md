<!-- Copyright (c) 2022-present Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# From Imperative to Functional: TypeScript Fetch Promise

![From Imperative to Functional: TypeScript Fetch Promise](from-imperative-to-functional-.-typescript-fetch-promise.png)

This
[gist](https://gist.github.com/tobiasbriones/544350fc301ffe32c1dd64d2f6ec6d81)
shows how more functional code is superior to imperative code to
perform a simple fetch request to get a JSON value that models a tree data
structure to be represented later by `HTMLCanvasElement`.

Both versions are correct and do the same, the "functional" version still has
some imperative style because JS/TS is not a functional language but the
difference **is clear** again.

By noticing the:

👎🏻 return statement

👎🏻 ternary operator

👎🏻 error handling block

And lack of:

👉🏻 pipe operator

👉🏻 pattern matching

👉🏻 expressions[^1]

[^1]: Notice the stupid semicolons appear when the line of code is imperative,
    sounds familiar isn't it? yes, Rust 🦀

Then we have reasons why the "functional" snippet doesn't get **even better**.

Another keynote to notice is that imperative versions will lead you to add
multiple return statements which increment the code complexity making it
hard to reason, follow and pretty prone to error.

Also, notice how throwing from a `try-catch` block **is an antipattern** as it
becomes a `goto`[^2][^3].

[^2]: You can learn plenty of these details from IntelliJ IDEA inspections

[^3]: Understanding these details are what make you stand away from bad 
    programmers and make you a competent one

If TS had `try`-expressions (like Kotlin) the imperative version would get
much better regarding correctness and style, but **expressions are
declarative so functional** in the end 😋.

**The more you make a program better, the more functional it gets**, that's
because **FP is the only/original programming paradigm there exists as per
scientific concerns**, and all other paradigms are just cheap workarounds.

Another good one I know a lot from experience is that **the more I refactor code
to improve it the more domain-specific it gets**, and FP is clearly the natural
way to go for DSLs.
