---
permalink: pi-day-2023-03-14
title: "Pi Day (2023/03/14)"
description: "Axioms are the fundamental unit of mathematical logic that inductively enable creation."
ogimage: ""
---


<nav>
  <a class="home" href="/">
    <span class="material-symbols-rounded">
      home
    </span>
    <span>
      Blog
    </span>
  </a>
  <div class="article">
    <a class="title" href="#">
      Pi Day (2023/03/14)
    </a>
    <ul>
      <li>
        <a href="#references">
          References
        </a>
      </li>
    </ul>
  </div>
</nav>

<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Pi Day (2023/03/14)

![Piaxid Monochromatic 1/4: Axiom](axiom---piaxid-monochromatic-1-4.svg)

**Piaxid Monochromatic 1/4: Axiom**

---

Axioms are the fundamental unit of mathematical logic that inductively enable
creation.

Axioms are true statements that enable meaningful definitions, and any other
kind of high-level statements like theorems.

Math is pure as it's homogeneous: math creates math.

Math is the abstraction of abstractions.

---

One immediate example of this are the axioms for the formal creation of the
natural numbers [1]:

- $$(i)$$ $$0$$ is a natural number.
- $$(ii)$$ If $$x$$ is a natural number, there is another natural number
  denoted by $$x'$$ (and called the *successor* of $$x$$).
- $$(iii)$$ $$0 \neq x'$$ for every natural number $$x$$.
- $$(iv)$$ If $$x' = y'$$, then $$x = y$$.
- $$(v)$$ If $$Q$$ is a property that may or may not hold for any given
  natural number, and if
    - $$(v.I)$$ $$0$$ has the property $$Q$$ and
    - $$(v.II)$$ Whenever a natural number $$x$$ has the property $$Q$$, then
      $$x'$$ has the property $$Q$$ then all natural numbers have the property
      $$Q$$ (mathematical induction principle).

---

Another example is the creation of the Fibonacci sequence inductively:

```haskell
fib 0 = 0
fib 1 = 1
fib n = fib (n-1) + fib (n-2)
```

<figcaption>
<p align="center"><strong>The Fibonacci Sequence (a.k.a. "the 'Hello, world!' 
of Haskell programming"</strong>[4]</p>
</figcaption>

The declarativeness of math enables creation with mathematical elegance unlike
the imperativeness of those impure where every instance is to be addressed
(i.e. defined or demonstrated) imperatively at a time.

---

Finally, the last example shows an immediate application of $$\pi$$. First,
lets define algebraic numbers [2]:

A real number $$x$$ is said to be an algebraic number is there is a natural
number $$n$$ and integers $$a_0, a_1, ..., a_n$$ with $$a_0 \neq 0$$ such that

$$a_0x^n + a_1x^{n-1} + ... + a_{n-1}x + a_n = 0$$

A real number which is not and algebraic number is said to be a
transcendental number.

---

It can be proved that $$\pi$$ is transcendental, and with this, it can be
proved that it's impossible to "square" the circle by a ruler-and-compass
construction [3].

---

Math enables us to create from fundamentally abstract concepts like axioms
and I devised the concept of mapping it $$1:1$$ to the virtual universe of
software via *mathematical software (engineering)*, and today (2023/03/14) is
the glorious day of $$\pi$$ in which I take the opportunity to get inspired and
celebrate it.

## References

[1] Mendelson, E. (2015). Introduction to Mathematical Logic. CRC Press.

[2] Morris, S. A. (1989). Topology Without Tears.

[3] Liaw, C., & Ulfarsson, H. A. (2006, April 7). Transcendence of e and π.
Williams College. Retrieved March 14, 2023, from
[Web \| Transcendence of e and π \| Williams College](https://web.williams.edu/Mathematics/sjmiller/public_html/book/papers/transcendence/TranscedenceOfPi.pdf).

[4] The Fibonacci sequence - HaskellWiki. (n.d.).
[The Fibonacci sequence \| Wiki \| Haskell](https://wiki.haskell.org/The_Fibonacci_sequence#Naive_definition).


