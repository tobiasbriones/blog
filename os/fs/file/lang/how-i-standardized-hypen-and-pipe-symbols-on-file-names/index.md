<!-- Copyright (c) 2022 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# How I Standardized Hyphen and Pipe Symbols on File Names

Some symbols like "|" are not allowed as file names, and they would 
convolute the file system if used anyway so we better not use them there.

Source code has to keep in a well-designed shape so file names must be 
simple, but we can still encode relevant semantics to them to empower our 
source tree and get a clever system.

Encoding information on file/directory names as well as tree structures is 
important to naturally build a DSL from our file tree, so we have an 
expressive system that won't get fragmented since we're adding logic to each 
place (e.g. we don't need a centralized "index" file to tell file semantics 
because our domain-specific system naturally knows that information).

I've started to encode the hyphen (-) and pipe (|) symbols into file names 
for simple and language-readable or programming-readable source code 
files (i.e. no white spaces, only lowercase-alphanumeric and simple symbols).
Notice that articles are a language for me too (everything is a language, i.
e. homogeneous).

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
