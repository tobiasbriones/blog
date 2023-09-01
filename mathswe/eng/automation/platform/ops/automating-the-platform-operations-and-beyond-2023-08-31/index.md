<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Automating the Platform Operations and Beyond (2023/08/31)

## Previous Article Operations

I started using GitHub pages with Jekyll[^1] and the minimal theme at the
beginning since it was the most available.

[^1]: Jekyll is a popular tool that builds static websites from Markdown and
    available for GitHub pages

I've published several articles of different kinds and have others unpublished
that I can use for proof of concept so I can check the newer designs fit well in
practice.

With that, I've been experimenting by manually deploying the content to the
`gh-pages` branch.

Now that I've found concise guidelines in practice, I'm ready to automate the
workflow.

It's not an easy task: there's a lot to automate, many bugs and side effects
I've fixed, complex definitions, and many parts of the system are still
temporal.

I always try to avoid over-engineering, so for example, I'll obviously get rid
of Jekyll and many other mundane tools[^2] (temporal parts), but now, I still
have to support them, so I have to check whether the tooling to develop and
test[^3] will return the investment.

[^2]: Jekyll has given me a lot of trouble regarding side effects
    (some articles ending with "x" don't build, etc.), watch mode doesn't work,
    Ruby dependencies, customization, etc., and I don't need mere static sites
    anymore, so I better move on and off Jekyll soon

[^3]: When developing software, repetitive manual testing or automated testing
    of something you will get rid of later introduces a lot of overhead, so you
    have to avoid both under-engineering and over-engineering

Many details are still unclear in this early stage, so I still haven't formally
opened a repository to work on these ops tools. I'm not fully sure yet where
they will belong in the end. This is due to other tools that I will create, so
all of them are barely starting to take shape.

Eventually, after validating many processes, I'm ready to start automating some
parts of the system.

## Development of a CLI Tool

In order to automate my common workflow on mathsoftware.engineer I needed to
develop a CLI tool to write some commands to operate on the article's source
code all the way to production.

### Kotlin and FP

One of the favorite technologies for this implementation was Kotlin for various
reasons.

The developing tooling is not stable yet and will not be for a while since this
will be part of something big that is not well-defined in practical terms yet.

For CLIs, Rust[^4] would be my first choice, but it's too complicated for this
stage of the system, so I need something faster to develop.

[^4]: I've been using Rust to automate Ubuntu system installation instead

I could use Purescript or Haskell to build a robust DSL, but that would also be
over-engineering for this stage too.

A convenient tool for my familiarity and its FP support is Kotlin since Java
was my first language, and I'm well versed all over these ecosystems.

The JVM isn't convenient for CLIs or GitHub actions tasks due to VM startup and
overhead. Getting crazy about GraalVM or Kotlin native wouldn't make a lot of
sense either. Since performance optimization is not a requirement (yet), I won't
bother about this concern.

As said, I'd use Rust which is appropriate, but I ended up choosing Kotlin
(JDK 19) because it's a great balance among all the constraints. Furthermore, I
can use the Arrow library to use a decent approach to FP, that is, my code and
ideas can still be future-proof this way.
