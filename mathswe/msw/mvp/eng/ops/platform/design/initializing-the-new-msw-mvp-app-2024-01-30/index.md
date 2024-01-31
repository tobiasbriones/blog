<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Initializing the New Math Software MVP App (2024/01/30)

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
and have been using successfully. The file/project name is piped to the MVP
context just like I've been doing with EPs and CPs. MVPs are mono repositories
containing internal MVPs because of their volatile and centralized nature.

MVP projects must be explicitly piped with the `---mvp` context to clarify their
purpose.

The new MVP project has the structure:

`Opening Operations at the Math Software MVP | Initial Project Structure`

```
mathsoftware---mvp
├── math.software---mvp
│   └── README.md
└── README.md
```

Since the mono repository `mathsoftware---mvp` is the only MVP in the
organization by the new standard, the rest of the MVPs will be hosted there.

To start operations, I just introduced the **Math.Software MVP** (i.e., the web
app) in the `math.software---mvp` directory.
