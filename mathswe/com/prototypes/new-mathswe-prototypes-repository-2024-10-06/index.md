<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New MathSwe Prototypes Repository (2024/10/06)

Prototypes are ultra-fast projects for development while you understand the
requirements and shape an actual project. They provide the speed tradeoff by
requiring no standard practices, like good commit messages.

I've been in the process of migrating private prototypes to MVPs so they can
finally reach production while eventually aiming for the engineering grade.

I'm migrating Texsydo (Web and FX) prototypes, as said in
[Testing the Texsydo FX Prototype (2024/09/03)](/testing-the-texsydo-fx-prototype-2024-09-03),
while
[Providing Templates for Repetitive General-Purpose Projects (2024/09/20)](/providing-templates-for-repetitive-general--purpose-projects-2024-09-20).

Prototypes, templates, and MVPs have in common *certain level of centralization*
because **they're not the *very* final objective** but a crucial mean to get
there (mostly MVPs).

The problem is the *organization of those prototypes*. When I felt pressured, I
used to create diverging throw-away branches in their repository. So, the
prototype branch stays close to the underlying project but will never be part of
it (i.e., you won't merge it with the `main` branch).

It becomes more overwhelming and unmaintainable over time to keep decentralized
orphan prototypes. There, I noticed the centralized nature of prototypes.

According to the MVP standards I drafted this year, a GitHub organization can
have only one MVP. Since prototypes have the lowest possible engineering
standards as a tradeoff, they are much more centralized than MVPs. Therefore,
you should only have a `prototypes` private repository for the whole
organization (Texsydo, Repsymo, MSW, etc.).

MVPs provide a production-grade quality for their "Minimum" features, at least.
So, MVPs are a bit centralized in their domain (e.g., Texsydo organization).

Now, prototypes have nothing to do with MVPs, so prototypes should be globally
centralized and private. From MathSwe's organizational graph, you'll find a
proper place for such a repository on the business side[^1].

[^1]: MathSwe Com, at the bottom of the graph that covers everything else above

Prototypes are **globally centralized** within **a unique private repository**
and require **cutting development standards** to shape a potential project fast.
They are the projects with **the lowest engineering standards**, and you'll
probably reuse them to **make them either a production-grade project or an
MVP**.

Even though prototype *development* standards[^2] are minimal, they must not
affect the code. Their **code must be clean enough to refactorize** to ensure
you can migrate and **use it on production**.

In other words, **there's no reason to write garbage code in MathSwe**, so you
must be responsible for tradeoffs instead. There will always be standards, even
if they're minimal.

[^2]: Formal documentation, PRs, commit messages, etc

Therefore, prototype development practices must be minimal while their code must
hold a pre-release (or experimental) quality.

---

Prototypes are experimental projects with pre-release quality that can become
real projects, like MVPs. They can evolve from "prototype to MVP to
engineering-grade" or "prototype to production-grade." Prototypes should also
touch production as experimental software to evolve them over time.

It's crucial to organize prototypes to keep them maintainable. By noticing their
centralized nature, you can understand they must belong to a global private
mono-repository. Prototypes are not OSS since they don't have scope outside the
organization, so they cannot be products.

Creating a `prototypes` repository for each GitHub organization is
overengineering since you don't even know the potential projects a prototype can
produce. GitHub organizations can only have one MVP repository, but prototypes
are significantly more centralized than MVPs. Therefore, the whole
organization (MathSwe Com) must maintain the unique `prototypes`
mono-repository.

In MathSwe, there will always be quality standards. Prototypes are not
low-quality projects but projects that optimize for other tradeoffs. Hence,
**projects must be in their intended place**, like prototypes, MVPs,
production-grade, or engineering-grade. A project's placement can be
centralized, like prototypes; domain-centralized, like MVPs; or domain-specific,
which is the final objective.

Prototypes require minimal development standards to provide pre-release grade
software that can evolve in production and originate potential projects.
Engineers will understand their domain and address intricate pragmatic
development challenges that projects of higher standards do not allow.
