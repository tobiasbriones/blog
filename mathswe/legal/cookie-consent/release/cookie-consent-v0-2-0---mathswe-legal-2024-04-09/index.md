<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Cookie Consent V0.2.0 | MathSwe Legal (2024/04/09)

The Cookie Consent service was previously initiated in "mathswe/legal" at v0.1.0
with a robust implementation, documentation, and validation. Version 0.2.0
removes unnecessary geolocation fields that v0.1.0 added by default, renames
the "analytical" field for consistency, and provides a new minimalistic body
response.

## Data Minimization and Focused API

Previously, in
[Cookie Consent v0.1.0 | MathSwe Legal (2024/03/29)](/cookie-consent-v0-1-0---mathswe-legal-2024-03-29),
MathSwe initiated the service to production with a great implementation,
documentation, and validation basis.

Version `0.2.0` delivered by the PR
[Data Minimization and Consistency in Cookie Consent | MathSwe Legal (2024/04/09)](/data-minimization-and-consistency-in-cookie-consent---mathswe-legal-2024-04-09)
removes unnecessary fields from the `Geolocation`
that were all added by default in `v0.1.0`, changes the `analytics` field of
`CookieConsentPref` to `analytical` to preserve naming consistency, and defines
a dedicated `ClientCookieConsent` response to provide the client with focused
data for their needs instead of the whole `CookieConsent` record from the
server.

Breaking changes involve minor structure improvements for
the `CookieConsentPref` request body (also part of the response) and the
response that becomes the new `ClientCookieConsent` body.

The release is available at GitHub
[Cookie Consent v0.2.0: Removes unnecessary geolocation data and provides a more focused API](https://github.com/mathswe/legal/releases/tag/v0.2.0).

## Request and Response Integration

The request body changed the field `analytics` to `analytical` to keep naming
consistency (mathematical, statistical, analytical, etc.), and the response
adopted the `ClientCookieConsent` body that avoids sending too much unnecessary
data back to the client. It affects the `post_consent` endpoint that applies
cookie consent from client banners.

The request body becomes:

`Request Body | CookieConsentPref`

```rust
pub struct CookieConsentPref {
    essential: bool,
    functional: bool,
    analytical: bool,
//  ^^^^^^^^^^ rename field from analytics to analytical 🆕 //
    targeting: bool,
}
```

Moreover, since `CookieConsentPref` is part of the response and banner model,
they must update the `analytical` field name to function.

The new response simplifies the original `CookieConsent` with all the record
data, which is unnecessary for the client.

`Simplified Response | ClientCookieConsent`

```rust
pub struct ClientCookieConsent { // integrate focused response 🆕 //
    id: String,
    pref: CookieConsentPref,
    created_at: DateTime<Utc>,
    geolocation: Geolocation,
}
```

Documentation available at
[Cookie Consent \| v0.2.0 \| GitHub](https://github.com/mathswe/legal/tree/v0.2.0/cookie-consent).

Version 0.2.0 demands minor updates on the client side for the request and
response processing, but they're still breaking changes from version 0.1.0.

## API Stabilization

With the enhanced request and response bodies in version 0.2.0, client banners
and customization panes can safely integrate the post-consent endpoint with a
more stable, future-proof API.
