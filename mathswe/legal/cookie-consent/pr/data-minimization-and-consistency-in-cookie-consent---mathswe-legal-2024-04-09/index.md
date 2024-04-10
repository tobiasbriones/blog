<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Data Minimization and Consistency in Cookie Consent | MathSwe Legal (2024/04/09)

The Cookie Consent service is ready for version 0.2.0 with moderate private and
public design improvements by minimizing the geolocation data to store and
providing a more focused API.

---

**Tune API for data minimization and consistency**

Apr 10: PR [#5](https://github.com/mathswe/legal/pull/5) merged into `main
<- cookie-consent` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It applies data minimization to `Geolocation` and tunes the API request/response
schemes for posting consents.

- Removes many unnecessary fields from the `Geolocation` record that were placed
  as defaults in `v0.1.0`.

- Changes the `analytics` field to `analytical` in `CookieConsentPref` for
  vocabulary consistency.

- Defines the `ClientCookieConsent` to respond with more appropriate information
  to consume by the client. It prevents sending all the data back to the client,
  which is unnecessary.

---

The new updates to the Cookie Consent service changed some implementation by
choosing the geolocation fields to store since the last version `0.1.0` stored
all the fields by default. So, its design required a good data minimization to
avoid storing unnecessary or meaningless data.

It also had API breaking changes by adopting the `analitical` field name for
requests (and responses), and it introduced a better data type to respond to the
client, namely `ClientCookieConsent`, to send only meaningful data as a
response.
