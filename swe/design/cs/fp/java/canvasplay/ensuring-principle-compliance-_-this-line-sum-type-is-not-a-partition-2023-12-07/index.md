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
