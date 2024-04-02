<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Cookie Consent v0.1.0 | MathSwe Legal (2024/03/29)

## New Cookie Consent Microservice

The new Cookie Consent service was delivered as per the
[New Cookie Consent Microservice Ready | MathSwe Legal (2024/03/28)](/new-cookie-consent-microservice-ready---mathswe-legal-2024-03-28)
development, reaching its `v0.1.0` release.

## Requesting a Consent

Allowed MathSwe website and web apps can now send cookie consent requests for
their process at the production endpoint to complete the work of the cookie
banner or preference pane.

When users set or update cookie preferences, the client has to send
the `CookieConsentPref` JSON body to the service to finish validating the
request.

| Path | Method | Body                | Response        |
|------|--------|---------------------|-----------------|
| `/`  | `POST` | `CookieConsentPref` | `CookieConsent` |

`Cookie Consent Request Body`

```rust
pub struct CookieConsentPref {
    essential: bool,
    functional: bool,
    analytics: bool,
    targeting: bool,
}
```

Notice that `essential` is, in theory, always `true` and does not require
consent. The other values go according to the preferences the user chooses.

When processed correctly, the server will respond with a `CookieConsent` JSON
value.

`Cookie Consent Response`

```rust
pub struct CookieConsent {
    id: String,
    value: CookieConsentValue,
}

pub struct CookieConsentValue {
    domain: Domain,
    pref: CookieConsentPref,
    created_at: DateTime<Utc>,
    geolocation: Geolocation,
    anonymous_ip: Option<AnonymousIpv4>,
    user_agent: String,
}
```

The `id` field is a unique `String` value that identifies a cookie consent
record.

The other specific values correspond to:

`Specific Response Values`

```rust
pub enum Domain {
    MathSweCom,
    MathSoftware,
    MathSoftwareEngineer,
}

pub struct Geolocation {
    time_zone: chrono_tz::Tz,
    colo: String,
    country: Option<String>,
    city: Option<String>,
    continent: Option<String>,
    coordinates: Option<(f32, f32)>,
    postal_code: Option<String>,
    metro_code: Option<String>,
    region: Option<String>,
    region_code: Option<String>,
}

pub struct AnonymousIpv4(String);
```

Regarding the `Geolocation` type, I've been reviewing it and found many
unnecessary details that I will remove for `v0.2.0`. That is because I modeled
all the geolocation data Cloudflare provides in an HTTP request in case "it's
needed," but it includes many nonsensical fields to remove, and many seem to
be `null` anyway. Therefore, the next release will apply data minimization to
this geolocation information.

Regarding the `AnonymousIpv4` type, it applies data minimization by converting
the user IP into the last-digit anonymous IP. The technique makes the last IP
octet zero to minimize storing the full IP address, for example, it
turns `xxx.yyy.zzz.www` to `xxx.yyy.zzz.0`.

**The client must store at least the essential parts of that response**,
probably in the cookies itself, to provide the user with the ID generated for
the effective consent and the updated cookie banner preferences.

**Essential response data is** the `id: String`, `pref: CookieConsentPref`,
and `created_at: DateTime<Utc>` fields. The ID is the key to claiming a
consent record. Recall that anyone who enters a website can give consent without
any login, so the consent ID identifies *who gave consent*.

The current production deployment is available at
`https://mathswe-cookie-consent.tobiasbriones-dev.workers.dev`, where it'll
receive allowed MathSwe client requests via the method `POST`.

The server will reject any requests from other domains (including Postman tests)
with a `Forbidden 403` status.

The [Cookie Consent v0.1.0 documentation](https://github.com/mathswe/legal/tree/v0.1.0/cookie-consent#cookie-consent-1)
has further details to integrate it with a client.

The release and deployment of the Cookie Consent v0.1.0 service enable client
cookie banners to finish most of the remaining compliance requirements after
users consent to how the underlying website or web app may use their cookie
data.

## Complying with the GDPR

Complying with the **General Data Protection Regulation (GDPR)** is often an
advanced requirement to allow business operations in some EU countries. The work
of the new Cookie Consent service helps with the advanced requirement of consent
demonstration.

### Consent Definition and Implementations

User consent is necessary to process their personal data. The client
applications must request consent transparently and allow users to update
consent preferences at any time.

The GDPR defines **consent** as follows:

> ‘consent’ of the data subject means any freely given, specific, informed and
> unambiguous indication of the data subject’s wishes by which he or she, by a
> statement or by a clear affirmative action, signifies agreement to the
> processing of personal data relating to him or her;
>
> Source: #11 \| Art. 4 GDPR – Definitions [1] (under fair use)

Moreover, the GDPR is an upper boundary to take as a reference, as said by
GDPR.eu:

> The General Data Protection Regulation (GDPR) is the toughest privacy and
> security law in the world. Though it was drafted and passed by the European
> Union (EU), it imposes obligations onto organizations anywhere, so long as
> they target or collect data related to people in the EU.
>
> Source: What is GDPR, the EU’s new data protection law? \| GDPR.eu [4] (under
> fair use)

These definitions are general for legally processing personal data; they're not
limited to cookie usage. So, the process applied to the Cookie Consent
microservice and the cookie banner has *started building a framework for further
consent requirements in MathSwe*.

For example, if you want users to leave their email, which is personal data, you
can update them with a newsletter. So, in other cases besides cookies, I can
leverage the experience and standards of the cookie legal implementations
developed so far to comply more easily with more kinds of data protection
regulations in the future.

As always (while open-source is feasible), MathSwe supports first-class
open-source standards with thorough transparency, and the current cookie legal
implementations are one more proof of that.

The GDPR definitions can be leveraged as raw legal facts to keep implementing
robust compliance in the future. It's also crucial to monitor and adapt to
evolving regulations and interpretations to maintain compliance effectively.

The study and implementation of the Cookie Consent service, the coming front-end
details left to finish, and the coming redaction of the cookie policy are taking
place. All of them will act as a powerful framework to maintain a robust legal
basis in MathSwe in the future.

### Proof of Consent

Advanced legal requirements in some EU countries encompass storing consent
records as proof demonstrating you're requesting proper users' consent.

> Where processing is based on consent, the controller shall be able to
> demonstrate that the data subject has consented to processing of his or her
> personal data.
>
> Source: #1 \| Art. 7 GDPR – Conditions for consent [2] (under fair use)

The Cookie Consent service will receive requests from MathSwe websites and web
apps to process and store the famous **proof of consent** to demonstrate user
consent on cookie preferences.

Users can also update their consent at any time.

> The data subject shall have the right to withdraw his or her consent at any
> time.
>
> Source: #3 \| Art. 7 GDPR – Conditions for consent [3] (under fair use)

The client app allows the user to set their preferences to update their
consent [^1], so it will emit a new consent through the microservice, and the
client app will overwrite the old consent.

[^1]: Via the footer's "Cookie Preference" button

Notice that the service gives a unique ID to each consent.

Consents can come from any user, like anonymous users without a personal
account. So, the consent ID defines *who gave it*. Therefore, you can claim the
consent record with the provided ID stored in the client app, so it is key[^2]
*to show this ID in the cookie banner* when consent is effective for that
client.

[^2]: Pun intended since the consent ID is *the key* to the consent value in the
    KV (Key Value) database

Demonstrating consent records requires complete information. It also requires
data minimization to avoid collecting more than needed. In that regard, you can
check [the `CookieConsent` response](#requesting-a-consent) to see how
accurately data is stored but minimized, like the `AnnonymousIpv4` that avoids
storing the full IP address while still serviceable. As said, the next release
effort will also apply data minimization to the `Geolocation` type.

One more challenge is to keep the reference of the information provided to the
user at that moment, that is, the cookie banner info and the cookie and privacy
policies. The Cookie Consent service doesn't store this boilerplate but can be
inferred thanks to the `mathswe/legal` repository, which will contain the policy
redactions versioned with Git.

The release celebrated in this article is a clear example of how MathSwe
open-source standards are rigorous and transparent. Tools can use information,
like repository tags, where microservices are versioned, to infer the privacy
and cookie policies at a given time. The process is then cohesive[^3]
and efficient[^4].

[^3]: The repository `legal` is only for legal microservices

[^4]: It's neither necessary nor wanted to store redundant data to comply;
    tracking the version of the privacy/cookie policies should work well, so the
    system is efficient without storing any massive boilerplate

Regarding "Storing consent records," Finsweet says, "Most websites do not have
this level of compliance. However, storing consent records is mandatory for some
countries under GDPR." [7]. Therefore, that's what I mention by
**advanced compliance**, and this is another point to celebrate in this release
🎉.

The Cookie Consent service does its best to store cookie consent records to
demonstrate user preferences with rich proofs that can determine any user
consent. Its functionality gets extended by other tools like the banner and more
advanced tools that MathSwe can create in the future to keep providing the
service with rigor.

## Validating the Design of Consent Records

Website operators must store complete and accurate information to prove user
consent, but it also must be minimized to avoid collecting unnecessary data.
It's then crucial to verify the Cookie Consent design on the details of the
information it collects.

### References on Cookie Compliance Services

I took references from companies like CookieYes and others that provide
dedicated support for cookie compliance so I could make appropriate design
decisions.

First, we have CookieYes:

> CookieYes is an all-in-one cookie consent management platform for businesses.
> We empower millions of websites around the world to comply with data privacy
> laws with our dynamic products and outstanding support.
>
> Source: About CookieYes \| CookieYes [8] (under fair use)

We also have Finsweet Cookie Consent:

> Incredibly powerful cookie consent plugin built specifically for Webflow
>
> Create a custom cookie consent UI that complies with GDPR law
>
> Source: Finsweet cookie consent for webflow [9] (under fair use)

I'm taking many other references under the hood, but the above are enough for
this validation.

Now, I can validate the records applied by the Cookie Consent `v0.1.0`
against the documentation of the mentioned sources.

### Validating the Data Stored in Consent Records

The Cookie Consent `v0.1.0` service stores
[the `CookieConsent` record](#requesting-a-consent) as compliance proof to
show (if asked) whether there was user consent at a particular moment with other
details that enrich the record with accurate information.

The following table compares the information CookieYes stores as consent
proof [5] with the MathSwe Cookie Consent `v0.1.0` service implementation.

| CookieYes                    | MathSwe Cookie Consent v0.1.0                                                                                                                                      |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Consented domain             | It stores the `Domain` where the request <br/>comes from (MathSwe origins only). Consent is valid for all subdomains, explicitly stated in the cookie banner side. |
| Consent date                 | It stores the `created_at` field with the UTC date and time when the service applies the consent.                                                                  |
| Consent ID                   | It stores the `id` field, which is a unique `String` securely generated for each consent request.                                                                  |
| Location                     | It stores the `Geolocation` of the client request.                                                                                                                 |
| Anonymized IP address        | It implements and stores the `AnonymousIpv4` with last-digit anonymization. Handling the full IP is avoided in the process as much as possible.                    |
| Consent status               | It can be inferred with the "category-wise status."                                                                                                                |
| Category-wise consent status | It stores the `CookieConsentPref` with consent by cookie categories. It does not collect granular consent by individual cookie though (probably unnecessary).      |

Several cookie compliance sources define the Anonymized IP address as:

> IP anonymizer is a tool or feature to anonymize IP addresses, i.e. to make
> them unidentifiable to protect user privacy. It usually sets the last octet of
> IP address to zero. E.g. 92.168.12.144 will be set to 92.168.12.0.
>
> Source: What is IP Anonymizer? \| CookieYes [6] (under fair use)

> The user's IP with the last digit replaced with a 0 to maintain anonymity.
>
> Source: For super cookie consent GDPR compliance \| Finsweet cookie consent
> for webflow [7] (under fair use)

Therefore, the Cookie Consent `v0.1.0` implementation of `AnonymousIpv4` is
correct for creating the consent records.

In addition, according to Finsweet, you should also store the browser user agent
and banner text [7]. The MathSwe service stores the `user_agent` value, and
regarding "banner text" or "information provided at the consent moment,"
that information can be inferred, [as explained above](#proof-of-consent).

Minor improvements like granular cookie-wise consent are neither a priority nor
seem important. If you notice, most websites don't allow you to opt-in cookie by
cookie, and *no user is losing time doing that anyway*. The existing
category-wise consent covers the needs well since MathSwe doesn't plan to use
cookies heavily anyway.

Some improvements can be addressed later, like data minimization in the
geolocation stored.

Therefore, the MathSwe Cookie Consent `v0.1.0` service, compared to dedicated
cookie compliance providers, adheres to robust standards regarding cookie legal
requirements for advanced and secure compliance.

## Initial Release of the MathSwe Cookie Consent Service

MathSwe is thoroughly developing cookie compliance to support using cookie
technology in their websites and web apps when necessary. Even though MathSwe
plans cookie usage to be minimal, it's strategic to implement advanced
compliance with the most demanding regulations like the GDPR to be future-proof
and scale operations smoothly.

MathSwe will keep demonstrating rigor and transparency, not only for
mathematical software itself but for anything relating to business and legal
operations.

The Cookie Consent microservice is available, documented, and validated against
remarkable cookie compliance providers like CookieYes. To enhance its informed
designs, major sources like the GDPR itself were also reviewed.

There are some minor improvements spotted while reviewing this version to
consider for the next Cookie Consent release to keep providing a quality service
with rigor.

The Cookie Consent service belongs to the `mathswe/legal` repository, which will
contain all the MathSwe legal developments. Therefore, the cookie and privacy
policies will also be redacted and served from this legal space.

The purpose of the Cookie Consent service is to apply user consent from allowed
MathSwe origins so it can store consent records. Consent records or proofs serve
as legal documents that can help demonstrate the process of obtaining user
consent.

The ongoing cookie banner development can progress to finish it now that the
server side is available for MathSwe origins, such as mathswe.com,
math.software, and mathsoftware.engineer. The coming front-end integration to
the Cookie Consent service will accelerate the last developments to finish
implementing the cookie compliance process.

## References

[1] [#11 \| Art. 4 GDPR – Definitions](https://gdpr-info.eu/art-4-gdpr/).
(2018, March 29). General Data Protection Regulation (GDPR).

[2] [#1 \| Art. 7 GDPR – Conditions for consent](https://gdpr-info.eu/art-7-gdpr/).
(2018, March 28). General Data Protection Regulation (GDPR).

[3] [#3 \| Art. 7 GDPR – Conditions for consent](https://gdpr-info.eu/art-7-gdpr/).
(2018, March 28). General Data Protection Regulation (GDPR).

[4] Wolford, B. (2023, September 14).
[What is GDPR, the EU’s new data protection law?](https://gdpr.eu/what-is-gdpr/)
GDPR.eu.

[5] M. (2024, March 14).
[What information does Proof of Consent hold? \| Proof of consent](https://www.cookieyes.com/documentation/proof-of-consent/).
CookieYes.

[6] M. (2022, February 8).
[What is IP Anonymizer? \| Is Google Analytics GDPR compliant? [Checklist for compliance]](https://www.cookieyes.com/blog/google-analytics-gdpr/).
CookieYes.

[7] [For technical people \| For super cookie consent GDPR compliance \| Storing consent records \| Finsweet cookie consent for webflow](https://finsweet.com/cookie-consent#store-consents).

[8] CookieYes. (2024, March 12).
[About CookieYes](https://www.cookieyes.com/about/). CookieYes.

[9] [Finsweet cookie consent for webflow](https://finsweet.com/cookie-consent).
