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


Addressing cyclic dependencies is an important design concern as it increases
the complexity of our software, and the job of a software engineer is to reduce
that complexity to build simple systems.

This is an interesting effect, there's a lot to talk about graph cycles.

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
<p align="center"><strong>Go does not Compile Cyclic Dependencies</strong></p>
</figcaption>

I have used Go in my job experiences, and for other projects as well.

Go is a more modern language, and its opinionated system makes it a decent
choice, unlike legacy languages like Java with many flaws.

You can write good Java, but you need to *know what you're doing*, or even
worse, you have to hire expensive practitioners increasing the technical
debt.

That's why Go is usually used at large organizations where developers come in,
and out: if compiles then it shouldn't be that crazy.

Circular dependencies require complex algorithms to resolve. Routers disallow
cycles for example. I implemented a minimum-spanning-tree algorithm for my game
"Dungeon MST" written in Go when I took the "Computer Networks" course from the
mathematics career.

The concept of "modules" is vague in CS as many other things. In this case, a
Java `package` or Go `package` is considered a kind of module, so they shouldn't
have circular dependencies.

If the circular dependencies come from more homogeneous sources like normal
source files, that's not a problem, as *they're actually the same*. So, here we
wouldn't have the "sink" concept I mentioned earlier:

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
same package `data`. So, circular requirements among files in the **same**
`package` or "module" are valid. Circular dependencies among **different**
`package`s are generally discourage, and disallowed by Go.

In the end, *everything is connected to everything* as I said before, you have
to see the *level of abstraction* to avoid falling out of relativity into
absolutism or "sinks" as said above.

You can see this idea I'm talking about like, recursion is natural and good in
FP and mathematics, but it's seen as dangerous in imperative setups.

I know recursion can become a "sink" (infinite loop) used imperatively
(absolutist), but that *won't happen* when used in pure math or (declarative)
FP as it belongs to the correct level of abstraction.

This shows one more time how fundamental principles like homogeneity will tell
you whether you're doing well. Also recall that top practitioners employ
fundamental or universal principles while bad ones only use generic marketable
tricks like OOP "principles".
