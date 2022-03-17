# Data-Driven Organizations and Power BI Overview

![Cover](images/cover.jpg)

<figcaption>
Image from 
<it>
<a href="https://pixabay.com/photos/chart-data-business-graph-finance-6765401">Pixabay
</a>
</it>
by Mariakray
</figcaption>

---

## Introduction

Organizations should own their data but also have a way to make proper use of
them. Data are facts and with data science, we can infer accurate reports about
what's going on in the organization.

There are many ways of performing data analysis, the most popular are:

- Python libraries (Numpy and Pandas).
- The Julia programming language is a niche for that domain.
- Jupyter Notebooks that can document results with Markdown but run the code
  right there.
- Even other popular programming languages like Kotlin, Java, and C# are used
  for data analysis or similar applications.
- Needless to say, systems programming languages like Rust, C++, or C also take
  part in this adventure for high-performance computations [^1].

[^1]: For example, Python libraries like Numpy are partly written in C so that
    Python is used as a C interface with an easier syntax but the underlying code is
    written in C [6].

Moreover, those are technical ways of developing data science applications. For
end-users, there are plenty of options too. One of the most popular is, of
course, Microsoft Power BI and Excel, and other competition like Tableau.

Power BI is a Microsoft 365 product that consists of apps and services to create
a powerful client for creating end-users reports from data. Like any other
Microsoft product, it uses a binary format, so it's useful for end-users to
understand their data rather than for data professionals.

Even though Power BI is a tool aimed at office professionals, it still allows a
more advanced use for technical staff, but it does not require knowing. Using
Power BI is like using any other tool from the Microsoft Office suite. That
enables an organization to quickly generate reports that may be deployed to the
web, the desktop, or mobile.

### Data-Driven Organization

According to *Building a Data-Driven Organization &vert; O’Reilly Media* [2], a
Data-Driven Organization should have a culture to use data for decision-making,
the organization with the structure to support it, and the technology that
supports it. The key is to develop that culture. Everything is data, for
literally any decision to take, use data as a fact to lead to that decision.

A problem with traditional organizations is requiring the so-called HIPPO
(Highest-Paid Person in the Office)
to make decisions [2]. Recall that you also need data permissions to acquire
them, so you usually depend on the HIPPO who will be far from good results,
that's a big problem for the data-driven organization.

Everyone should use data in the organization so the results are more accurate,
and you can check your organization is data-driven when data initiatives are
coming from the bottom of the hierarchy rather than the top or bureaucrats [2].

According to the *Five facts: How customer analytics boosts corporate
performance &vert; McKinsey* [7] survey, states that data-driven organizations
are 23 times more likely to acquire customers, 6.5 times as likely to retain
customers, and 18.8 times as likely to be profitable.

![mckinsey-exhibit-1](images/mckinsey-exhibit-1.svgz)

<figcaption>
<strong>Extensive use of customer analytics drives corporate performance 
heavily</strong><br>
Source: <it>Five facts: How customer analytics boosts corporate
performance &vert; McKinsey</it> [7] (under fair use)
</figcaption>

---

![mckinsey-exhibit-2](images/mckinsey-exhibit-2.svgz)

<figcaption>
<strong>Successful companies outperform the competition across full customer 
lifecycle</strong><br>
Source: <it>Five facts: How customer analytics boosts corporate
performance &vert; McKinsey</it> [7] (under fair use)
</figcaption>

---

### The Role of the CIO

The **CIO (Chief Information Officer)** is the top executive who has the
expertise to take the lead on the organization's information. It is a
professional for the business rather than for the product. The CIO has to make
sure about managing and getting results from the organization's information and
keeping up with the new information technologies. Instead of focusing on the
technical result of a product, the CIO focuses on the business results instead.

According to *Chief Information Officer (CIO)* [3], the definition of CIO is as
follows:

> A chief information officer (CIO) is the company executive responsible for the
> management, implementation, and usability of information and computer
> technologies. Because technology is increasing and reshaping industries
> globally, the role of the CIO has increased in popularity and importance. The
> CIO analyzes how various technologies benefit the company or improve an
> existing business process and then integrates a system to realize that benefit 
> or improvement.
>
> Source: *Chief Information Officer (CIO)* &vert; Investopedia [3]

As said before, the CIO must keep up with the current technological trends. An
effect of this is that, the definition of CIO before used to be more technical
but now with cloud computing, the new mainstream and emerging technologies there
is a plenty of opportunities for businesses to move faster but, this also
implies the CIO and, most importantly, the whole culture also keeps up to date
to be capable of constantly moving forward. This can be resumed into the
following comparison:

| **From**               | **To**                       |
|------------------------|------------------------------|
| IT-outcome-focused     | Business-outcome-focused     |
| Order-taking           | Collaborative agenda-setting |
| Supporting             | Compelling                   |
| Cost-controlling       | Revenue-building             |
| Process re-engineering | Data-exploiting              |
| Sourcing               | Creating                     |
| Function-focused       | Platform-focused             |
| Seeking parity         | Seeking differentiation      |
| Within IT              | Everywhere                   |
| IT-risk-focused        | Business-risk-focused        |

Source: *CIO Agenda 2018* &vert; Gartner [4]

### Watch Out Fake Friends

I found a fascinating article that actually encouraged me to write this entry.
First, let's see the following quote:

> Without data you’re just a person with an opinion.
>
> --- W. Edwards Deming (patron saint of the Total Quality Management (TQM)
> movement)

Then it makes sense, as stated before, data are facts, and you build science
with facts. If you don't have the facts then you just have an opinion.

Like anything in else in real life, this does not help too much in practice. Now
we know that "Without data you’re just a person with an opinion", right?.

Now check this out. Data are collected from social interactions which turn them
into not-real facts, extremely temporal-and-human-coupled, data are everywhere
nowadays, and finally, you must keep in mind the glorious inequality
`quality > quantity`.

This recalls me
that [Dijkstra](https://en.wikipedia.org/wiki/Edsger_W._Dijkstra)
wanted to make computer software out of theorems proving every library
correctness but in real life, computer science is as stated by its name, a
science and not math, so you prove a program wrong with unit testing instead
directly proving it right. That is also said by
[Robert C. Martin](https://en.wikipedia.org/wiki/Robert_C._Martin) in one his
lectures.

Facts and math are important, but you need to know how to use them properly. An
example can be the correctness proof for a significant subset of the Rust
programming language's safe type system at the
[RustBelt: Securing the Foundations of the Rust Programming Language](https://people.mpi-sws.org/~dreyer/papers/rustbelt/paper.pdf).

The article I talked about at the beginning of this subsection is *Without An
Opinion, You're Just Another Person With Data* &vert; Forbes. Pay attention to
the following interesting assertions:

> ... Roberta Wohlstetter argued that the Japanese attack succeeded because 
> of an overabundance of data: “At the time of Pearl Harbor the circumstances of
> collection in the sense of access to a huge variety of data were…close to
> ideal.” Problems arose, not from too little information, but from too much,
> and from the inability to glean useful "information" from mere "data." She 
> concluded that the job of lifting signals out of a confusion of noise is an
> activity that is very much aided by hypotheses.
> 
> Source: *Without An Opinion, You're Just Another Person With Data* &vert; 
> Forbes [5]

Having a lot of data is a responsibility and problem that needs to be managed
properly, otherwise it'll never work. This tells us that mere data are not facts
but noise.

Next, we have this quote to conclude this subsection:

> Executives who make effective decisions know that one does not start with 
> facts. One starts with opinions… The understanding that underlies the right 
> decision grows out of the clash and conflict of divergent opinions and out of
> serious consideration of competing alternatives. To get the facts first is
> impossible. There are no facts unless one has a criterion of relevance.”
> --- Peter Drucker
> 
> Source: *Without An Opinion, You're Just Another Person With Data* &vert;
> Forbes [5]

So, going back to the original quote, watch out instead for
"Without An Opinion, You're Just Another Person With Data". When you get to be
an expert at what you do then you will eventually be able to use data as facts.

In this introduction, I hope to had given you enough insight about data-driven
organizations and why you must make proper use of your data, so you can
articulate all this information into useful approaches. Next, I will provide
with the article an overview of Power BI that will help as an entry point to
building reports. That knowledge can also be used for any other tool or concept
in the data science field and, it's not necessarily coupled to Power BI.

## References

[1] Microsoft. (2022). Power BI on Microsoft Learn. Microsoft Docs. Retrieved
March 15, 2022,
from [Power BI on Microsoft Learn &vert; Microsoft Docs](https://docs.microsoft.com/en-us/learn/powerplatform/power-bi).

[2] Thusoo, A. (2022). Creating a Data-Driven Enterprise with DataOps. O’Reilly
Online Learning. Retrieved March 15, 2022,
from [4. Building a Data-Driven Organization - Creating a Data-Driven Enterprise with DataOps [Book] &vert; O’Reilly Media](https://www.oreilly.com/library/view/creating-a-data-driven/9781492049227/ch04.html).

[3] Chief Information Officer (CIO). (2021, June 17). Investopedia. Retrieved
March 15, 2022,
from [Chief Information Officer (CIO) &vert; Investopedia](https://www.investopedia.com/terms/c/cio.asp).

[4] Gartner. (2018). Mastering the New Business Executive Job of the CIO.
[CIO Agenda 2018 &vert; Gartner](https://www.gartner.com/imagesrv/cio-trends/pdf/cio_agenda_2018.pdf).

[5] Silberzahn, M. J. A. P. (2016, March 15). Without An Opinion, You’re Just
Another Person With Data. Forbes. Retrieved March 15, 2022,
from [Without An Opinion, You're Just Another Person With Data &vert; Forbes](https://www.forbes.com/sites/silberzahnjones/2016/03/15/without-an-opinion-youre-just-another-person-with-data/?sh=10542115699f).

[6] Numpy.org. (2022). GitHub - numpy/numpy: The fundamental package for
scientific computing with Python. Numpy &vert; GitHub Repository. Retrieved
March 15, 2022,
from [Numpy &vert; GitHub Repository](https://github.com/numpy/numpy).

[7] Bokman, A., Fiedler, L., Perrey, J., & Pickersgill, A. (2018, February 5)
. Five facts: How customer analytics boosts corporate performance. McKinsey &
Company. Retrieved March 15, 2022,
from [Five facts: How customer analytics boosts corporate performance &vert; McKinsey](https://www.mckinsey.com/business-functions/marketing-and-sales/our-insights/five-facts-how-customer-analytics-boosts-corporate-performance).
