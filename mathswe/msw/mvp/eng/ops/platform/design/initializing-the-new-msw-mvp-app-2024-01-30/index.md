<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Initializing the New Math Software MVP App (2024/01/30)

Previous theoretical and practical works are being iterated for reaching
production at math.software with the new MVP (Minimum Viable Project) web app
offering high engineering standards and domain-specific mathematical software.
While MVPs are pragmatically developed, engineering-grade counterparts will
be internally ascertained.

## Fast Paced Prototype

I started working on the app prototype in a single `index.html` file with the
styles I unofficially developed for MSW Engineer. I added upcoming styles and
some JS logic for the fractal animation with its special canvas parallax effect.

I (unofficially) deployed the prototypes to production and have been setting
priorities for the overwhelming projects, updates, and research I've had lately.

I'm urged to go to production since, as the founder, I need a return on my
investment after so much slack time in the past due to many other situations
that arise while growing as a professional. Thus, that's what I mean about
fixing priorities at this moment to trade minor tasks and projects behind for
the most important projects involving MathSwe directly.

## Drafting new Engineering Standards

In one of my next blog drafts, I'm adapting the **MVP (Minimum Viable
Project)[^1]** concept and introducing **Pre-Engineering Grade** as well as
granular variants of software quality that branch in two: General SWE and
Specialized SWE.

[^1]: I changed the popular "Product" noun of the acronym to "Project" since the
    underlying structures refer more to the technical organization of projects

These standards allow special software versioning and quality inference in a
scalable and rigorous way.

The draft's getting extremely large due to the formalities I'm devising against
my experience and knowledge of math and traditional engineering, so it will take
longer to publish.

The MVP and Pre-Engineering-Grade (I also call "the boundary") concepts have
priority for me now to move forward on the operational project side (i.e., ROI)
while enriching the mathematical side of MathSwe in the background.

Since I must go to production now, I'll be using MVPs for MSW and Repsymo to go
the right way.

## Milestone with Logo Upgrades

I created the **Math** and exported (with more retouches[^2]) the **MSW
(Mathematical Software)** and **MSWE (Mathematical Software Engineering)**
icons I've been devising over the years since I worked on the **Piaxid**
concept. So, my logos have a formal sense I ascertained from so many years of
formal background and philosophy.

[^2]: I stopped working on the logos in Photopea since I'm working in the
    Canvas Play DSL for mathematical drawings and visualizations

The current logos are not official yet. I have to draw them with the Canvas DSL
I've also been designing, but since we're against the MVP development, it's
perfectly valid to add what I have for now and iterate.

## Web App

I created the `math.software---mvp` app with React 18, TypeScript 5 with SWC,
and Vite 5. I installed the `bootstrap-react`, `bootstrap`, and
`better-react-mathjax` dependencies. I'll write more about it in the next PR
blog.

## Official MVP Structure

After writing an extensive draft of the blog I mentioned earlier, I concluded
the standards for the repository name, semantics, and internal project
structure.

For example, I declared there can only be one MVP (repo or project) per GitHub
organization and must be named after `{ org_name }---mvp` with the pipe operator
[I standardized before](/how-i-standardized-hyphen-and-pipe-symbols-on-file-names)
and have been using successfully. The project name is piped to the MVP context
just like I've been doing with EPs and CPs. MVPs are mono repositories
containing internal MVPs because of their volatile and centralized nature.

MVP projects must be explicitly piped with the `---mvp` context to clarify their
purpose.

The new MVP project has the structure:

`Opening Operations at the Math Software MVP | Initial Project Structure`

```
mathsoftware---mvp       (monorepo)
├── math.software---mvp  (webapp)
│   └── README.md
└── README.md
```

Since the mono repository `mathsoftware---mvp` is the only MVP in the
organization by the new standard, the rest of the MVPs will be hosted there.

To start operations, I just introduced the **Math.Software MVP** (i.e., the web
app) in the `math.software---mvp` directory.

## Setting Up Deployments

I updated the Netlify deployment since I was testing
[the prototype I made in ~a day](#fast-paced-prototype)
with plain HTML. I set the repository with readme files up-to-date and status
badges.

Production will run in the premium APEX domain
[math.software](https://math.software). The MVP versions will be deprecated[^3]
when the engineering-grade counterparts become available. Therefore, the process
is efficient while the platform matures as per demand.

[^3]: The purpose of MVPs is to finish their lifecycle and replace them with
    engineering-grade counterparts in the long run, which shows you're having
    massive success

Staging is running in the [staging.math.software](https://staging.math.software)
subdomain deploying the `msw/staging` branch.

The deployment is established for a long while and supports the staging
environment as a plus. By opening deployment operations, my premium domain name
will start returning the investment 🎉🥳🎉.

## Expected MVP Quality

The MVP versions will have a "Mainstream/Mainstream+" quality grade according to
my current blog draft. That is quality near the "Pre-Engineering" grade or one
step behind the boundary between the engineering grade and general engineering.
In other words, two/three steps behind the engineering grade.

Notice by "engineering grade" I mean traditional engineering with rigor.

The app is aimed to be a PWA like Repsymo always was, so it must work offline
for the highest user experience. Since I also have experience with this, I'm
considering the tradeoffs. For example, adding the fonts and everything bundled
with the app instead of downloading them lazily via CDN, which involves adding
third-party copyright notices in the source code to comply with IP laws. I'll
manage it in further ops before the app is ready for one of the upcoming
releases.

MathSwe MVPs must be of high standards, showing that the grade of MVPs is
relative. Since the underlying field is MSWE, even a simple MVP must have great
mainstream quality, unlike ordinary MVPs that can consist of "runnable mockups."
Thus, by MVP in MathSwe, we can think of quality General SWE.

## Celebrating the Opening of the Premium App

This is the first blog about the new major project **MVP: Math.Software**
located in the **mathsoftware** GitHub organization 🎉🥳🎉.

As usual, I'm providing
[the `BSD-3-Clause` License](https://github.com/mathsoftware/mathsoftware---mvp/blob/main/LICENSE)
to the organization's MVP[^4].

[^4]: Remember that, from my new standard draft, one organization can have at
    most one MVP repository or project, so that is what I mean by "the
    organization's MVP"

The initial app will be released under version `0.1.0` as per standard[^5]. For
formality reasons, I declare a version `0.1.0-dev` (which is SemVer compliant)
in the project's `package.json` while in development of that version, so the
very last commit before release should be for removing the "dev" suffix since
the iteration is complete. That is, version `0.1.0` (without `-dev`) is left
exactly before release, so the version declared doesn't lie. The same applies to
any other version, so the information assigned is coherent.

[^5]: The initial version of SemVer software should be `0.1.0` in contrast to
    some projects I've seen using awkward `0.0.1` versions

Finally, I added the temporal styles I worked on for the current version of MSW
Engineer (the ones the blog is using right now). So, the new iterations will
start extracting plenty of value from previous works from the past year.

The upcoming news will be the work in the `msw` branch for adding the prototype
I wrote in plain HTML, so there will be something (minimal and validated ✔) to
deliver to production for the `0.1.0` version.

## Reaching Production with Informed Standards

I'm focused on deploying as many results involving MathSwe as possible without
compromising the engineering, academic, and brand components. I started with a
plain prototype of the math.software MVP. Eventually, all this led to a draft
for the next MathSwe standards to keep complying with the engineering component
I just mentioned.

The new standards are being employed, empowering more iterations to get previous
works more mature.

MathSwe MVPs are designed with a General SWE quality grade. They're stable for
users and well-designed artifacts. As the organization's vision, MVPs provide
early access while aiming to be replaced with their engineering-grade
counterparts, showing major maturity milestones.

The **math.software** app is an MVP that will reach production soon in `v0.1.0`
with minimal and stable features, allowing me to integrate past results
appropriately and exploit my premium domain, thus passing from theory to
practice once more.
