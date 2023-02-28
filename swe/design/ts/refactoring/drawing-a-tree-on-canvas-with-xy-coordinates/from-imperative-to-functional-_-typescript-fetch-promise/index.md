<!-- Copyright (c) 2022-present Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# From Imperative to Functional: TypeScript Fetch Promise

![From Imperative to Functional: TypeScript Fetch Promise](from-imperative-to-functional-_-typescript-fetch-promise.png)

This
[gist](https://gist.github.com/tobiasbriones/544350fc301ffe32c1dd64d2f6ec6d81)
shows how more functional code is superior to imperative code to
perform a simple fetch request to get a JSON value that models a tree data
structure to be represented later by `HTMLCanvasElement`.

Both versions are correct and do the same, but the "functional" version
still has some imperative style because JS/TS is not a functional language but
the difference **is clear** again.

The difference between both snippets is the following:

```ts
async function fetchTree(path: string): Promise<TreeNode> {
  const onError = reason => showError({ reason, msg: 'Failed to fetch tree' });
  let tree = newTreeNode();

  try {
    const res = await fetch(path);

    if (res.ok) {
      tree = await res.json();
    }
    else {
      onError(new Error(res.statusText));
    }
  }
  catch (reason) {
    onError(reason);
  }
  return tree;
}
```

<figcaption>
<p align="center"><strong>Imperative</strong></p>
</figcaption>


```ts
function fetchTree(path: string): Promise<TreeNode> {
  return fetch(path)
    .then(res => res.ok ? res : Promise.reject(res.statusText))
    .then(res => res.json())
    .catch(reason => {
      showError({ reason, msg: 'Failed to fetch tree' });
      console.error(reason);
      return newTreeNode();
    });
}
```

<figcaption>
<p align="center"><strong>More Functional</strong></p>
</figcaption>

The refactored code (a.k.a. "more functional") **is not functional**, but it 
gets close. This is to avoid introducing functional abstractions like pipes, 
monads, etc., on top of JS/TS which is not a functional language but a mixed 
one as the underlying project is pretty short.

By noticing the:

👎🏻 return statement

👎🏻 ternary operator

👎🏻 error handling block

And lack of:

👉🏻 pipe operator

👉🏻 pattern matching

👉🏻 expressions[^1]

[^1]: Notice the stupid semicolons appear when the line of code is imperative,
    sounds familiar isn't it? yes, Rust 🦀

Then we have reasons why the "functional" snippet doesn't get **even better**.

The imperative version has the following visible problems, and **I encourage 
you to reason the code snippets** to figure them out:

- Imperatively needs to add `async` to the function signature:
  - It leads to the "async-await hell".
  - It "colors" functions because of the above sub-item, so we have a
    **heterogeneous** system that differentiates between "normal" functions
    and "`async`" functions.
  - More boilerplate.
  - In con of this asynchronous model read the JEP for Java's virtual threads
    at the "Alternatives" section and watch the timestamp at "Async Await" I
    added to the [bibliography](#bibliography).
  - In "pro" of imperative (but not `async`-`await`) watch what B. Goetz also
    had to say about reactive programming on the [bibliography](#bibliography)
    but keep in mind that this and the above sub-item are in the context of
    Java and the other workaround languages like JavaScript, C++, C#, Kotlin...

- Usage of *mutable* variable `tree` for returning the function value:
  - Otherwise, we'd have a multiple-`return` mess (to get rid of the
    variable), whilst a competent `(programmer, language)` might as well
    directly replace it with a high-level declarative approach (e.g. ADTs with
    pattern matching).
  
- Usage of `try`-`catch` for error handling which has many disadvantages and 
  can be replaced with sum types or monads like Rust does, so there's no 
  reason why we should keep using `try`-`catch` blocks in robust software 
  development:
  - Code only gets more tightly-coupled.
  - Provokes many mundane workarounds like the mutable variable I said above,
    the `goto` antipattern I mention later, multiple-`return` mess, and 
    completely stupid checked/unchecked exceptions (both are terrible).
  - Java is the only useful language that implements *checked exceptions*, so 
    they're not a good feature as no other language have them, and 
    *unchecked exceptions* are much more terrible as they constantly hide 
    errors and that **disallows us** to produce *engineering grade* software.
  - Go and Rust error handling techniques are vastly superior to that of 
    imperative exceptions, and although Go is imperative, its approach is 
    returning a *tuple* (a math concept so functional) that works as a poor 
    man's `Result` sum type.
  - As always, it fragments functions into two kinds (like the asynchronous 
    item above): normal functions and the ones that `throw` an error. So we 
    have again a **heterogeneous** system that could otherwise be a 
    simple-and-robust *homogeneous* system (i.e. FP) that returns values like 
    `Result` instead of *procedures* or *methods* that only yield 
    arbitrary **side effects**.
  - Therefore, code gets quite messy, worked-around, and simple FP 
    approaches perfectly replace this archaic feature.
  
- Usage of OOP which might enhance lower-level imperative code but is complete 
  nonsense for high-level software (most projects) and yes, more boilerplate:
  - Notice the `new Error` line.
  
- As I say below, it still has and needs mixed components 
  (functional/declarative, OO, etc.) so **if we could go fully functional** 
  (with better languages) **why keep writing cheap code like that?**:
  - The functional component is universal as it's always present: 
    - Methods and procedures are poor man's functions as they always have 
      the vague idea of "inputs", "transformation", and "output".
    - Multiple-return methods are a poor man's ADT with pattern matching.
    - Same for anything else, we only use *alternative paradigms* because of 
      pragmatic convince like (if you checked the Java 
      [bibliography](#bibliography)) "Everyone already uses Java, and reactive 
      programming doesn't match Java-centric imperative constructs like control 
      and loop structures and also its tooling like stacktraces" so then 
      it's not a reactive's or functional problem but a Java one.
    - FP is universal as it's (attempts to be) a `1:1` mapping of math. Now 
      regarding Java for instance, the language is convoluted these days so 
      the "good enough" is not "enough" any longer (it ages poorly like most 
      languages) and I'd add my phrase here "All monoliths will eventually 
      fall apart" so we better build *homogeneous* systems instead if we don't 
      want our engineering artifacts be a waste.
  - Understanding that "most" of time we use (you like it or not) poor man's 
    FP, then that $$~80/90%$$ of time **can converge to fully-functional** 
    (evaluate for under/over-engineering here), **but not the other way 
    around** as said above that imperative always needs a mix of paradigms 
    but functional doesn't need other paradigms as *it's homogeneous*, or 
    simply put, math.
      
Regarding minor issues that arise in this basic code snippet are the following:

- Has more formatting constraints, I always put the `else` branch on a new 
  line (which gives more LoC) as it is factually the best way to format it.

- It's obviously quite prone to error and hard to read.

- Even the Mozilla docs for `Promise` only show toy examples, and real life 
  error handling gets worse with imperative code. Code on the internet like 
  docs or tutorials almost always skip the status `ok` error handling and go 
  directly to fail when parsing the `JSON` data. Who cares when code is 
  imperative anyway?

Also, notice how throwing from a `try`-`catch` block **is an antipattern** as it
becomes a `goto`[^2][^3].

[^2]: You can learn plenty of these details from IntelliJ IDEA inspections

[^3]: Understanding these details are what make you stand away from bad 
    programmers and make you a competent one

If TS had `try`-expressions (like Kotlin) the imperative version would get
much better regarding correctness and style, but **expressions are
declarative so functional** in the end 😋.

**The more you make a program better, the more functional it gets**, that's
because **FP is the only/original programming paradigm there exists as per
scientific concerns**, and all other paradigms are just cheap workarounds.

Notice we can go "pure functional" but not "pure imperative" or "pure OO" so 
the **workarounds** are clearly the alternative non-functional paradigms.

Another good one I know a lot from experience is that **the more I refactor code
to improve it the more domain-specific it gets**, and FP is clearly the natural
way to go for DSLs.

I used to be a huge fan of Java as an OO approach (still have my good reasons) 
but I got to understand those "clever" ways of programming are **just 
intellectual distractions**.

So, my favorite phrase I use to teach others or tell my story is that the
**simplest designs are the best** and FP is all about simplicity.

Mainstream languages like JavaScript, TypeScript, and Java don't have good
functional support, but we can still build better code regarding robustness 
and clearness by leveraging their available features and our computer science 
knowledge.

## Bibliography

- [Propping Threads Up by Missing Their Point \| State of Loom \| OpenJDK](https://cr.openjdk.org/~rpressler/loom/loom/sol1_part1.html#propping-threads-up-by-missing-their-point)
- [JEP 436: Virtual Threads (Second Preview) \| OpenJDK](https://openjdk.org/jeps/436)
- [Async Await \| AMA About the Java Language — Brian Goetz and Nicolai Parlog \| YouTube](https://youtu.be/9si7gK94gLo?t=1704)
- [Reactive Programming \| AMA About the Java Language — Brian Goetz and Nicolai Parlog \| YouTube](https://youtu.be/9si7gK94gLo?t=1360)
