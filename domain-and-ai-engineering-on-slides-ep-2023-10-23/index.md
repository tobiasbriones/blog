---
permalink: domain-and-ai-engineering-on-slides-ep-2023-10-23
title: "Domain and AI Engineering on Slides EP (2023/10/23)"
description: "The development and blogging of the Slides EP was large, so this blog presents other notes related to the project."
ogimage: ""
---


<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Domain and AI Engineering on Slides EP (2023/10/23)

The development and blogging of the Slides EP was large, so this blog presents
other notes related to the project.

## Slides EP

I exhaustively blogged the development of the Slides EP at
[Building Slides from Screenshots App in JavaFX](/building-slides-from-screenshots-app-in-javafx)
with an associated JavaFX application that implements a master-view-detail GUI
with ORC assistance to create presentations from screenshots and code snippets.
The app includes complex and exciting features along its development.

## Domain Engineering

These general-purpose software out there cannot be composed.

Even if they have (bloated) AI assistance, macros, or even APIs
(if at all), they're just products for profit. Photoshop can paint an image, but
it'll never understand source code or your specific system. M$ Word or LaTeX
**will never understand what an equation means, *as in mathematics***, etc.

They need bloated features like AI that take huge deep learning models to
develop and train and have heavy licensing and marketing because they're general
purpose. For instance, products that suck need capitalism
(marketing) to *sell them*, but *they're not the solution to the problems*.

If you're a domain expert, you **simplify matters** to the specific domain, and
**the more you simplify, the less bloated** AI, marketing, analytics and
engineering you have because *you address the problems instead of the symptoms*.

AI can have its place to automate *external systems* which I wanted to leave
clear in this development, but it'll never replace the underlying domain as
they're independent tools.

That is, you need to *understand* **how AI should be useful to automate
works *for* our domain language** instead of buying mundane general-purpose
software that uses AI as magic. Notice *the difference between automation and
magic*.

For example, grammar checkers are not technical, so you get a lot of errors
marked because they don't *understand* computer and math languages and idioms.
They only want you to pay a subscription to fix all the "issues" ~~you have~~
they sell you. Their dream is to remove all the "issues 💸," so your original
tone even changes, and ends up sounding like a robotic agent or someone else.

They won't even tell you the *real* enhancements of your technical text you
should be fixing. They're not engineering grade. They tell you to remove
something for "clarity issues," then it tells you to add it back again 🤯 for
correctness.

In the end, **you have to be proficient** in English (i.e., the domain) to know
what you're doing because any "magic" tool was made just for the sake of a
profit agenda, thus bloated under the hood. You have to compose your tools
instead of paying for generic ones that turn into workarounds and will never
return most of the investment.

Another clear example is ChatGPT, which can generate mundane Python or any
popular mainstream language or framework code but struggles with ultra-niche
technologies like Purescript or even JavaFX. It makes up unexisting APIs and
code that wouldn't compile. These unpopular techs require domain expertise, and
there will be (hopefully) never enough data to train or fine-tune those
ultra-bloated magic-based (and unethical with IP legal issues) models with
technology that requires actual engineering.

In the end, **you must be proficient in your domain** and realize that tools
like ChatGPT are nothing but a faster way to *automate* what you otherwise have
to search in Google results.

General-purpose software is useful *but not engineering-grade*. On the other
hand, mathematical software must be engineering-grade by nature.

You should refrain from saying, "It was ~~generated~~ by AI" when, in fact, the
reality of how AI should work comes to "It was *automated* by AI." The exception
would be only when the whole tool (as a final product) is named after AI, e.g.,
"AI generated image."

There are likely possible ways to make AI work for our domain language (as said
above).

I figured out one simple way to leverage an AI application for this project, so
it serves as a good example.

Developing anything domain-specific here is natural but not the objective,
though. Thus, the purpose is to blog a new example project to **start taking
action, devising standards, and proving concepts**.

Recall the domain engineering automation concepts of what this comes about even
though my purpose this time is to develop these ideas conservatively in Java as
an example project.

### Domain Composition Versus Magic

It struck me as funny (after writing [Domain Engineering](#domain-engineering))
when I read "Adobe Photoshop API magic, now available in the cloud" 😂 on the
Photoshop developer page. It tells you to remove background via API, but you
actually need **composition**, like in FP. You should "compose backgrounds"
instead of removing them from a binary image. That contrasts *a simple solution
for the former versus a complicated one for the latter*.

Although you can compose layers in Photoshop or certain functionalities in
general-purpose software, you just can't take some 10% of the monolith and pay
10% of the price to actually compose it with a totally different domain like
math or programming. As said before, *it won't return most of the investment
since general-purpose software is not composable*.

Hence, as math software engineers, we must compromise to build composable
software to remove both needs for "magic" and for duplicating
expensive-to-engineer features.

## OCR Side Effects

It can be daunting to try to make ML models work. From my job experiences, we've
had to use several OCR providers as fallbacks. They all suck, but some suck less
for the underlying problem, so we might as well make that one the "primary
provider 💸."

One of the shoe stones you'll find is when the image quality is low or bad, and
you can't make your users buy an iPhone 15 Pro MAX ULTRA and employ basic skills
to shoot a photo 😣.

Solutions you find out there are generic, and if you try to make them work for
you by fine-tuning, they will just become the same over-engineered OOP
inheritance garbage based on product type classes that were popular in the Java
times, the same way "AI" is hyped today.

## AI should be a Bridge in your System Boundary

> Notice **AI can be a bridge between the general-purpose input and the
> domain-specific one, but the underlying domain will always exist**.

I mentioned this because many idiots believe the marketing idea that "AI is
everything" (same for other marketing-hyped concepts like capitalism or OOP),
while in fact, **everything is rooted in complex (usually "boring")
math, domain, and engineering facts**, so AI (capitalism, OOP, etc.) is just one
small part of a system, it's just one more tool.

## Applying AI to Automate your Domain

There are two ways to see AI, as "magic" or automation. As an engineer, you must
ensure you understand how to apply AI correctly since it is a heavy technology
with huge responsibilities. Applying AI correctly means it will be a mean to
automate your domain rather than using it as the "magic" that marketers,
general-purpose providers or practitioners will tell you.

There is a terrible misconception out there that anything hyped or trendy
becomes "the whole thing." For example, they believe that every random product
after the ChatGPT hype is an "AI." Any competent engineer must understand that
the AI stereotype (or "the current thing") is not supposed to do the job for
you. It becomes like the CEO or PhD who steals the credits because of their
political status.

Implementing complex features involving AI is expensive, and the Slides EP does
a good job, but it still leaves areas to optimize. Even though, it does provide
the design and documentation to solve the primary parts of the system.

The way I applied ORC AI in the Slides EP shows a clear insight on how to apply
AI properly, since it automates the slides when you only have raw inputs like
screenshots by converting it into domain concepts. Conversely, if your user
input is already efficient, like source code instead of screenshots, using AI is
unnecessary, it is all about composition, a balanced system design, and
optimizing for your domain.





