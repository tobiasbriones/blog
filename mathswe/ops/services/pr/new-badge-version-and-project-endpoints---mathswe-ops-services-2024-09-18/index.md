<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New Badge Version and Project Endpoints | MathSwe Ops Services (2024/09/18)

---

**Allow hyphens in Hostname**

Sep 16: PR [#3](https://github.com/mathswe-ops/services/pull/3) merged into
`services/dev <- services/mathswe-client`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It fixes the rejection of valid hostnames with hyphens.

---

**Add router with MS-Client CORS**

Sep 16: PR [#4](https://github.com/mathswe-ops/services/pull/4) merged into
`services/dev <- services/ops`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the Itty-Router dependency that routes CF Worker applications and sets
up the CORS origin to only accept MathSwe-Client curated origins.

---

**Implement inference of project version from GitHub repository**

Sep 17: PR [#5](https://github.com/mathswe-ops/services/pull/5) merged into
`services/dev <- services/git-platform`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It defines a Git platform and build-system abstractions such as GitHub, NPM, and
Cargo to provide an implementation through the GitHub API that reads a project
(including subdirectories) given its repository URL, infers the build system it
uses, finally, it reads the project's version.

It only reads public repositories at branch `main` (production) for simplicity.

`InferVersion API`

```ts
export async function inferVersion(
    gitPlatform: GitPlatform,
    user: string,
    repo: string,
    path: Option<string>,
): Promise<Either<string, string>>
```

`Infering Project Versions`

```ts
const rustProject = await inferVersion(
    gitHub,
    "rust-unofficial",
    "awesome-rust",
    none,
);
const jsProject = await inferVersion(
    gitHub,
    "jquery",
    "jquery",
    none,
);
const nestedMvp = await inferVersion(
    gitHub,
    "mathswe-ops",
    "mathswe-ops---mvp",
    some("system"),
);

console.log(rustProject);
console.log(jsProject);
console.log(nestedMvp);
```

`Output: Infering Project Versions`

```
{ _tag: 'Right', right: '0.1.0' }
{ _tag: 'Right', right: '4.0.0-beta.2' }
{ _tag: 'Right', right: '0.1.0' }
```

Besides providing some GitHub and build system abstractions, it's a useful API
that an endpoint can use to get the version of a given GitHub project, even if
it is a nested subdirectory, like microservice (independently versioned) or mono
repository with sub-projects.

---

**Add test suite for readBuildSystem**

Sep 17: PR [#6](https://github.com/mathswe-ops/services/pull/6) merged into
`services/dev <- services/git-platform`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds some tests for the method that reads the GitHub project's build system
by mocking the file list API response.

---

**Implement endpoint badge/version**

Sep 18: PR [#7](https://github.com/mathswe-ops/services/pull/7) merged into
`services/dev <- badge` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It provides the new endpoint that takes a repository GitHub URL, or one of its
subprojects (e.g., microservice), and responds with the badge showing its
current version.

---

**Implement endpoint badge/project and enhance badge styles and code**

Sep 19: PR [#8](https://github.com/mathswe-ops/services/pull/8) merged into
`services/dev <- badge` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The badge/project endpoint provides the main badge for a given MathSwe project
(including MVPs).

---
