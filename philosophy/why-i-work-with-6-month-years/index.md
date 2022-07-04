<!-- Copyright (c) 2022 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Why I Work with 6-Month Years

The simplest designs are the best, they are more suitable to be proved and
if it can be proved, then we have math. Something simple like a recursive
logic that maps to a universe set a single value like 1 in the base step.

## Introduction

I'll give you insights as a mini framework, so you understand what my logic 
is about.

### Easy Does Not Necessarily Equal Simple

The easiest designs are monoliths with primitive obsession, but the simplest 
designs are devised in a case by case basis. So easy designs are bad but easy 
for the practitioner to come up with whilst simple designs are the best -as
they're easier to formally state- but hard for the practitioner to obtain.

We can say that:

- easy designs are underengineered, so they're terrible: easy to create but hard
  to use/understand, while
- simple designs are just what they must be, like a definition or 
  equivalence using an if-and-only-if biconditional, so they're correct: 
  hard to create but easy to use/understand.

Even in chaotic systems or stochastic processes we're interested in 
*modeling* simple equations even when they're no deterministic, but simple
linear models can be used to solve the problems properly rather than leveraging
complicated non-linear algebra.

Notice that in engineering we *design* unlike math where we *model*.

#### Illustration of What Simple Looks Like

Next, I'll give definitions so you can infer similar conclusions:

> **simple:** composed of a single element; not compound.
>
> **easy:** achieved without great effort; presenting few difficulties.
>
> Source: [Oxford Languages and Google](https://languages.oup.com/google-dictionary-en)


We even have more definitions of "simple" [^1], but the idea is the same.

[^1]: There's a definition of simple for group theory
    [Simple Group \| Wikipedia ](https://en.wikipedia.org/wiki/Simple_group)

I love to depict the idea of simplicity with math. Mathematical induction
(or recursion) is a key for understanding mathematics. The power and beauty
of math comes into the following ways:

- Definition
- Recursion or Induction
- Homogeneity
- Declarativeness
- ...

Math is the language of languages, that is, the universal language.

The power of defining models gives us the power of creating languages from math,
or domain specific languages (DSLs).

To achieve ultimate scalability homogeneity is key, in my words, homogeneity
is key for pureness.

If we use math to define languages then is scalable, pure, homogeneous:
everything is a language. Those are strong guides that tell you're doing the
right thing right. It's the guide a real philosopher must have to leverage
the most abstract of math.

As I've said in previous posts, trees are recursive structures, so they're
pure. You can chop a tree, and you'll get a tree -it's infinitely scalable-,
but if you have impureness like vertical hierarchies you will have a
pyramidal hierarchy and if you chop a pyramid in halves (use your imagination)
the "peons" on the bottom will be doomed and "piglets" in the top will
barely still have a smaller pyramids, so pyramids are not reusable.

Scalability is such a topic here, I will elaborate much more in a next article
with my recent discoveries. I know I have pretty unique thoughts and
discoveries because I'm a mathematician as is and an engineer as is, at same,
while ordinary professionals are just how universities indoctrinate them:
just a plain mathematician or a plain engineer, which is a vertical
integration of knowledge (really bad).

Check the popular function to compute the factorial, that is simple:

**Factorial Example Function in Haskell**

```haskell
fac :: (Integral a) => a -> a
fac n = product [1..n]
```

**Factorial Example Function in JS (more imperative)**

```js
function f(n) {
  if (n === 1 || n === 0) {
    return 1;
  }
  return n * f(n - 1);
}
```

Notice how we *define* the basic step with the number 1. That tells us that
from a simple number like just 1 we can populate any logic or tree via
declarative recursion or mathematical induction. It's homogeneous, so it's pure.

##### Why Use Recursion in Programming?

This is a perfect question that just came into my mind right now. When I was
given free programming tuition at university I left an assigment to
students to find out why they should use recursion even when computers are
stupid enough to not understand recursion and pollute the stack of function
calls.

I researched such benefits before that day, but it wasn't too clear for me
what recursion was up to.

Now I can perfectly understand many advanced concepts by merging math and
engineering. It's not just about writing easier to understand code
(although recursion it's super hard to understand for many programmers), but
for declarativeness, homogeneity, scalability in the logical aspect of
course, etc.

I fell in love with recursion when I started prototyping the
[EP: Machine Replacement Model](https://github.com/tobiasbriones/ep-machine-replacement-model)
and then the [Repsymo Solver](https://repsymo.com).

That was when I was studying the deterministic dynamic programming models from
operations research or mathematical programming courses.

I created those two projects to be able to properly teach those topics in my
university presentations.

Now I can represent (hence Repsymo) those models in a useful way for the
practitioner!

We have the power of representing the beauty of recursion and math with Repsymo.
