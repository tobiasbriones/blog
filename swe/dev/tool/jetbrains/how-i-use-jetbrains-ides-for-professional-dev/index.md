<!-- Copyright (c) 2023 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# How I Use JetBrains IDEs for Professional Dev

## Local History

Sometimes, git is still not available to save the changes made to our code, and
we need to leverage local tools to recover information.

It can happen when you're refactorizing or redesigning the code when it's still
unclear what particular changes should be committed to version control so
the history makes sense and is simple to review by peers.

Professional developers create a well-designed git history by first making good
commits that allow an understanding of a project's development.

The same engineering principles you know are also used for designing commits:
building small independent changes that deliver working code and passing tests.
So, you create a meaningful project history by applying fundamental engineering
principles like cohesion.

Other reasons can come when there's no even remote git support from the
organization. Although it seems like a bad joke, sometimes we have to work
with no git or no proper git support because of bureaucratic rules,
prototyping, code that is complicated to use because it's proprietary, etc.
So, unfortunately, those reasons run out of our scope, but we need to keep
being professional; there's no excuse!

There can be various reasons why we're working locally, and even though
**we're not** (partially or totally) using git in those moments, **the IDE or
text editor is using it**, so we can address incoming issues with no problems.

So, when for any reason we can't commit yet, sometimes we can get into trouble
and need to recover uncommitted local changes.

### Recovering File History From Local Changes

![Local History: Cannot Undo](images/local-history-.-cannot-undo.png)

<figcaption>
<p align="center"><strong>Local History: Cannot Undo</strong></p>
</figcaption>

![Local History](images/local-history.png)

<figcaption>
<p align="center"><strong>Local History</strong></p>
</figcaption>

![Local History: Local Diff](images/local-history-.-local-diff.png)

<figcaption>
<p align="center"><strong>Local History: Local Diff</strong></p>
</figcaption>

![Local History: Commit 1](images/local-history-.-commit-1.png)

<figcaption>
<p align="center"><strong>Local History: Commit 1</strong></p>
</figcaption>

![Local History: Commit 2](images/local-history-.-commit-2.png)

<figcaption>
<p align="center"><strong>Local History: Commit 2</strong></p>
</figcaption>
