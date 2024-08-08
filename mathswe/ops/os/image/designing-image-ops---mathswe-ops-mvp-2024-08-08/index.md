<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Designing Image Ops | MathSwe Ops MVP (2024/08/08)

---

**Add test_file.txt with lorem ipsum**

Jul 17: PR [#1](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/1) merged
into `main <- ops/resources`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It serves as a sample file to test basic downloads.

---

**Add project system with modules tmp, download**

Jul 18: PR [#2](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/2) merged
into `main <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It creates a new Rust app `system` that will provide the tools for automating
Linux systems, such as installing software safely.

It implements modules for managing system temporal files and performing blocking
downloads with a basic integrity check.

- **Module `tmp`:** It will provide APIs to manipulate system temporary files. A
  program should request to `TmpWorkingDir::new()` its dedicated temporal
  directory to work with, for example, `mathswe-ops_26P8JP/`. It currently
  provides a blocking download method since asynchronous requirements are still
  obscure.
- **Module `download`:** It will implement secure downloads (not necessarily to
  a temporal directory or file).
- **Module `download::hashing`:** It will implement file checksum code to
  compare file hashes for integrity.

The new project `system` started providing modules for programs to get a
dedicated temporal directory to operate within and download tooling capable of
extension for more integrity file checks (besides `SHA-256`).

---

**Add CLI and command APIs**

Jul 20: PR [#3](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/3) merged
into `main <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It initializes a CLI structure with the `clap` library to parse command line
arguments and provides a module `cmd` with an API to call system commands.

---

**Add modules image::*, download::*, package, os, system, exec, with
program-wide structure and refactorization**

Aug 1: PR [#4](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/4) merged
into `main <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It defines and implements modules required for the program's structure,
robustness, and ability to scale and maintain software images.

- `mod package:` It defines what a high-level package needs. It defines:
- Serializable version types such as `SemVer` and `SemVerRev`.
- A `Software` record, an essential software product model.
- A `Package` record to model a high-level software package.
- Performs various module redesigns to fit scalability and maintenance needs
  with type safety.
- Implements GPG signature verification with `download::Integrity::Gpg(GpgKey)`
  for downloaded files requiring this method (like Zoom).
- Implements serialization of image information that changes a lot, or is
  volatile, such as versions (e.g., `SemVer`). For example, `ZoomInfo` with
  version, and GPG key with its fingerprint.
- `mod image::desktop:` It implements the images of type `DesktopImageId` for
  software that only works for desktop GUIs, such as Zoom. It tests the current
  operations and system design with the image `Zoom.`
- `mod image::server:` It implements the images of type `ServerImageId` for
  software that works in general for server or workstation/desktop, such as Rust
  It tests the current operations and system design with the image `Rust.`
- `mod os:` It provides OS-level command implementations to abstract away from
  `ImageOps.`
- `mod system:` It contains the main program module.
- `mod exec:` It takes responsibility for the last layer of the (user-facing)
  program execution.

These changes provide modules to **structure the program**, **safe downloads**
(to temporal directories) with `Integrity::Gpg` verification (besides the
`Sha256` `Hash` provided before), and **proof of concept** via the
`DesktopImageId::Zoom` and `ServerImageId::Rust` `ImageOps` implementations.

Serialization of volatile image information that changes frequently, thus
requiring high maintenance (commits, reviews, etc.) goes to a `JSON` file in the
program directory. Changing data such as image version and checksum goes to its
info file while domain data belongs inside the program memory. Therefore,
**hacking the program is minimized or disallowed** since the `Image` `JSON` file
only contains minimal data. For example, it only contains its version, not the
whole `Url` to fetch from, so the `HTTPS` protocol and the correct `Url` domain
name (which don't have to change) are always safe since that data is part of the
**program domain**.

Recall that `Image` models and their serializable (`JSON`) info go on a
case-by-case basis allowing **`Image` customization**.

Modules `system` and `exec` implement terminal operations, such as user-friendly
program messages while consuming abstractions like `ImageOps` with traits such
as `Image`, `Install`, and `Uninstall`.

Modules `image::desktop` and `image::server` demonstrated the system design with
(currently side-effect) automated and manual testing. Adding more images is
maintainable and relatively stable from now on.

---

**Clean boilerplate with ImageLoadContext and Implement GoImage**

Aug 4: PR [#5](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/5) merged
into `main <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the `ImageLoadContext` abstraction that provides the otherwise
boilerplate at each image module, so images only need a `new` constructor
instead of three.

Integration of `ImageLoadContext` removes two extra constructors (`from` and
`load_with`) boilerplate from each image module since image loading should
follow the same pattern.

It also implements the `GoImage` with particularities such as decompressing
(untar) files, using the `usr/local` system directory, and adding and removing
environment variables.

The `ImageLoadContext` integration makes the client code smoother and safer
while the `GoImage` implementation keeps adding more features to the repository.

---

**Implement images Java, Gradle and add SemVerVendor**

Aug 5: PR [#6](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/6) merged
into `main <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It implements the `JavaImage` and `GradleImage` as well as a new version
definition `SemVerVendor` to support images with versions such
as `java x.y.z-amzn` containing the `SemVer` followed by the vendor to fetch
from.

---
