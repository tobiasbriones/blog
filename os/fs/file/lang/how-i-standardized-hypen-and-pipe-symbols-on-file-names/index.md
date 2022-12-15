<!-- Copyright (c) 2022 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# How I Standardized Hyphen and Pipe Symbols on File Names

File names can denote meaningful information so that we can use file systems 
as a domain-specific language by encoding symbols like hyphens (-) and pipes 
(|) into file names by extending the usual standard for source code file names.

## Hyphen and Pipe Symbols Semantics in File Names

Hyphens (-) (actual hyphens, not word separators) and pipes (|) can have
relevant semantics in file names and can also be encoded into file names
for that purpose.

A clear example is when rendering an article title from its file name:

From `how-i-standardized-hypen-and-pipe-symbols-on-file-names`
to `How I Standardized Hyphen and Pipe Symbols on File Names`.

But these can have other semantics like the title `Thoughts on XYZ
State-of-the-Art Model | Blog | Math Software Engineer`.

Some symbols like "|" are not allowed as file names, and they would
convolute the file system if used anyway so we better not use them there.

Source code has to keep in a well-designed shape so file names must be
simple, but we can still encode relevant semantics to them to empower our
source tree and get a clever system.

Encoding information on file/directory names as well as tree structures is
important to naturally build a DSL from our file system tree, so we have an
expressive system that won't get fragmented since we're adding logic to each
place (e.g. we don't need a centralized "index" file to tell file semantics
because our domain-specific system naturally knows that information).

I've started to encode the hyphen (-) and pipe (|) symbols into file names
for simple and language-readable or programming-readable source code
files (i.e. no white spaces, only lowercase-alphanumeric and simple symbols).
Notice that articles are a language for me too (everything is a language, i.e.
homogeneous).

If hyphens (-) are already used as word-separators or whitespaces in file
names, and pipes (|) are not allowed and are too complex for file names
anyway, so how to encode them in the simplest way?

## Standard File Names

Standard file names use to contain lowercase symbols from the below:

- Alphanumeric.
- Simple symbols like:
    - Hyphen (-): to denote a whitespace.
    - Underscore (_): to denote a whitespace.
    - Dot (.): to denote file extension or semantics.

Some exceptions can have capital or uppercase styles like camel/pascal case
and root files like `README.md` or `LICENSE` which are uppercase.

## Encode a Hyphen

As per the global standard, hyphens (-) are used to separate words in file
names so a simple extension is to be designed to add actual hyphens into
file names.

The simplest and most naturally-extensible design is to add a second hyphen
(--), that is:

- A hyphen is a word separator as per the usual standard: from
  `word-separator` to `Word Separator`.
- A second hyphen is mapped to an actual hyphen: from
  `state--of--the--art-word-separator` to `State-of-the-Art Word Separator`.

### Analysis

Adding a second hyphen to denote an actual hyphen is cohesive as both styles
keep words together and is also progressive as one hyphen is naturally a word
separator or a whitespace so, therefore two hyphens are represented as another
separator: the actual hyphen.

In other words, the whitespace (-) and hyphen (--) representations are both word
separators and the former is more frequent than the latter (hence the 
preferences in the number of hyphens to add) so this design decision is 
cohesive and progressive.

### Example

This was when I came out with this design:

From `triangle-for-tangent-point-at-node--to--node-line.svg` to
`Triangle for Tangent Point at Node-to-Node Line`.

## Encode a Pipe

As per the global standard, pipes (|) are not allowed into file names so a
simple extension is to be designed to add pipe symbols to file names.

Since pipes (|) are (more meaningful) word separators just like whitespaces
(default and most used) and hyphens (second most used), then a proper design
is to add a third hyphen (---) to denote a pipe (|).

So, adding this to the design listed in [Encode a Hyphen](#encode-a-hyphen):

- A third hyphen is mapped to a pipe: from `word-separator---blog` to `Word
  Separator | Blog`.

### Analysis

Adding a third hyphen to denote a pipe is cohesive and progressive for what
was said about word separators [above](#encode-a-pipe) so this is a correct
design.

The pipe operator is a fundamental element of functional programming (and so
MathSwe) so I considered it as a high-priority to add it as the third hyphen
in file names.

### Example

This was also when I came out with this design:

From `solutions-tree-with-html-and-css---ep-mrm.png` to `Solutions Tree
with HTML and CSS | EP: MRM` when styled as the (high-level) image name.

## Standardize Hyphens and Pipes on File Names

Hyphens (-) and pipes (|) can have relevant semantics to empower cleverer 
domain-specific file systems, but it's not trivial to denote such symbols as 
per the usual standard so, an extended standard was designed to define 
double hyphens (--) to be represented as hyphens (-) and triple hyphens (---)
as pipes (|) which for instance, can be useful to render image names from their 
file name, which leads us to turn file names into a language just like 
usual source code is.
