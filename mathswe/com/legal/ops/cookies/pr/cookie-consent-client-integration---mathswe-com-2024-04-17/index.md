<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Cookie Consent Client Integration | MathSwe Com (2024/04/17)

---

**Create env var for service cookie-consent and update deps**

Apr 14: PR [#13](https://github.com/mathswe/mathswe.com/pull/13) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It creates a new environment variable to connect to the Cookie Consent service,
so the cookie banner (and customization pane) apply the consent to store those
records. Further, it applies minor dependency updates to the project.

---

**Rename field from analytics to analytical (CookieConsent)**

Apr 15: PR [#14](https://github.com/mathswe/mathswe.com/pull/14) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It pairs with the last naming convention adopted by MathSwe Cookie Consent
v0.2.0. See more:
https://blog.mathsoftware.engineer/cookie-consent-v0-2-0---mathswe-legal-2024-04-09#request-and-response-integration.

---

**Add new component NotificationToast to @ui, @app**

Apr 15: PR [#15](https://github.com/mathswe/mathswe.com/pull/15) merged
into `dev <- mathswe/ui` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It integrates the "NotificationToast" component from the UI library to allow the
app to show brief update messages.

---

**Display cookie consent details**

Apr 16: PR [#16](https://github.com/mathswe/mathswe.com/pull/16) merged
into `dev <- mathswe/ui` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It shows the details from the "ClientCookieConsent" server response, such as
consent id, date, and geolocation.

---

**Tune cookie consent id sizing in banner and fix public icon path in
NotificationToast**

Apr 16: PR [#17](https://github.com/mathswe/mathswe.com/pull/17) merged
into `dev <- mathswe/ui` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It enhances the sizing of the cookie consent ID/"see more" detail in the Cookie
Banner component. It fixes the public directory path to load the Notification
Toast component icon in production.

---

**Fix inline padding when effective consent id is multiline**

Apr 16: PR [#18](https://github.com/mathswe/mathswe.com/pull/18) merged
into `dev <- mathswe/ui` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It removes bloated padding from inline elements to make the "Effective Consent
ID" line fit well when it doesn't fit in one line (a few cases sometimes).

---

**Integrate Cookie Consent service**

Apr 17: PR [#19](https://github.com/mathswe/mathswe.com/pull/19) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It calls the Cookie Consent endpoint to apply the consent and persists the
response with the cookie preferences along with newer features to support this
integration:

- Posts user consents to the Cookie Consent (v0.2.0) service.
- Stores the response as the confirmation that the system applied the consent.
- Displays the effective consent ID in the banner.
- Displays the effective consent details in the customization pane.
- Avoid automatically prompting the banner if redundant, or unwanted.
- Avoid posting redundant consents that already match the effective user
  preference.
- Refactors inconsistent definitions with improvements in the internal API.

The Cookie Service integration into the Cookie Banner and Customization pane
makes the user preferences effective. It enriches the client-side consent
behavior and transparency with the new details presented to the user about
their effective consent.

---
