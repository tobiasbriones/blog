<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Initializing the MathSwe Ops GitHub Organization (2024/06/19)

I was testing some automation code I wrote in Rust last year to create software
images you can install and restore. For instance, you need to restore the system
state if you lose your OS and need to reinstall due to a fatal issue on your
machine. Also, you might use VMs in the cloud that delete everything when you
exit [^1], and you have to install and set up your software every time.

[^1]: So you have to pay 💲 to save the machine state

Remarkable non-functional requirements are **security** and
**correctness** (error, global state, side effect, third-party documentation,
and fetch repository handling) are essential in this project. While correctness
is always a must in MSW, there are challenges because of the imperative nature
of OS system programming. The security aspect is an additional challenge
extrinsic to MSW. Hence, I designed the SWAM concept to address these special
scenarios.

Regarding functional requirements, it'll have DSLs based on MSWE principles,
thus reaching the engineering grade, but for now, it's starting as another MVP.

To speed up matters right now, I thought maybe some existing tools could help,
but it didn't work well.

I reviewed tools like Packer (by HashiCorp), but I think these DevOps tools are
more appropriate for the cloud and won't integrate well with mathematical
software. I'm not losing efforts in over-engineered DevOps tools just to find
out you can't handle errors rigorously. Third-party tools may be leveraged
independently for personal use instead.

Besides, third-party tools are not open standards. Contrary, you can consider
software like Linux and Git open standards since everyone
uses/audits/tests/etc., and they're open source *without any business bias*.
Only employing open standards is required to get the software engineering grade.

The first implementations that come into mind are about automating the Linux
workstation or VMs, as described before.

I've had many ideas of what to automate but little focus before. With the new
MathSwe Ops project, these will start coming out.

One of the goals is to automate the system restoration to install configured
software with zero human interaction. Everything has to handle the vast mutable
states, side effects, and imperatives to operate the OS and keep up to date with
esoteric by-package instructions.

It's necessary to perform some system setup for daily work, like opening the
workstation software, such as IDE with internal windows. It must follow the UX
standards with visuals optimized to the center and navigation from left to right
and top to bottom.

Furthermore, deploying mathematical software will be a requirement in the
future. Unlike generic tools for ordinary DevOps, MSW must be deployed
properly to ensure the engineering grade with MathSwe support.

As said, the software will need to be engineering grade, despite the impurity
of OS mutable state, side effects, etc., described before, compared to pure MSW.

The software must be an *open standard* with its DSL *specifying*
the domain, which must be of mathematical or scientific roots. Then, the DSL is
extended or implemented via SWAM[^2], and the engineer must ensure using only
*open standards* in that implementation and pass the general SWE process to
finally achieve the **engineering grade**.

[^2]: Hence, "SWAM, Special Models" since it implements the DSL, which is the
    "pure model," into a concrete special model in the wild that passes the
    tests to comply with the DSL

For example, with tools such as Linux and Git, the software can reach the
engineering grade since these are open-source global standards[^3]. Even though
an OS has many side effects, with standards like Rust, the software can be
well-tested to pass a **general software production grade**. Hence, the
"Special" in SWAM since the implementation grade is generalist, but the whole
(DSL + SWAM) is safe to consider engineering grade.

[^3]: It's like the scientific method where science must be open to reproduce
    and study it

It's important to notice that *SWAM is not limited to MathSwe Ops*. MathSwe Ops
covers a wide form of SWAM since it operates on the whole OS rather than a
particular app. In other cases, a project has its spec
(DSL) and particular SWAM integrating its DSL[^4]. Therefore, MathSwe Ops
happens to be a wide-spectrum that operates models along the OS to empower the
engineering process rather than an implementation of a particular MSW directly.

[^4]: For example, Repsymo can take the OR definitions from MSW Sci
    (mathematical science software), which constitutes the DSL, and multiple
    SWAM implementations to realize it

Finally, the licenses *expected* to be provided to the projects will be
BSD-3-Clause as much as possible and GPLv3.0 for better compatibility in
projects that require calling or using Linux tools that are usually copyleft,
like Git. *The Affero version is not an option* for this project to avoid
scaring away cloud providers[^5]. So, permissive for the independent standards
and copyleft for lower level code.

[^5]: It's crucial to put minimal friction for everyone with permissive
    licenses to develop and deploy mathematical software, as well as providing
    support everywhere while leaving copyleft ones for final software 💡

The first [organization](https://github.com/mathswe-ops) project
is [its MVP `mathswe-ops---mvp`](https://github.com/mathswe-ops/mathswe-ops---mvp),
as per MathSwe standard (i.e., one MVP per org. max), and provides the GPL-3.0
License since it can contain any code for experimentation, so it's ready to
comply with any possible integration with Linux friendly software, which is
usually copyleft.

The new MathSwe Ops GitHub organization will establish a better focalization for
the system automation code while eventually reaching the engineering grade via
SWAM to develop and deploy mathematical software.
