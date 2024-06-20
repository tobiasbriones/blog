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
