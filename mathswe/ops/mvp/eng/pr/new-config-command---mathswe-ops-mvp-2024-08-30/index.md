<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New Config Command | MathSwe Ops Mvp (2024/08/30)

---

**Refactor CLI app module before implementing more commands**

Aug 25: PR [#10](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/10) merged into `ops/dev <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The `Config` operation needs an implementation and I found the main entry code
had some over-engineered parts, so it's necessary to:

- Simplify it.
- Make it more modular.
- Add more abstractions.

These changes integrate a new submodule `main` for the CLI app.

`New Module main`

```
├── main
│   ├── batch.rs
│   ├── cli.rs
│   ├── exec.rs
│   ├── image_exec.rs
│   └── system.rs
├── main.rs
```

This organization denotes the CLI submodules, considering it needs to apply
batch logic with a report. By default, **all commands are by batch**, so the
system will process it by batch whether the command only accepts one image or
many as it gives the same result.

It introduces the `OperationContext` in the module `exec` to load all the
environment information, like the OS, which will be available for
decision-making. It clarifies where to look for runtime details.

It introduces the `OperationExecution` in the module `exec` to implement the
CLI-facing commands and resolve the low-level operations to load and execute.
For example, low-level operations are independent, but a command can be a
composition like `install + config`, so this module converts the user command
into program low-level operations to execute (e.g. `ImageOps`).

The restructure, simplification, and redesign of the CLI-facing modules provide
scalability to the program to add more commands and operations that the user can
compose.

---

**Implement operation Config**

Aug 26: PR [#11](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/11) merged into `ops/dev <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

Some images require configuration after you install them, so they function in a
restored state.

This PR:

- Implements the new operation `Config`.
- Implements `Config` for `MinicondaImageConfig` to create conda environments
with packages pre-installed.
- Composes the `--config` flag on the `Install` operation.

Usages: `system config miniconda`, `system install --config miniconda`, etc.

The configuration goes to the `image/` directory. For example,
`image/miniconda.config.json` for `MinicondaConfig`.

It demonstrates the system's capabilities to extend more functionalities since
I've invested much in its design to fit Rust's robustness.

The new configuration command makes the System CLI more powerful by allowing
users to **restore the image state after installation**, or whenever they want.


---

**Update System app docs with command Config**

Aug 27: PR [#12](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/12) merged into `ops/dev <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the recently added command `Config` and flag `--config` to the project
`README.md` documentation.


---

**Implement GitImage with Config**

Aug 28: PR [#13](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/13) merged into `ops/dev <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the `GitImage` to the repository and the tooling necessary to implement
its `Config` operation.

The config file is an empty structure at `image/git.config.json` you replace
with your values.

- It includes configuration fields for Git `Core`, `User`, and `Commit`.
- It allows you to set and enforce sign commits with GPG.
- It allows you to set the global Git ignore file that applies to all Git
repositories on your machine.

You can run `system install git`, `system install --config git`, `system config
git`, etc.

The Git implementation is quite useful for setting up a workstation machine from
cold-start with your personal Git environment.


---

**Get project ready for initial release**

Aug 30: PR [#14](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/14) merged into `ops/dev <- system` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It sets the project information to create the Debian installer and be able to
release it now for faster iterations.


---

**Update landing page info and set to release**

Aug 30: PR [#15](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/15) merged into `ops/dev <- ops.math.software` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the latest documentation of the System app and sets the project for its
initial release.


---

**Update project docs for release**

Aug 30: PR [#16](https://github.com/mathswe-ops/mathswe-ops---mvp/pull/16) merged into `ops/dev <- ops/docs` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the landing page section in the `README.md` and updates the badge SVGs.
A new project will automate updating the badges, so this work should not be done
manually for future releases.

---

The new `config` operation required a significant refactorization of the
CLI-facing modules so that the app could support the `config` command and any
other commands it may need.

The `config` command is a key feature of MathSwe System Ops since it sets up the
software users install with their needs, configurations, or dependencies out of
the box. It ***restores* images**.

I can release the System CLI app via GitHub releases and a `deb` package with
checksum. While there are more pending tasks, the current changes make the app
productive and give an idea of what it is.

Finally, the **MSW Ops web home** is ready with up-to-date essentials of MSW Ops
and MathSwe Ops concepts, as well as the System app documentation for end users.

The current developments can provide production value. The latest changes allow
the initial release of the MVP projects to cool down and not leave behind other
tasks pending from other projects that cover other MSW and MathSwe requirements
to reach production as a whole.
