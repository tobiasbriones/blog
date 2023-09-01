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

[2]: Jekyll has given me a lot of trouble regarding side effects
    (some articles ending with "x" don't build, etc.), watch mode doesn't work,
    Ruby dependencies, customization, etc., and I don't need mere static sites
    anymore, so I better move on and off Jekyll soon

[3]: When developing software, repetitive manual testing or automated testing of
    something you will get rid of later introduces a lot of overhead, so you 
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
