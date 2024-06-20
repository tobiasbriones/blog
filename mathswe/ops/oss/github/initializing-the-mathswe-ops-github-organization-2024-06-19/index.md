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
