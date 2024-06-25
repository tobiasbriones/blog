<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Ensuring Security Without Breaking the API in System Ops (2024/06/20)

The new MathSwe Ops project will focus on security specifications while
addressing challenges such as smooth DSL integrations via SWAM and avoiding
unnecessary side effects. Acceptable tools must be open standards with domain
expertise when relying on software engineered by other organizations.

In [Initializing the MathSwe Ops GitHub Organization (2024/06/19)](/initializing-the-mathswe-ops-github-organization-2024-06-19),
I updated about the `mathswe-ops` organization to focus on the system automation
code and how to eventually reach the engineering grade with SWAM for developing
and deploying mathematical software.

One can use available tools or build news if existing ones don't fit the
criteria. Namely, *rigorous error handling* given the *stateful OS nature* and
long-term impact on automated software that will stay operating in the system;
thus, **tools must be thoroughly secure**.

To speed up matters right now, I thought maybe some existing tools could help,
but it didn't work well.

I think these DevOps tools are more appropriate for the cloud, which means
they're final solutions and won't integrate well with mathematical software. I'm
not losing efforts in over-engineered DevOps tools just to find out you can't
handle errors rigorously.

Third-party generic tools won't integrate well. You should check if they were
originally designed to fit your specific work. So, they **fit your API or DSL to
avoid breaking and over-engineering it** while recalling that integrations with
tools or implementation details go to the system boundaries. **System boundaries
are a focus of side effects**. Hence, it's crucial to make tooling integrations
fit your design, whether they're a third or first party.

For example, you'd have to pass a list of system commands to generic tools in a
generic config file (instead of application code) to install some software in
cold OS installation, which, further, you can't reuse for other needs. You get a
shell script quality but the complexity of setting up a DevOps suite while
pushing more boundaries on your system.

Furthermore, generic tools will —hopefully— provide generic error handling (what
guarantees that?). Spawning new processes when using the terminal only increases
the side effects, creating more boundaries, reducing the API domain (turns more
imperative/esoteric), and speeding up complexity.

Then, you'll have to read esoteric command outputs as primitive values (strings
or int) according to their CLI version instead of interacting with an actual API
that runs in the same process with no alien message passing via CLI.

Computing is chock-full of side effects, but *there are many we should avoid*
with proper systems engineering.

Besides, third-party tools are not open standards. Contrary, you can consider
software like Linux and Git **open standards** since everyone
uses/audits/tests/etc., and they're open source *without any business bias*.

Only employing open standards is required to get the software engineering grade
since it resembles how science works.

Unlike tools written in imperative languages by generic programmers, a tool
written in a functional language requires **domain expertise** to design its
DSL for successful compilation, which is required for the engineering grade.

In systems engineering, you can use a functional language as the frontend
(DSL) and compile it down, like
[copilot: A stream DSL for writing embedded C programs](https://hackage.haskell.org/package/copilot)
that NASA uses, among many other systems using Haskell.

> Copilot is a stream-based runtime verification framework implemented as an
> embedded domain-specific language (EDSL) in Haskell. Programs can be
> interpreted for testing, or translated into C99 code to be incorporated in a
> project, or as a standalone application. The C99 backend output is constant in
> memory and time, making it suitable for systems with hard realtime
> requirements.
>
> Source: copilot \| Hackage [1] (under BSD-3-Clause)

You can follow the same concept for encoding the security and domain
specifications in Haskell and translating it to efficient machine language, so
while engineers work with high-level languages, compilers and tools work with
machine languages.

Its **relativism** makes it efficient since the DSL is declarative for the
engineer while C or low-level code is declarative for the machine. Conversely,
the DSL is imperative for the machine, which cannot execute concepts, while
machine code is imperative for the engineer.

Imperative code is at the wrong level of abstraction, hence the side effects.

Whether it is declarative or imperative *depends on the observer*, and that's
what engineers must address by letting tools (i.e., SWAM) work with the machine
while engineers work with the DSL.

Engineers should use tools written in purely functional languages like Haskell
and PureScript to keep engineering standards, as they will give the security
requirements by enforcing a DSL with efficient machine implementations.

While I will start updating the new MathSwe Ops project by experimenting with
its MVP repository, I'll keep reviewing open standard tools that can help MSW
development and deployment. Using open standards is mandatory for
engineering-grade software.

Tempting more system side effects by calling external terminal or CLI
applications should be discouraged for engineering-grade settings as they
require an imperative focus on esoteric outputs and third-party documentation.
Spawning more processes (i.e., more side effects) hinders proper error handling
and builds more boundaries that also break the DSL, create runtime overhead, and
programming logic over-engineering (e.g., you have to *try to* parse strings to
actual errors, etc.).

DSLs and SWAM make systems relative by making them declarative for engineers and
machines.

MathSwe Ops should stress security specifications as a crucial feature, where
functional languages will help as they have done before with many organizations
with rigorous engineering standards.

## References

[1] [copilot. Hackage.](https://hackage.haskell.org/package/copilot)
