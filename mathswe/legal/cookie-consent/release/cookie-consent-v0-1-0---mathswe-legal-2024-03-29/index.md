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

The client must store that response, probably in the cookies itself, to provide
the user with the ID generated for the applying consent and the updated cookie
banner preferences.

The current production deployment is available at
`https://mathswe-cookie-consent.tobiasbriones-dev.workers.dev`, where it'll
receive allowed MathSwe client requests via the method `POST`.

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

[7] [For technical people \| For super cookie consent GDPR compliance \| Storing consent records \| Finsweet cookie consent for webflow](https://finsweet.com/cookie-consent).
