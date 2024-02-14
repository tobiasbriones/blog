<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Overriding Hash Code and Equals in a Multi-Representation Record

## Multiple Email Representations

An email model consists of a **local** name followed by an **@** symbol and a
**domain** name. While this is pretty simple, they might also contain noise like
**dots** and **+** symbols for various purposes.

For example, `joedoe@place.com` is a canonical or base form of an email while
accepting the (infinitely many)
variants `joe.doe@place.com`, `joe.doe+aksfnsfs@place.com`, etc.

In programming terms, an email model can be a record with multiple
representations. That is different email values for readability or testing
purposes but corresponding to the same functionality. Therefore, emails can be
equal (or not) depending on your abstraction.
