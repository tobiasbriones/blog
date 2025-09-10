<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# DoRep for Jekyll V0.1.0 (2025/09/05)

## DoRep for Jekyll Migration from the Texsydo Web Prototype

It migrates the Jekyll side tooling from the Texsydo Web Prototype.

- [Encapsulating Jekyll Tooling of MSW Engineer \| DoRep for Jekyll (2025/08/14)](https://blog.mathsoftware.engineer/encapsulating-jekyll-tooling-of-msw-engineer---dorep-for-jekyll-2025-08-14).
- [Fix Client Ergonomics When Running Jekyll Internally \| DoRep for Jekyll (2025/09/05)](https://blog.mathsoftware.engineer/fix-client-ergonomics-when-running-jekyll-internally---dorep-for-jekyll-2025-09-05)

GitHub release at
[DoRep for Jekyll v0.1.0: Migrates Jekyll tooling from the Texsydo Web Prototype](https://github.com/texsydo/dorep-for-jekyll/releases/tag/v0.1.0).

The CLI tool will allow the ongoing Texsydo Web MVP migration to decouple the
Jekyll website generation from the Texsydo logic by providing a command to turn
a Jekyll blog into a static website ready for deployment.

## Website Generation Interface

DoRep for Jekyll initially provides a basic Kotlin CLI tool so Texsydo Web MVP
can call it.

The command in this release is `./dorep-for-jekyll build {src} {dst}`, and the
recommended way of running it is by building and running the binary, as given
by [the documentation](https://github.com/texsydo/dorep-for-jekyll/tree/v0.1.0)
and
[manual test](https://github.com/texsydo/dorep-for-jekyll/tree/v0.1.0/test/src)
of the project.

The build command generates a static website from Markdown, while the tool
integrates the MSW Engineer styles and logic, like navigation, footer, etc.

## Supporting Web Standardization and Deployment for the Texsydo Web MVP Migration

The initial release of DoRep for Jekyll encapsulates the web development part of
MathSoftwareEngineer, encompassing styles and JS logic. The implemented command
allows clients to generate a static website, ready for deployment, from an
intermediate Markdown Jekyll source, in which the tool includes the MSW Engineer
web standards out of the box.

These web standards have evolved while I've designed and "inlined" them into
production during these years. The medium-term goal is to keep formalizing them
to migrate to React and work with proper web development across more powerful
Texsydo integrations in MathSwe documentation and apps.

Decoupling the systems not only allows a scalable and clean design of Texsydo
Web (MVP), but also provides the "library" web frontend software under the
permissive BSD-3 license rather than Texsydo MVP's AGPL. The website generation
API of DoRep for Jekyll will allow the current Texsydo Web MVP version to ship
as a product that converts mathematical text into a static website, enriching
all apps and developments across MathSwe. 

