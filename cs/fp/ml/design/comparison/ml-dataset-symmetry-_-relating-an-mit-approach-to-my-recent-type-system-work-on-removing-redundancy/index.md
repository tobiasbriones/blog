<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# ML Dataset Symmetry: Relating an MIT Approach to my Recent Type System Work on Removing Redundancy

## How Symmetry Reduces Complexity

I was reading an MIT article about
[How symmetry can come to the aid of machine learning](https://news.mit.edu/2024/how-symmetry-can-aid-machine-learning-0205).
It struck my attention due to the last works I wrote about removing redundancy
in records with functional abstractions while designing the API of Canvas Play.

The MIT approach takes an insight into Weyl’s law, which measures spectral
information complexity and how it can be related to measuring the complexity of
datasets to train a neural network.

NNs can then be trained efficiently (by removing the symmetric redundancies) and
keeping correctness with even smaller errors —**especially important for
scientific domains**.

Results for enhancing sample complexity are guaranteed by a theorem pair
providing the improvement with an algorithm, which is also the best possible
gain.

## References

[1] [How symmetry can come to the aid of machine learning.](https://news.mit.edu/2024/how-symmetry-can-aid-machine-learning-0205)
(2024, February 5). MIT News | Massachusetts Institute of Technology.

[2] Briones, T. (2024, January 15).
[Removing Record Redundancy with Sum and Refinement Types.](/removing-record-redundancy-with-sum-and-refinement-types#factorizing-the-duplication-with-sum-types)
Factorizing the Duplication with Sum Types | Blog | Math Software Engineer.
