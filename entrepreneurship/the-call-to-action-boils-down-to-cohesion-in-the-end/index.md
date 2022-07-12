<!-- Copyright (c) 2022 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# The Call to Action Boils Down to Cohesion in the End

![Call To Action](images/call-to-action-poster.png)

<figcaption align="center">

<strong>Call to Action</strong>

</figcaption>

---

The *call to action* boils down to cohesion in the end. Marketing, capitalist
buzzwords, and OOP always have to end up in the same fundamental principles of
FP/math/science/real-engineering.

Although buzzwords have *something helpful*: they're easy to understand for
non-geniuses or average people. In this case, for business people.

## Call to Action Design

Call to action consists of giving a clear way to the user to do what the
business wants them to do (e.g. add to cart) unlike those archaic desktop 2000s
WinXP apps with hundreds of bloated enterprise and small forms, buttons, and
options in the same window. Or bloated unresponsive websites full of options
and advertisements.

Software used to be a monolith in those archaic times of computers and
incipient programming (recall that computers were square back in the day and
everything was unclear and full of obnoxious workarounds 😂).

Now we have the capability to enforce better-designed systems that
are cohesive:

- 🐵 do one thing (homogeneous whole) and well done, instead of

- 🙈 everything (a monolith) and done mediocre full of workarounds like OO
  systems.

If you present the user with a bloated app they won't focus on what to do, but
you *want them* to make sure to buy your stuff and do it right, so you maximize
your profit and also the user's effort to get value from your business.

That doesn't mean you can only put a function into a file or a button into a
screen, but instead, the same logic for a module, the same logic for a screen,
etc.

### Understand When It's Bloated

An adult human mind can only hold 4 different things on average [1]. So, what
matters is not to limit to "one button per screen" but the same kind of
logic/interaction in one screen (scales horizontally).

*It's relative:*

- if you go to [StackOverflow](https://stackoverflow.com) you
  can see a cohesive whole as the entire website and that makes sense for you
  as a user who wants to move around the website to obtain value from it, but
- if you're the programmer then the whole website is a monolith with many
  *orthogonal* concepts.

The science of this is that you have to define *homogeneous* entities or things.

The actions in the top right menu are cohesive, I can know that because
*they're close to each other*, so I expect the underlying logic to be
homogeneous too, all menu items are homogeneous or of the same type so the
bundle of actions in the menu is cohesive.

Notice that "close" is *relative* to your metric, so you can define you
metric space or use the Euclidean distance, now you can see why you must be
good at math to be a good software engineer.

A small change in the input (mouse cursor or keyboard keys) produces a small
output close to the sibling entities (menu actions) output. This *measure* of
homogeneity is what defines the quality of the software or design of your
system.

The art of cohesion places homogeneous entities/things close, so they can
build a united cohesive whole. A highly cohesive system can be more
mathematically proved whilst heterogeneous systems with orthogonal
integrations are a complete mess.

Therefore, if you want to optimize for an action in your screen, you must
give the clear UI/UX to the user to achieve only *that action,* so you *call
to the action*. If you give orthogonal options to that action to the user
then it's become a heterogeneous system and the results are more difficult
to prove or test and the user will likely take a suboptimal decision if any at
all.

#### Side Note on (Un)Deterministic Input and Output

Some people might say that there are systems where "a small change of the input
produces a random change in the output" like chaotic dynamic systems,
cryptography, stochastic processes, etc., but those are just temporal
workarounds to get good-enough results faster, we want forward engineering here
instead.

I'd call those systems that are not cohesive like AI, data science, or
stochastic processes *minimal workarounds* or *controlled workarounds* since
they produce great results but are just temporal or short-lived *methods*.

I see them as minimal workarounds that are proved-enough or partially proved
hence they work. How do you know AI or DS is going to work? Because they're 
not deterministic but have been proved-enough to make *much* fewer mistakes 
than humans, so they are good at fixing the workarounds of the 
non-deterministic mess we find in real life like getting information out of 
semi-structured HTML files downloaded from the news external website that 
are not modeled like we were the owners of the news company (notice how 
"you" and "the news company" are external systems and heterogeneous, so we 
need the temporal workaround I talk so much).

On the other hand we find nonsense like "enterprise OO systems" which
build workarounds on top of workarounds so, they're anything but minimal or 
controlled workarounds, they're just a nonsense mess.

Your business logic is not short-lived, it's more like gravity or physics
constants: they're long-lasting. So the less temporal the more cohesive it
has to be.

Imagine if you jump and end up in another galaxy, see my point? On the other 
hand, we can have arbitrary t-shirt colors from one day to another and that 
*short-lived event* doesn't change the raw universal laws that are cohesive and 
*long-lasting*.

That's why non-cohesive AI, stochastic models, etc. work, because
they are for temporal solutions and *are built* on top of cohesive models
like algebra, physics constants, deterministic models. Have you heard a
stochastic process yields a different output from *family* of know possible
outcomes unlike differential equations that give a deterministic output given
the same initial condition? Can you get me? They are based on math and 
physics laws in their core anyway, so they're controlled workarounds.

I develop on these wonderful topics in my other article [everything is 
relative](../../philosophy/everything-is-relative). 

#### The Subscription Model Example

We usually see that subscription plans are minimal and give 3 options on 
average. You can go to any commercial platform and verify this.

If they give more options, then the user will start overthinking 
*workarounds* but if you employ highly cohesive flows then the user only has 
to focus on one thing at a time and thus make a more optimal decision.

### Marketers vs Scientist

Cohesion is one of the most important universal principles, and I teach it a
lot, as you can see, it applies to any problem, disregarding the buzzwords they
make.

OOP marketers (they're not programmers) call it SOLID, etc., but in the end,
everything formally ends up in the same scientific principles (with no marketing
buzzwords). The way they see it, is just an easier way to understand it for
their context.

Like some say, OOP ends up much like FP when applying "OOP principles".

Imagine if real engineers like electrical/civil engineers or math software
engineers would use cringe buzzwords fuzzy words like "WET", "YAGNI", "Tell
Don't Ask", etc., (I don't even know/remember what they mean, and we DO NOT
want to know either). Then now, imagine cities, electrical systems of your
house, spacecrafts, car engines, traffic lights, etc., would be a complete mess
developed by a chunk of stupid people unable to understand science.

It's deplorable that many "engineers" or "programmers" don't know what it
means "DSL", and they even say that they don't care, but they obviously know
the cringe OOP words because they're cheap programmers. It's fair if you
don't know something but being a bottom programmer (IT labourer actually) is
completely different. I've also explained that *everything is a language* so
a software engineer must positively understand what "DSL" means, to then build
homogeneous systems.

A real engineer must understand the scientific principles of their
engineering discipline like for example, electric charge/field for electrical
engineers, differential equations for civil engineers, set theory for
software engineers, etc.

## Cohesion is Universal

The dictionary states that cohesion "forms a united whole" so here your work as
an engineer is to define what the homogeneous "whole" means since everything is
relative, that is, you must remove the heterogeneity to get a cohesive "whole".
That proves that cohesion is a universal axiom of design.

Cohesion is a universal principle, it can even be proved that "lexical cohesion
is computationally feasible to identify" [2]. The education system doesn't teach
you any of these fundamental topics, they're clearly not cohesive organizations
so schools are just cheap workarounds instead of what they're supposed to be.

FP tells you *what to do* or what functions you can call, FP concepts
also ensure to keep the logic flow cohesive. You can't call a function that
is far away and does not make sense for the current data type. It's like a
`Result` which can only be defined as `Ok` or `Err` so you *know what to do*
and carry on.

### Cohesive Flows

As you can see, we find how cohesion is formally applied as a measure for

- Physics
- Software Engineering
- Natural Language Grammar
- Even Marketing

You can employ cohesion for any long-lasting design.

Highly cohesive flows are about building homogeneous subsystems that focus
on the same content, so you can go back and forth and don't get burned.

In contrast, see how a heterogeneous system that has vertical integrations
draws a lot of energy. Think of a switch or a three-phase industry-power engine
that has to turn on and off: the drastic change of inertia takes a huge
amount of energy and does not focus on the same thing, my UPS goes to 
battery at the moment they turn on this kind of equipment, so *non-cohesive
systems affect random external systems* that have nothing to do. What does my 
computer has to do with the side effect of turning on a far away washing 
machine? That's because those electric systems get from zero to full power 
at once, so that implies a lot of things to do at once, and so poorly done. 

Another way to put this even more clear: non-cohesive systems do not focus 
on one united whole, but they fragment that whole (like OOP does) and side 
effects from that fragmentation are going to end up in my computer UPS 🥴.

You need to have cohesive workdays to avoid getting tired with heterogeneous or
vertical tasks that make you change the context uncontrollably, so cohesion, 
of course, also applies to your personal life.

## Conclusion

The call to action is a modern approach to interface with our users that helps
build cohesive flows so that the user is focused on retrieving value from our
software/products/services and the business also maximises their profit by
having more engaged users who are presented a clear way to solve their problems.

If you do it wrong, you'll bloat users with orthogonal options that only
create an unmanageable cyclomatic complexity. So users will not even keep
burning their heads into your platform and won't be clear *what to do*.

I'm a relativist who enjoys and applies these principles all the time, you have
to read my article on
[everything is relative](../../philosophy/everything-is-relative), I've been
developing these days with more insights I have every day.

## References

[1] Finn, E. (2011, June 23). When four is not four, but rather two plus two.
MIT News | Massachusetts Institute of Technology. Retrieved July 11, 2022,
from https://news.mit.edu/2011/miller-memory-0623

[2] Morris, J., & Hirst, G. (1991). Lexical Cohesion Computed by Thesaural
Relations as an Indicator of the Structure of Text. ACL Anthology. Retrieved
July 11, 2022, from https://aclanthology.org/J91-1002.pdf
