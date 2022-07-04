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

### Monoliths are Full of Impureness

From my experience, I can tell that **monoliths are born from coupling two or 
more different things**. Yes, coupling is necessary to provide useful value, 
but they add impurities and must be minimized (a.k.a. loose coupling).

The most important engineering principles for any kind of engineering I can 
give so far are understanding:

- Abstraction
- Cohesion
- Coupling

I can give more like modules, homogeneity, etc., but I think you should 
focus on them to come up with further analysis. For example, you can *couple 
modules*, *high cohesion leads to homogeneity* by having the same 
responsibility, etc.

You must perfectly understand those at least to be a great engineer. Math 
gives you the training for acquiring that reasoning, but it requires plenty 
of study-time and intelligence to be equally good at math and engineering. 
Real engineers always need a substancial scientific and mathematical 
background to come up with robust designs, so you have to be good at all 
these if you want to be an engineer anyway!.

Like the example of computing the factorial with recursion, we only had one 
value (number 1) and one function leading to homogeneity, unlike OOP where 
you usually need to fragment logic, -as if was about a corporate policies-, 
into separate "UseCase"s. 

Corporates are full of heterogeneous peons (employees), so what does human 
resources has to do with engineering? I have lost many productivity because
unrelated-incapable people have to do our jobs. Everything is fragmented in
corporations. If you're clever enough to pay attention to details and
abstractions you can clearly see this.

Math creates partitions while OOP/capitalists fragment everything, that's 
another whole topic I use to talk about. Needless to say that you should 
use partitions (e.g. ATDs) to keep cohesiveness unlike heterogeneous 
fragmentation.

#### Monoliths Lead to Workarounds

If you look at the pyramid of power (a vertical structure), you can see 
peons are inferior to managers (I call them mega-peons) and mega peons are 
inferior to piglets. That's just a chess game with heterogeneous entities. 

They're not even real professionals. Real professionals are (autonomous)
engineers, scientists, mathematicians, researchers, doctors, etc., since we
don't need cringe-buzzwords/marketing/politics/capitalism derivatives. Just
imagine if an electrical engineer who deploys or builds satellites would use
absolutely stupid buzzwords fuzzywords like programmers or software "engineers"
do (e.g. "WET", "DRY", "AI systems 170k+ followers") instead of scientific facts
like electric charge, Coulomb law, etc. That's why software "engineers" are 
looked down as clowns for many people. But software engineering is not like 
their garbage, it is more like real science, facts, research, math, engineering
as is. That's up to you, and I encourage you to keep growing in the right career 
path as an engineer (a real one).

You wonder why OOP is inherently broken and is like capitalism? That's a
whole topic.

I recently came up to the conclusion that OOP is a complete scam (as many 
say, the trillion-dollar mistake, that's why you must be good at math and 
science) since they give you inherently coupled and lowly cohesive systems by 
the definition of classes or "objects" and then they sell you nonsense 
marketing. In other words, you are supposed to write loosely coupled and highly
cohesive software (that's what OOP teaches you and it's right but...) OOP is 
inherently extremely coupled and lowly cohesive:

- It couples data and behavior.
- It does two things at the same time: hold data and define behavior 
  (not cohesive and not reusable classes, they're big monoliths).

We write OO programs that are inherently coupled (like I said, OOP scales up
hardware incorrectness) with heterogeneous hierarchies. Why we depend on 
other departments like HR if they don't have anything to do with us?

Extremely coupled software/organizations end up like this, with 
heterogeneous hierarchies, of course, full of impureness.

They make you talk to recruiting people for example, once, I thought they 
understood about tech at least in general, but they're not tech 
professionals but psychologists so why we have to work with them for tech 
affairs?

That's why I talk about autonomous engineers moving along the dependency 
tree of diverse domains. You need to have your domain as an engineer to 
scale well.

What does OOP have to do with capitalism? After what I said, I can also
conclude that they create cheap software/solutions by using the *easy*
approach and then having messed-up systems that have to be workaround with
heterogeneous fragmented boilerplate policies or patterns until
it gets so coupled that it's impossible to change at all.

Some marketers say that structured programming is the "structure" and OOP is 
the "superstructure" (I'll talk more about it in another entry), so you can 
see the problem with the impurities I talk about (coupling things like crazy)
, because that cheap idea of OOP just doesn't scale. See the pattern (if 
you're a programmer you can easily follow me): structure -> superstructure.

The cheap idea of OOP doesn't scale because of its impurities. What if you
want to create a huge software system for going to Mars or another planet? You
will have SuperSuperStructureFactory, and you know the rest of the joke, they're
just cheap workarounds to bypass the actual problems. Impurities lead to cheap
poorly-defined systems that eventually must die when you can't hold the monolith
anymore.

Just imagine that coupling OOP has, but I didn't even talk about inheritance 
yet!!!.

Now can you see why FP/recursion/homogeneity/pureness/math is infinitely 
scalable, we don't see stupid fuzzywords or marketing in the factorial 
function, with a simple number like 1 we can populate the whole logic. 
Literally everything in a computer is a number, or bytes, so why don't use 
FP with mathematical background? That's why I say that **software 
engineering is the engineering that requires math skills the most** similar 
to electrical engineering where you see many two-majors with physics. 

As I've said before, OOP is just useful for workarounds in messed-up systems, 
for cheap software, but for good or bad, most software has to be like that, and 
hardware is inherently broken so OOP just makes it worse. Thus, OOP-done-right
is also inherently (pun intended) broken because hardware is meant to be used 
to write cheap software. If you do OOP-done-wrong (most OOP we see) then that's 
just nonsense.

Hardware does not even understand floating points, I just ran on Python a 
popular problem with floating point calculations, see the results below:

```python
>>> a = 0.2
>>> b = 0.1
>>> c = a + b
>>> c
0.30000000000000004
>>> c == 0.3
False
>>>
```

So any workarounds like OOP won't scale at all, and will just eventually 
become monolithic monsters full of impurities until they have to die 
naturally to restore that energy (workarounds) that was used to barely hold the 
monolith.

##### Avoid Workarounds

If hardware, imperative, procedural programming are inherently broken, why 
keep on this line of paradigms if it'll happen what I explained? Impureness
are propagated. This tells us that software doesn't need to be perfect, it's 
a feature not a bug (reference the meme), so we use OOP for cheap programmers
writing cheap software.

Engineering software properly can only be achieved using math just like an 
electrical engineer uses physics laws, so FP is the way to go here.

They can say, we can formalize and prove OOP, define new math, new science, 
etc., but that doesn't work that way. We can prove theorems without lemas 
and already-proved theorems, we can prove the monolith all at same, it's 
valid, but it's nonsense as we could get into a monster cyclomatic 
complexity leading into a system that *is not simple*. We should make FP 
more performant instead, like the innovations we see in the Rust language 
with zero-cost-abstractions.

I think it's imperative (pun intended) to say that my ultimate objective or 
wet dream as a math software engineer is to have everything formalized so 
that we run math as is, instead of workarounds like "C++ spaghetti bugged 
high-performance scientific code". For this, hardware architecture has to 
play well with pure math, but I'm not a computer engineer, so I can't work 
on that, and it'd be absolutely crazy and expensive to achieve something like 
that, so we have to use imperative general-purpose hardware, so I can think 
of a virtual machine since a physical one is out of my domain: the math virtual 
machine (MVM) as the platform to run math (as is).

We have to avoid workarounds, I'll provide you with the underlying 
definitions to make it clear:

> **workaround:** a method for overcoming a problem or limitation in a 
> program or system.
> 
> Source: [Oxford Languages and Google](https://languages.oup.com/google-dictionary-en)

So workarounds are literally (in Spanish) an "alternative solution". If we 
add patches like this, impurities come and propagated into the systems, and 
they start becoming monoliths, and you know the rest of the process. Recall 
that OOP is about building monoliths by definition, and that everything is 
relative, so how do you know it's a monolith or not?, hint. zoom in/out the 
system representation. But, math is pure by definition (while OOP is 
naturally impure).

> **Workaround:** A workaround is a method, sometimes used temporarily, for 
> achieving a task or goal when the usual or planned method isn't working.
> In information technology, a workaround is often used to overcome hardware,
> programming, or communication problems. Once a problem is fixed, a workaround
> is usually abandoned.
> 
> Source: [techtarget.com](https://www.techtarget.com/whatis/definition/workaround#:~:text=A%20workaround%20is%20a%20method,a%20workaround%20is%20usually%20abandoned.)

#### Do not Allow Workarounds

Now this subsection is about enforcing the philosophy of being professional 
and do things right.

Here applies a good phrase that looks fool-obvious, but it's necessary to
repeat to your chimp human mind all the time: **the only way to do things
right is doing them right** (no workarounds allowed). If we keep doing nonsense
that can't be proved but can only be sold, we'll be stuck in this nonsense
circle of workarounds forever.

##### My Story

I learned that phrase long ago (and it hurt) when I had to work with some
authoritarian piglets who love to have free employees just because they have
many degrees, and I was just a fool/nerd student with the best grades and
professor recommendations, so I was treated just as a "student without a
degree to join the bureaucrats" or peon in my words, but they do need the
free work!. Just an inferior student but who gives you the work of a
professional.

Companies or universities itself scam you because if you don't
have a degree on hand they don't value your work, so they make you work for
free, and you have to be "intelligent but be humble", don't criticise because
they achieve knowledge from a piece of degree paper, you're automatically
wrong because they have many degrees, and you're just a student (I'll write
more about this later) you're work is not valued because you don't have a degree
on hands but your free work does matter for them.

I'm usually open and trust people, they're supposed to be "educated" at 
university, but I learned for life that I must have not coupled my own projects
and creativity to work for free for any piglet because they're not your friends.

They're a complete mess as individuals/organizations, I really advice to never 
get related to those people.

How can you move forward if we need to pass by universities? We don't need to,
just go do social engineering at university, taking care of whom you contact 
to.

We don't need universities since they're a scam, an archaic way of "getting 
educated", they are absolutists like if you actually need them to fulfil your 
dreams and life (a monopoly of "educated" people), otherwise if you're not 
approved by them then they think you're supposed to be a loser for life (what 
can you be/do without a degree?).

One thing is to "validate your studies" which we all can do and in the end have
to do in practice working for some organization so university was just another
workaround to feel you learned, and another thing is to graduate a university
with internal agenda and businesses even if they're public.

As you can see, universities are monoliths or monopolies, they don't have 
real professionals unless they're prohibited top 25 universities, they're 
not simple as you need to go 5 years (another monolith) "to barely be 
someone in this life", the more degrees you get the more vertical you grow,
and you know what happens to monoliths. They don't have real professionals like 
industry because they're full of those impurities I mentioned (and more).

##### Be Professional

I learned for life to not come out with workarounds because the only way to 
do things right is to do them right.

You won't fix workarounds later, that's a myth, workarounds are not even 
fixable, just look at the disasters of "null" and "OOP" issues. Piglets want 
to sell, but you're a professional, so you want to do things right instead.

Nothing is perfect, you can fix small issues, but when something is not 
right, then is just NOT right to proceed. As an engineer you need to master 
the art of balancing tradeoffs.

#### I Give Factual Opinions

This is not just an opinion since I use to state factual arguments rather 
than give opinions. Being a mathematician, the worse you can do is to give 
opinions on core topics like these.

Well, in
[DDO and Power BI Overview](https://blog.mathsoftware.engineer/ddo-and-power-bi-overview)
I talked about "Without An Opinion, You’re Just Another Person With Data" 
but we're then talking about prototypes or early stage designs, what I
try to say is that, as a leader and mathematician you need to know how to emit
factual opinions.

#### Take Away

In short, monoliths are impure because they are extremely coupled to many 
things so impure means that it can't be mathematical analysed, or it's too hard
to reason about or model leading to inherently incorrect systems and stuff 
like "trillion-dollar mistake" just because many "engineers" learn 
"engineering" on YouTube and don't have the background required to be an 
engineer. Don't get it wrong, YouTube and such platforms are great to learn
development but not engineering at all. Development is a subset of engineering.
