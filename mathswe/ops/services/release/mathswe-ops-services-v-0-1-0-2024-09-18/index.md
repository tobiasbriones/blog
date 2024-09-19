<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# MathSwe Ops Services v0.1.0 (2024/09/18)

## Web API to Generate SVG Badges for Release and MathSwe Project Name with Icon

Some MathSwe projects, like microservices and MVPs, are subdirectories in their
repository. So, enriching their documentation with release badges requires
manual maintenance as popular badge providers, like Shields.Io, don't support
these standards. These subprojects are independent and need their version when
released, and eliminating (unnecessary) release overhead is a top engineering
priority to deliver value faster.

Further, besides the version badge problem, project docs require a badge with
their icon and name, which is currently manually handled. Finally, data like
base64 icons don't fit a Markdown URL, so the badge provider should support
these.

This initial release provides results to the
[Initializing MathSwe Ops Services (2024/09/15)](/initializing-mathswe-ops-services-2024-09-15)
insight.

- [Initializing the MathSwe Ops Services Project \| MathSwe Ops Services (2024/09/15)](https://blog.mathsoftware.engineer/initializing-the-mathswe-ops-services-project---mathswe-ops-services-2024-09-15).
- [In-House MathSwe-TS-and-Client Libraries \| MathSwe Ops Services (2024/09/15)](https://blog.mathsoftware.engineer/in--house-mathswe--ts--and--client-libraries---mathswe-ops-services-2024-09-15).
- [New Badge Version and Project Endpoints \| MathSwe Ops Services (2024/09/18)](https://blog.mathsoftware.engineer/new-badge-version-and-project-endpoints---mathswe-ops-services-2024-09-18).

GitHub release at
[Services v0.1.0: Publishes a Web API to Generate SVG Badges for Release and MathSwe Project Name with Icon](https://github.com/mathswe-ops/services/releases/tag/v0.1.0).

The release endpoint accepts GitHub repositories and their subprojects, while
the other accepts MathSwe projects and their MVP versions. The new endpoints in
the Services API remove significant manual maintenance overhead when releasing
and creating MathSwe project documentation.
