---
permalink: removing-cyclic-dependencies--_--java-vs-go-2023-05-28
title: "Removing Cyclic Dependencies, Java vs Go (2023-05-28)"
description: "I just came across a cyclic package dependency case in Java. It's important to address the role of graphs in visualizing abstract concepts like cyclic dependencies in software design to reduce complexity and build simple systems. The benefits of using Go as a more modern and opinionated language are also emphasized."
ogimage: "https://raw.githubusercontent.com/tobiasbriones/blog/gh-pages/removing-cyclic-dependencies--_--java-vs-go-2023-05-28/removing-cyclic-dependencies--_--java-vs-go-2023-05-28.png/removing-cyclic-dependencies--_--java-vs-go-2023-05-28.png"
---


<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Removing Cyclic Dependencies, Java vs Go (2023-05-28)

![Removing Cyclic Dependencies, Java vs Go (2023-05-28)](images/removing-cyclic-dependencies--_--java-vs-go-2023-05-28.png)

<figcaption>
<p align="center">Background by 
<a href="https://pixabay.com/users/pexels-2286921">Pexels</a> via
<a href="https://pixabay.com/photos/abstract-architecture-contemporary-1867937">Pixabay</a>
</p>
</figcaption>

---

I just came across a cyclic package dependency case in Java. It's important to
address the role of graphs in visualizing abstract concepts like cyclic
dependencies in software design to reduce complexity and build simple systems.
The benefits of using Go as a more modern and opinionated language are also
emphasized.

Addressing cyclic dependencies is an important design concern as it increases
the complexity of our software, and the job of a software engineer is to reduce
that complexity to build simple systems.

This is an interesting effect, and there's a lot to talk about in graph cycles.

Today, I was refactoring a Java project a bit and found that a `package` was a
bit **coupled**.

The `data` package requires the `ui` package when the dependency would normally
consist of a "client" package (i.e., `ui`) that requires a more-universal or
abstract one (i.e., `data`). This way the dependencies keep simple or linear.

![Removing Cyclic Dependency Commit](images/removing-cyclic-dependency-commit.png)

<figcaption>
<p align="center"><strong>Removing Cyclic Dependency Commit</strong></p>
</figcaption>

When module `D` depends on `U`, and `U` depends on `D`, you're saying that
`D` depends on `D` (itself), so this is a kind of *sink*[^1] that strikes me as
interesting.

[^1]: As a mathematician, this relates to me as dynamical systems or automata
    theory

As a philosopher, I see this as a design flaw since you're building a sink in
that dependency graph, but everything is relative (i.e., everything is connected
to everything). If one part of the system is cyclic like that, I can see it as
something absolute or disconnected from the rest, hence flawed.

Let's see what Go says if I build a dummy PoC project that resembles this flaw:

```
package test
        imports test/ui
        imports test/data
        imports test/ui: import cycle not allowed
```

<figcaption>
<p align="center">
<strong>
Go does not Compile Cyclic Package Dependencies
</strong>
</p>
</figcaption>

I've used Go in my job experiences and for other projects as well.

Go is a more modern language, and its opinionated system makes it a decent
choice, unlike legacy languages like Java, with many flaws out of the box.

You can write good Java, but you need to *know what you're doing*, or even
worse, you have to hire expensive practitioners increasing the technical debt.

That's why Go is usually used at large organizations where developers come in,
and out: if compiles then it shouldn't be that crazy.

Circular dependencies require complex algorithms to resolve. Routers disallow
cycles, for example. I implemented a minimum-spanning-tree algorithm for my game
"Dungeon MST" written in Go when I took the "Computer Networks" course for my
math major.

Many concepts in CS, like "modules," are vague. In this case, Java/Go `package`s
are considered a kind of module, so they shouldn't have circular dependencies.

If the circular dependencies come from more homogeneous sources like normal
source files, that shouldn't be a problem, as *they're actually the same*. So,
here we wouldn't have the "sink" concept I mentioned earlier if you operate on
the *same* package:

```
.
├── data
│   ├── data.go
│   └── file.go
├── go.mod
├── main.go
└── ui
    └── ui.go
```

<figcaption>
<p align="center"><strong>Go Project Structure</strong></p>
</figcaption>

In Go, source files under the `package` `data` (or any `package`) belong to the
same package `data`. So, circular requirements among **files** in the **same**
`package` or "module" should be valid. Circular dependencies among **different**
`package`s are generally discouraged and disallowed by Go.

As I said before, *everything is connected to everything* in the end. You have
to see the *level of abstraction* to avoid falling out of relativism into
absolutism or "sinks," as said above.

Identifying the *homogeneous* and *heterogeneous* elements within a system is
necessary to apply these universally abstract principles. I mean, everything is
relative, so this depends on the level of abstraction.

For example, if your dependencies are *packages*, and you have different
packages, then it's heterogeneous, so don't be recursive as your **context is
imperative**. If both abstractions are the same, then you can apply math
techniques like FP or recursion as your **context is declarative**.

You can see this idea I'm talking about, like recursion, which is natural and
good in FP and mathematics, but seen as dangerous in imperative setups.

I know recursion can become a "sink" (infinite loop) used imperatively
(absolutist), but that *won't happen* when used in pure math or (declarative)
FP as it belongs to the correct level of abstraction.

This shows one more time how fundamental principles like homogeneity will tell
you whether you're doing well. Also recall that top practitioners employ
fundamental or universal principles while bad ones only use generic marketable
tricks like OOP "principles."

Another way you can create "sinks," or "infinite loops" by design, is through
Java inheritance. Inheritance is, by design, *inherently coupled* (pun
intended). So, if a supertype **depends** on a subtype, and vice-versa, you got
it 💥.

I encounter various instances where one can identify this problem.

For example, when calling a method implemented[^2] above. In imperative
settings, I follow the rule to call methods arranged below the caller. That is,
only call methods below the current one, or you can build a mess otherwise. For
example, you can fall into an infinite recursive loop of **imperative nature**.

[^2]: Again, the heterogeneity of imperative is here, so I use the word
    *"implement"* instead of *"define"* as definitions are **pure** while
    methods are "actions" or "verbs"

Another example is the usage of `this` in the constructor anti-pattern: you pass
a reference of an object (i.e., `this`) that is not in a consistent state yet,
because it hasn't been *constructed*.

Another example is the old-fashioned MVC "architectures" for GUI apps. The
"model" updates the "view," then the "view" updates the "model," and so on. This
leads to **complicated** solutions, but recall that your goal as a software
engineer is to build **simple** ones.

Nowadays, languages try to pose problems in a simpler approach, and Go has
showed how it's possible.

Contrary to what common sense would say, being simple is quite hard.

A good engineer will not usually apply "clever" designs but **simple** ones that
adhere to computer **science** and **domain** facts.

Regarding what I said above when the context can either be declarative or
imperative what you have to do is to **simplify those imperative designs into
homogeneous ones**.

So, you can see that FP is like math: it must satisfy the principle of closure.
You have to go fully functional to get the benefits of a closed system like math
or FP.

In a practical "enterprise" setup, you better be mediocre to settle down to use
mundane paradigm approaches instead of simplifying a lot. That is, you'll have
to face stupidly complicated systems, including cyclic dependencies of several
kinds. Someone like me doesn't want to work in such an environment 😁.

Thus, systems have to be simplified[^3], but some won't want to be simplified.

[^3]: Simplifying systems or code is like the mathematical simplification of
    **expressions** like polynomials you learned in basic courses

Neither Java nor (even worse) Go are functional. But, you have **to become a
better engineer from the math principles since you'll find math everywhere**,
even in mediocre heterogeneous and fragmented systems like imperative or OO.

In this case, you can see how graphs are ubiquitous, making them an essential
component of your abstract memory to visualize such phenomena.

This also happens in mathematics. If you're proving a statement $$p$$, the proof
can be really tough and reviewed by many experts. In the middle of the "proof,"
you unconsciously may *assume* $$p$$, so your "proof" is invalid because the
situation was too hard to see (like big software can be), and you committed a
**circular reasoning fallacy**.

Finally, I was reading
[Cyclic Dependency - an overview | ScienceDirect Topics](https://www.sciencedirect.com/topics/computer-science/cyclic-dependency)
which provides great insights as well related to these designs.

To avoid complicated designs or flaws like cyclic module dependencies, one has
to keep simplicity in the systems and visualize the abstract concepts to
understand what you're doing. Regarding Go and Java, we can see how the
opinionated design of Go makes it easier to write simpler systems with bounded
complexity, while Java is a legacy language that won't help with this unless
you're well-versed in the language and enforce it manually, which turns in more
technical debt.


