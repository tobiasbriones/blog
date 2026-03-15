<!-- Copyright (c) 2025-2026 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Pi Day 2025 (2026/03/14)

On March 14, 2025, I created the Pi Day entry, which remained empty while topic
ideas flew in my head, and attended to other job duties, which reduced my
availability at MathSwe.

Previously, efforts shifted toward **Texsydo** as a product, aiming to
transition my articles from semi-manual development to a domain-specific
pipeline. The focus moved from writing articles to establishing the system that
structures them.

My Pi article for 2025 began as an *empty* entry I seeded on 2025/03/14. Now I'm
back, committed to fulfilling it on 2026/03/14. I have also scheduled the
upcoming Pi article for 2026.

Mathematics shows that structures can emerge from *emptiness*. In
Zermelo–Fraenkel set theory, the construction begins with the empty set [1]:

$$0 = \varnothing$$

Induction builds natural numbers from $$0$$:

$$n + 1 = n \cup \{ n \}$$

Where $$n + 1$$ is the successor of $$n$$.

So, the first numbers are:

$$
\begin{aligned} 0 &= \varnothing \\ 1 &= 0 \cup \{0\} = \{\varnothing\} \\ 2 &= 1 \cup \{1\} = \{\varnothing,\{\varnothing\}\} \\ 3 &= 2 \cup \{2\} = \{\varnothing,\{\varnothing\},\{\varnothing,\{\varnothing\}\}\} \\ &\vdots \end{aligned}
$$

Thus, the natural numbers are

$$
\mathbb{N} = {0,1,2,3,\ldots}.
$$

From the natural numbers, one constructs the integers, the rationals as
equivalence classes of integer pairs, and finally the real numbers (e.g., via
Dedekind cuts) [2]. Within the real numbers lies π, that
is, $$\pi \in \mathbb{R}$$.

Mathematically, π often emerges from a converging process. Pi is the common
limit of the semi-perimeters (or areas) of regular polygons inscribed in and
circumscribed around a unit circle as the number of sides tends to infinity. [3]

$$\pi = \lim_{n \to \infty} n\sin\left(\frac{\pi}{n}\right)$$

At the beginning there is only a crude approximation while the structure appears
through iteration.

$$
\begin{aligned} a_1 &= 1\sin(\pi) = 0 \\ a_2 &= 2\sin(\pi/2) = 2 \\ a_3 &= 3\sin(\pi/3) \approx 2.598 \\ a_4 &= 4\sin(\pi/4) \approx 2.828 \\ a_6 &= 6\sin(\pi/6) = 3.000 \\ a_{12} &= 12\sin(\pi/12) \approx 3.106 \\ a_{24} &= 24\sin(\pi/24) \approx 3.133 \\ a_{48} &= 48\sin(\pi/48) \approx 3.139 \\ &\vdots \\ a_n &\to \pi \approx 3.14159 \end{aligned}
$$

In MathSwe, originally Piaxid, engineering is *axiomatic* and **centered** to
**ensure balance**. Hence, we can observe the relations I induced when designing
**Pi**axid and the centered axiom circle in the logos. Just as π is approached
by refining polygon approximations of a circle, MathSwe is devised around its
centered design.

On Pi Day 2025, the empty article marked the position of the idea in this Pi
sequence of blogs while its system (i.e., Texsydo) was still forming. Inducing
the domain and system foundations will eventually provide a cohesive universe. A
universe like the set of real numbers or mathematical software.

## References

[1] Reimann, D. A. (2022). Artistic Depiction of Numbers Defined by Sets. The
Bridges Archive. Recuperado 15 de marzo de 2026,
de [Artistic Depiction of Numbers Defined by Sets](https://archive.bridgesmathart.org/2022/bridges2022-399.pdf)

[2] Farlow, S.J. (2019). The Real and Complex Number Systems. In Advanced
Mathematics, S.J. Farlow (Ed.).
[The Real and Complex Number Systems](https://doi.org/10.1002/9781119563549.ch4)

[3] Pi as the limit of n-sided circumscribed and inscribed polygons « Math
Scholar. (
s.f.). [Pi as the limit of n-sided circumscribed and inscribed polygons](https://mathscholar.org/2019/10/pi-as-the-limit-of-n-sided-circumscribed-and-inscribed-polygons/)
