<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Initializing the Texsydo GitHub Organization (2024/09/04)

## Texsydo Web and FX Prototypes

The two prototypes are Web, which I use to deploy my articles, and FX, which
automates cover images for Web. After validating and developing them as internal
prototypes, I am now upgrading them to MVPs hosted on the new Texsydo GitHub
organization. Their DSLs aim for the engineering grade, so this MVP space is
urgent to move them forward.

I've written about the technicalities in previous blogs.

- [Automating the Platform Operations and Beyond (2023/08/31)](/automating-the-platform-operations-and-beyond-2023-08-31).
- [Text Ops: New Add PR Command (2024/03/23)](/text-ops-_-new-add-pr-command-2024-03-23).
- [Testing the Texsydo FX Prototype (2024/09/03)](/testing-the-texsydo-fx-prototype-2024-09-03).

Texsydo Web is a tool that automates and standardizes the development of
mathematical articles or documentation with a web target. Texsydo FX compliments
the automation of standard graphical content like cover images and rendering of
mathematical models. They need their upgrade to MVP to formalize results as a
new MathSwe product providing Applied MSW essentials.

## Upgrading the Logo Draft

I created an idea for the Texsydo logo a few months ago, so I needed to compose
it thoughtfully to initialize the Texsydo project formally. As always, I take
the essential idea of Piaxid and MathSwe to give semantics to the logo elements.

I opened up the logo draft and found its look was incipient with many details,
but I already had the base concept.

![](images/older-texsydo-logo-draft.png)

I had to take two new colors to match the project semantics, recalling that it's
about how MathSwe standardizes articles.

![](images/editing-the-texsydo-logo.png)

Like all the current MathSwe logos, I still don't apply the smooth curve to the
sharp boundaries to avoid losing time with Photopea. In fact, Texsydo FX is
supposed to accept a DSL to generate the exact *representations* of these
mathematical graphics. So I don't waste time making them manually.

![](texsydo.svg)

It has the "axiom" enclosed by "components" or "widgets" you find in my smart
articles, the left navigation to expand to the "abstract," and the right
navigation to diverge to the outside world. The top and left are abstract, the
bottom is concrete, and the right is the system boundary. All colors are
semantic, obviously.

MathSwe logos are **mathematical art** I create. The Texsydo logo particularly
denotes the **mental model** of *a graph* I always have in mind to know where I
am, in abstract terms. It's like a pilot without visuals who can still navigate
and understand where and what they are doing.

I've conceptualized two kinds of articles, the normals with left navigation like
this blog article, and minimalistic ones for presentations or landings, like MSW
home. The focus always optimizes for **the center**.

You read rich articles from left to right and top to bottom, while presentation
ones are centered and from top to bottom. Notice how the "axiom" keeps centered
when you "close away" the "left navigation." Logic itself tells me if what I do
makes sense. In other words, the "center" is the "left" of rich articles (LTR)
and the "center" of minimalistic ones.

Hence, we can claim that **the axiom is invariant** as it's always the
"center" of *any* article.

The Texsydo logo is, like all MathSwe logos I've devised, mathematical art that
represents the robustness of the MSW and MSWE concepts. The new logo lets me
start producing Texsydo with its coming MVPs, Web, and FX. As a fun fact, I
initially used Photopea to draw, but it's Texsydo FX, the one that will engineer
mathematical art.

## New Projects

As I've recently introduced, the first projects, Texsydo Web and FX, are ready
to be featured in the Texsydo space as the first MVPs. Given their current
prototype stage, I just have to progressively migrate their code from the
throw-away branch to the actual repository.

Texsydo FX is the fastest to turn into a product as its prototype is only about
3 files of code. Conversely, Texsydo Web is quite complex, and I've written its
prototype for over a year according to needs.

The Texsydo FX and Web prototypes will make it to the first Texsydo MVPs and the
next MathSwe products.

### OSS License Model

Texsydo is an Applied MSW that follows the same licensing idea as Repsymo,
considering I still must update Repsymo's MVP license from GPL to AGPL.

The specs or DSLs will be permissive BSD-3, as usual, while the special
implementations will be AGPL as they provide concrete products.

The Texsydo MVP repository is currently AGPL, so it takes the copyleft boundary
to cover any kind of code.

## Moving Texsydo Forward as a New Applied MSW

While Texsydo Web is essential to creating mathematical text and deploying it to
web articles, Texsydo FX helps it by automating graphical models, like MathSwe
cover images. Mathematical text is ubiquitously applied, so Texsydo is an
integral project to MSWE.

I just finished the Texsydo logo now that I'm formalizing this project. As
always, I apply Piaxid/MathSwe abstractions.

Piaxid or MathSwe logos are mathematical arts in which Texsydo FX plays an
essential role since, for example, I'll be able to write the logo definitions in
Texsydo FX (DSL) to create this abstract art and leave generic tools like
Photoshop behind.

The initialization of the Texsydo GitHub organization opens the OSS space to
work on Texsydo projects, like the other MathSwe organizations I've worked on.
The Texsydo Web and FX prototypes will be the first to make it to MVPs, which
will provide new MathSwe products enriching applied MSW.
