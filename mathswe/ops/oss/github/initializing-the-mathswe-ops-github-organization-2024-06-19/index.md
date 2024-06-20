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
