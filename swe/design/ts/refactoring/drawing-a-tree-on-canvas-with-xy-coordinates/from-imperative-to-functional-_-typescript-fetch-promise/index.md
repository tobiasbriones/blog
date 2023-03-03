<!-- Copyright (c) 2022-present Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# From Imperative to Functional: TypeScript Fetch Promise

![From Imperative to Functional: TypeScript Fetch Promise](from-imperative-to-functional-_-typescript-fetch-promise.png)

This
[gist](https://gist.github.com/tobiasbriones/544350fc301ffe32c1dd64d2f6ec6d81)
and the following observations show how code refactorized to more-functional is 
superior to its imperative version in both general and particular terms to
this snippet that performs a simple fetch request to get a `JSON` value that 
models a tree data structure to be represented later by `HTMLCanvasElement`.

Both versions are correct and do the same, but the "functional" version
still has some imperative style because JS/TS is not a functional language but
the difference **is clear** again.

## Difference

First, I clarify that I added as much imperative code to the "imperative"
version to compare both approaches here, but my original code wasn't actually
refactored from the "imperative" version below to the "more-functional" one,
but from a "saner imperative" version in between to the "more-functional" one
instead.

So, we'll need to start analysing both versions, knowing that
[everything is relative](everything-is-relative), so I'm measuring here
"imperative" vs "more-functional" **with respect** to this code snippet or
problem, and that these observations still apply to universal cases.

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
<p align="center"><strong>More-Functional</strong></p>
</figcaption>

The refactored code (a.k.a. "more-functional") **is not functional**, but it
gets close. This is to avoid introducing functional abstractions like pipes,
monads, etc., on top of JS/TS which is not a functional language but a mixed
one as the underlying project is pretty short.

## Getting Started on Noticing Details

This is an analytical endeavour so observation is key to get the most out of it
by applying our CS and SWE knowledge and experience.

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
you to reason the code snippets** to figure them out.

## General Concerns

I'll address concerns from this snippet that can be explained out generally.

### Async Await

Imperatively needs to add `async` to the function signature:

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

#### Usefulness of Imperative with Async Await

`Async`-`Await` is syntax sugar for JS/TS in this case, so it *should* be
useful for making code simpler, but the important notice is *when*.

I've written backend and frontend code like this either with `async` or not, I
believed that "raw" `Promise`s were caveman some years ago, but found that
can be leveraged to promote "more-functional" code.

I have the conclusion that, **if writing imperative code is needed** for any
reason (bounded to one function hopefully), **then it's the only way
`async`-`await` fits correctly**, it makes the code cleaner, but functional is
still superior as it would get to make it way more robust and cleaner altogether
by eliminating the imperative component.

### Mutable Variable

Usage of *mutable* variable `tree` for returning the function value:

- Otherwise, we'd have a multiple-`return` mess (to get rid of the
  variable), whilst a competent `(programmer, language)` might as well
  directly replace it with a high-level declarative approach (e.g. ADTs with
  pattern matching).

```js
async function fetchTree(path) {
  let tree = newTreeNode();

  try {
    const res = await fetch(path);

    if (res.ok) {
      tree = await res.json();
    } 
    else { /* ... */ }
  } 
  catch (reason) { /* ... */ }
  return tree;
}
```

<figcaption>
<p align="center"><strong>Using Mutability to Avoid 
Multiple-Returns</strong></p>
</figcaption>

```js
async function fetchTree(path) {
  try {
    const res = await fetch(path);

    if (res.ok) {
      return await res.json();
    } 
    else {
      // Can't throw here as it's an antipattern //
      /* ... */
      return newTreeNode();
    }
  } 
  catch (reason) { /* ... */ }
  // Return until the last statement or inside the catch block? //
  return newTreeNode();
}
```

<figcaption>
<p align="center"><strong>Multiple-Returns are Worse</strong></p>
</figcaption>

Notice the last `return` statement, it could be placed inside the `catch` 
block or even both redundantly, and it'd apparently work well in the end. 
That's why **imperative is for "whatever it gets stuff done"-driven 
programming**, and we can't say a mixed language is "functional" just because it
only has some "functional" features. **A functional language is based on 
actual CS (math) instead so a random programmer can't make it up** just because 
"it works" or "pays their bills", so **FP does allow real SWE** because of 
this.

No doubt functional languages have massive applications for engineering 
domains like telecom, fault-tolerant systems, etc., that most "engineers" 
(many are more marketers, mundane programmers or whatever else than engineers 
but get called "engineers") will never get in touch with.

Multiple-returns are super confusing, are also marked by your IDE as such,
increase the cyclomatic complexity, and if in some particular situation a 
mundane programmer tell you they look like "the right tool for the job" then 
there's for sure a simple matching approach that can replace them (but maybe not
the language support and programmer's talent).

### Try Catch

Usage of `try`-`catch` for error handling which has many disadvantages and
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

**Notice** how throwing from a `try`-`catch` block **is an antipattern** as it
becomes a `goto`[^2][^3].

[^2]: You can learn plenty of these details from IntelliJ IDEA inspections

[^3]: Understanding these details are what make you stand away from bad
  programmers and make you a competent one

#### Explaining the GoTo Antipattern

I'll explain the `goto` antipattern here:

```js
async function foo(path) {
  try {
    const res = await fetch(path);

    // Check for response status OK
    if (res.ok) { /* ... */ }
    else {
      // 🚩 Go to the catch block, unclear whether it actually throws or catches
      throw Error(`Response not OK: ${ res.statusText }`)
    }
  }
  catch (reason) { /* ... */ }
}
```

**Notice** also how unclear imperative code is: if you copy-paste (popular
among programmers, even more now with "ChatGPT") the code above, it can either
(pun intended) `throw` if it's not into a scoped `try` block or else `goto`
the immediate `catch` block otherwise like the snippet above.

So as I say in [maintainability](#maintainability), if stupid `try`-`catch`
blocks are supposed to handle "errors" but we have all this working around
setups because the designers (if anyone even cared to do it) of these features
took pragmatic decisions back in that time (e.g. many mainstream languages we
use like JS and PHP weren't designed at all from the beginning), but now we have
much technology in $$2023$$ so **why keep writing cheap software like that when
we can employ informed FP?**.

The answer to the above question is that we shouldn't keep writing code like
that. Even imperative (modern) languages like Rust replace all these "bad OL'
tricks" with correct functional approaches (e.g. sum types, pattern matching),
more of these "imperative, OO old tricks" are not found at all in many
turing-complete languages, e.g. there are no exceptions or `try`-`catch` in
Rust, Go, and functional languages at all and were replaced with tuples, enum,
ADT, i.e. **factual functional features that should've been there since the
beginning**.

Now check this out, **there's a version of `try`-`catch` much better than the
old one** most programmers are used to, but that only shows (again) that
imperative is doomed (in an ideal setup) indeed:

If TS had `try`-expressions (like Kotlin) the imperative version would get
much better regarding correctness and style (no multiple-returns, no mutable
variable, lets think about it), but **expressions are declarative so
functional** in the end 😋.

Therefore, even a factually better `try`-`catch` ends up **converging**
closer to FP, and we don't even need it as languages like Rust don't even
have them at all as said above.

#### Trying with Less Coupled Functions

The next imperative approach attempts to reduce coupling, but when knowing
functional languages, why should I do this at all?

```js
async function fetchTree(path) {
  try {
    const res = await fetchRes(path);
    return await res.json(); // Might throw too
  }
  catch (reason) { /* ... */ }
  return newTreeNode();
}

async function fetchRes(path) {
  const res = await fetch(path);

  if (!res.ok) {
    // Catch it from production logs, maybe too late 😁 //
    // Throwing from here is OK this time as it's not a goto //
    throw Error(`Response not OK: ${ res.statusText }`);
  }
  return res;
}
```

<figcaption>
<p align="center"><strong>Uncoupling Functions is Still Unclear</strong></p>
</figcaption>

Add to this getting experienced programmers to balance all these unclear
~~tradeoffs~~ workarounds to the technical debt if that wasn't enough. In
these contexts there will always be biases for one approach or other, so
you're failing to build engineering because even if the taken approach is
good for the problem someone else (guaranteed) will always get zealot about
it so what you're doing is who-knows-who-opinion-based development instead.

### Mixed Paradigm

As I say below, it still has and needs mixed components
(functional/declarative, OO, etc.) so **if we could go fully functional**
(with better languages) **why keep writing cheap code like that?**:

- The functional component is universal as it's always present in some way 
  or another:
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
  simply put, math. What **actually needs mixed paradigms** are 
  human-centric issues, but FP doesn't.

### Maintainability

Imperative is obviously quite prone to error and hard to read.

I always go back to read the imperative version and is easily noticeable that 
is heterogeneous, and harder to figure out, I ask, how do I quickly know this 
works? It's literally imperative (pun intended).

Many details get hidden into that imperativeness, it levels up the 
maintenance overhead with technical debt requiring more professional 
programmers to debug and read line-by-line if something's missing.

If I want to see if (all) the errors are being handled and how, it's quite 
messy, the stupid `try`-`catch` supposed to "handle errors" is useless because
`throwing` from the `try` block is an antipattern as I 
explained, and decoupling imperative code to establish boundaries is 
**inherently hard** as code like that (mutable vars, loops, side-effects, 
etc.) is **tightly coupled**.

Notice how I emphasize in handling all the errors, and *I spotted* that the 
`HTTP` `status` errors are undervalued. You even need to be abe to handle all 
errors to work in tech companies, so it's more important in the industry than 
you probably think!

Another problem is that, we can attempt to write functional code, but JS is 
full of garbage (not a secret), so for example, `res.json()` `throws` an error 
which is imperative thus completely unfavorable for us by disabling us to 
employ functional features.

I just resembled this code that is pretty common and opened up the browser
console to run it with the following results:

```js
fetch('not-found.json')
    .then(r => r.json())
    .then(console.log)
    .catch(console.error)
```

<figcaption>
<p align="center"><strong>Common Error Mishandling (Functional API)</strong></p>
</figcaption>

With responses:

- `GET https://{ ... } 404`
- `SyntaxError: Unexpected token '<', "<!doctype "... is not valid JSON`.

So the error is not correctly handled, and despite using the functional API
it's just equivalent to its imperative counterpart:

```js
try {
  const res = await fetch('not-found.json');
  const body = await res.json();
  console.log(body);
}
catch (reason) {
  console.error(reason);
}
```

<figcaption>
<p align="center"><strong>Common Error Mishandling (Imperative)</strong></p>
</figcaption>

So **imperative code written as fluent calls** is not functional, so we have
the same issues when `Stream`s were introduced to Java and exceptions were
thrown from ~~functions~~ (sugared anonymous objects implementing a
`FunctionalInterface` 🤪), therefore we can't take the advantages of powerful
functional abstractions (now imagine Java programmers writing `try`-`catch`
inside `list.map(...)` 😂).

The problem is not functional, or that functional doesn't fit Java, the
problem is that you can't write functional 🧠 when thinking imperatively 🦍.

Moreover, functional doesn't need other paradigms as it's homogeneous but other
paradigms **always** needs a mix, the detail is that **effects (programmers)
are the ones who actually need mixed**, because merging functional and mixed is
like merging science 🧠 and politics 💩, that is, incompatible. In other words,
awful code goes to the boundaries, so we're *able* to practice FP most of the
time but mundane mainstream languages *won't help at this* as previously seen.

In the end you always have to use functional (whether you like it or not) so
leveraging its full advantages is a trait of competent engineers.

Even the Mozilla docs for `Promise` and `Response.json()` only show toy
examples, and real life error handling gets worse with imperative code. Code on 
the internet like docs or tutorials almost always skip the status `ok` error 
handling and go directly to fail when parsing the `JSON` data. **Who cares when
code is imperative anyway?**

#### Mixed Paradigms is not Quite a Good Idea

Mixing paradigms is useful for alternative paradigms like imperative, and
OOP but when we can write homogeneous simple functional programs we must
keep writing functional programs.

The reason is simple: FP is homogeneous (pure) so if you introduce
workarounds (imperative, OO, etc.) it becomes heterogeneous 😖, it's like
"proving" a theorem with human chimp 🙉 opinions instead of using math to prove
itself recursively.

#### A SWE Experience

After writing the
[first and short version of this article](https://github.com/tobiasbriones/blog/pull/24)
some days later I had an experience with a SWE applicant, and it was fascinating
that I noticed the same issues I said here. The *fetching client code* in TS
was **confusing**, **missing error handling at all** (a must to have had),
and a **mix of `await` with `then`**.

But that's not it, it's also hilarious that obviously (one more time), the
`res.json()` error wasn't handled if the `JSON` response is corrupt to avoiding
a cryptic error as I had already said in
[Maintainability](#maintainability), but check this out: **if that wasn't
enough**, there was an **injection** vulnerability because of a "JSON" that was
actually a hardcoded string with format that was returned from the backend,
and if you inject the `"}` symbol to ruin that string representation of what
should be an actual `JSON` response then *you get the holy cryptic error I'd
already warned here*.

That imperative code felt like code that has to be written all the way wrong, 
as if they get paid to write bugs on top of bugs (popular among programmers I 
think).

Even so, the code was good overall (compared to the hell of spaghetti I've
had to review in several other experiences in my professional life), and also
taking into account that a codebase doesn't have to be perfect at the beginning.

All these sorts of issues must not be propagated as we're in a relatively
advanced 🐵 tech era in 2023 already! (who still writes `for` loops anymore when
we have high-level functional APIs?) since that's where the *engineering grade*
I talk about lies within! As (math) software engineers on the other hand, we can
take *informed decisions*, and we know that a *necessary* condition to apply FP
is *to know what you're doing (i.e. informed decisions)*. That's a reason why
the SWE industry keeps using awful mainstream languages: because they don't have
idea of what they're doing, they just want to build businesses for profit but
not for the *engineering*.

So, that was a (another) great exemplification for me of all the wrongdoings
that happen with imperative mixed code like this.

So, *here we come again*, **when it comes to imperative, who even cares?**
it's just programmer chimp 🐵 opinion-based programming with no
engineering-grade principles.

#### Shaping SWE Properly

You can say **horrible code can be written in any paradigm** but there's **a
fact** that FP is algebra and **not a matter of style** so can *actually* be
(re)factorized like those "useless" polynomial factors from high-school or
college most software "engineers" fail to use in real life while alternative
paradigms are instead mere opinion-based (OOP is the worst in this regard).
Do you wonder why Rust (an imperative systems language inspired in functional
languages) is able (i.e. *is informed*) to give such meaningful compiling
messages?

Another take to make it clearer is, if you don't match all the sum types
(e.g. the holy error) it won't even compile, but imperative is like 🐒 return
`-1` as an error "code" or use implicit `goto`s, so "whatever it gets stuff
done", so I can't call those uninformed practices "engineering" but "stuff
doing" instead.

So those are "bad" programming practices then don't write code like that, 
right? But when you fix all those wrongdoings it ends up as a DSL 
**converging** to FP. For instance, you create a `Result` `enum` to `match` it,
so you're forced to handle the error, and it also has an appropriate data type 
instead of nonsense like `-1` 😂.

Another annoying take is when they say "use the right tool for the job" or 
"if a future exists is because it has its use". That's a problem because 
stupid programmers often solve the **symptoms** not the real problem so "the 
right tool" is frequently misunderstood. For example, you get sold on Java 
and use OOP, so now you have to "buy" (unfortunately popular) old nonsense 
books about OO "architectures" to write "clean" code to fix the problems on 
top of problems (a.k.a. `ProblemFactory`), so employing a "hex architecture" or 
"encapsulation" sounds like a "good" practice at first but a better programmer 
who actually studied CS and doesn't think that Java or Python is the only 
language there exists and knows, will flatten the roots of the problem instead.

The above can be straightforwardly seen by understanding that math is 
homogeneous and doesn't have "face" or "opinions" but other domains like 
businesses have a lot of impurities, so they need designs like "private" 
property to "protect" them from the same human evils, thus it makes sense to 
use "OO encapsulation" in these cases, although they're just workarounds 
propagating out of the computing space and that's why **they will never scale**.

So in short, you'll **mostly** need facts (FP) for any SWE application and will 
have to add (to the boundaries hopefully) heterogeneous workarounds to fit 
your domain, and not because "FP needs a complement" but because you're all 
mere mortals, who lets say (in an artistic fashion), "need management to hide 
"secrets", pay a lot to capitalist manipulator scammers who get profit from 
badly designed systems and social needs to sell the patches, ...".

Also recall that traditional engineering is based on facts like electricity so
to turn mainstream SWE into (real) SWE we need a "fact check" domain, a perfect 
example is mathematical software, i.e. SWE ruled by math the same way EE is 
ruled by heavy physics so that's the pattern I always have in mind.

## Minor Issues

The following are minor issues that arise regarding this basic code snippet.

### Whitespaces are a Terrible Design

Imperative has more **whitespaces** like `await fetch(path).then(...)`
which makes the code **less cohesive**. Whitespaces are a horrible design,
imagine file names with capital and whitespaces, it yields many bugs and
workarounds, and by fragmenting our code in this case it makes it worse to
build **a fluent DSL**. You'd likely chain other `then`s so using a mix is a
heterogeneous approach again.

### Unclear Constraints

Imperative has more formatting constraints, I always put the `else` branch on a
new line (which gives more LoC) as it is factually the best way to format it.

### OOP

Usage of OOP which might enhance lower-level imperative code but is complete
nonsense for high-level software (most projects) and yes, more boilerplate:

- Notice the `new Error` line.

OOP belongs to the imperative paradigm line, so it might make sense for
applications like FSMs with mutable state, so this proves my position
regarding OOP as a workaround: if imperative is terrible per se, so OO is a
solution for the **symptoms** build on top of imperative, and that gives us a
proportion of huge magnitude regarding how often we should use it. It's a
patch on top of something inherently messy, you should rarely use it.

No doubt that OOP "done right" ends up like **poor man's FP** (again) based on
poor and **cheap marketable** versions of the same functional principles,
because if we used OOP as it should actually be used to enhance imperative or
procedural programs we'd end up applying it in very few cases.

So, I just put the `new Error` line to the imperative version to try 🤭 to go
"fully imperative" to get the difference between both approaches in this study
case, but it's obviously not required to "build" an object instead of passing a
simple struct.

## Final Observations

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

## Leveraging FP as the Universal Approach It Is

Mainstream languages like JavaScript, TypeScript, and Java don't have good
functional support, but we can still build better code regarding robustness
and clearness by leveraging their available features and our computer science
knowledge that was applied in this case study to address relevant concerns
about TS Fetch API by observing the underlying imperative and
more-functional features.

## Bibliography

- [Propping Threads Up by Missing Their Point \| State of Loom \| OpenJDK](https://cr.openjdk.org/~rpressler/loom/loom/sol1_part1.html#propping-threads-up-by-missing-their-point)
- [JEP 436: Virtual Threads (Second Preview) \| OpenJDK](https://openjdk.org/jeps/436)
- [Async Await \| AMA About the Java Language — Brian Goetz and Nicolai Parlog \| YouTube](https://youtu.be/9si7gK94gLo?t=1704)
- [Reactive Programming \| AMA About the Java Language — Brian Goetz and Nicolai Parlog \| YouTube](https://youtu.be/9si7gK94gLo?t=1360)
