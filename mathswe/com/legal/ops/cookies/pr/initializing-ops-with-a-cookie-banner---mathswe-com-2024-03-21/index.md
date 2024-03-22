<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Initializing Ops with a Cookie Banner | MathSwe Com (2024/03/21)

Almost a month of development was applied to the new MathSwe website to open
legal operations providing an international compliance of cookie laws to recent
projects like Math Software. Results encompass a complete cookie banner, Google
Analytics 4 (with consent mode v2) implementation, ongoing redaction of the
cookie privacy, and further compliance requirements.

---

**Initiate web app operations**

Feb 5: PR [#1](https://github.com/mathswe/mathswe.com/pull/1) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It sets up the web app for its initial development.

---

**Add component Table**

Feb 11: PR [#2](https://github.com/mathswe/mathswe.com/pull/2) merged
into `dev <- mathswe/ui` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It helps render minimalistic responsive tables.

I extracted the table component from the current MSW Engineer styles which
helped render the cookie policy tables with coherent cross-platform UX/UI and
responsive design.

---

**Implement component CookieBanner UI**

Feb 20: PR [#3](https://github.com/mathswe/mathswe.com/pull/3) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the UI implementation for the cookie banner that will allow the user to
select cookie preferences immediately.

---

**Enhance UI of component CookieBanner with styles and anims**

Feb 21: PR [#4](https://github.com/mathswe/mathswe.com/pull/4) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It enhances and fixes some further styles and adds the showing animation.

---

**Add dep react-cookies**

Feb 21: PR [#5](https://github.com/mathswe/mathswe.com/pull/5) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds app cookie support.

---

**Fix src path TS configs**

Feb 21: PR [#6](https://github.com/mathswe/mathswe.com/pull/6) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The build command failed because of the tsconfig.json typos in the project
paths.

---

**Implement state persistence via cookies in CookieBanner.tsx**

Feb 22: PR [#7](https://github.com/mathswe/mathswe.com/pull/7) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It enables cookie usage to start storing user's content via the `CookieBanner`
component.

---

**Enable env vars and set cookie consent to all subdomains**

Feb 22: PR [#8](https://github.com/mathswe/mathswe.com/pull/8) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It supports the `production` and `staging` environments with public variables
like domain names, as well as setting the cookie consent domain to all
subdomains.

Notice that valid cross-site cookies must be set only by the APEX domain. Set
the cookie consent from the main domain only, and it will apply to all its
subdomains.

---

**Implement rigorous Google Analytics app API**

Feb 27: PR [#9](https://github.com/mathswe/mathswe.com/pull/9) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

This implementation:

- Uses env variables to set up GA as per environment requirements.
- Follow the Google consent mode (v2).
- Is type-safe.
- Has important documentation.
- Pass the consent mode tests via Google Tag Assistance and is reactive to
  cookie changes.
- Disallows arbitrary updates (side effects) to the cookie banner check buttons
  while showing.

The remaining task is implementing this via SSR in the head tag (removing the
react-ga lib).

I had to test and reverse-engineer some libs like `@types/gtag.js`,
and `react-ga4` various times 😣 to get to the correct implementation. The
correct Google Analytics implementation was complicated and still has some
non-functional requirements pending, like loading the script with SSR in the
head (when Next.js is available for the app) instead of using the `ReactGA`
lib.

![](images/gtag-assistance-test.png)

I don't plan to use other modes than `analytics_storage`, but I configured all
of them according to the Google documentation and cookie categories, so they
will scale trivially if ever needed. For example, if you only give
`analytics` consent, then only the `analytics_storage` state will be `Granted`.

The GA implementation was tested with the Google Tag Assistance tool, and worked
correctly when the consent was updated from the banner in mathswe.com.

---

**Fix build with missing dep @types/gtag.js**

Feb 27: PR [#10](https://github.com/mathswe/mathswe.com/pull/10) merged
into `dev <- mathswe/ops` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the `@types/gtag.js` dev dependency to support the required type for
GTag.

---

**Add extra personalization with more complete cookie consent and better banner
styles**

Mar 2: PR [#11](https://github.com/mathswe/mathswe.com/pull/11) merged
into `dev <- legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds the other common categories of cookies: functional, and targeting.

It sets the other Google consent parameters from the Google documentation. They
may not be required, but the system will set them accordingly.

It allows quality personalization by showing the underlying domain name
requesting consent in the banner.

Personalization is even more complying now by enabling the "Essential Only" CTA
button in the cookie banner, besides the existing "Accept All" CTA button.

It improves some styles for better readability in the cookie banner.

![](images/cookie-banner.png)

The cookie banner component is done with these changes and complies with cookie
laws —as you can see. The next additions are being oriented to the preference
dialog (the expanded version) and the Rust microservice (in MathSwe Lambda) to
store the consents (required to comply with cookie laws, too).

---

The 11 PRs merged from Feb 5 to Mar 2 of 2024 addressed the initialization of
the mathswe.com repository with a React app following the other recent
developments of the math.software MVP. The purpose of initializing mathswe.com
is to open the `legal/cookie-privacy` path and provide a branded cookie banner
and preference system complying with international cookie law regulations.

The cookie banner was iterated from a basic UI with initial options. Animations
were implemented to show/hide it (that will standardize the UX/UI of other
config components), persistence in cookies, and enhancements like customizing
the underlying domain to let the user know where exactly their consent applies.

Environment variables were enabled, so values like `VITE_DOMAIN_NAME`
and `VITE_ANALYTICS_GTAG_ID` which open access to the staging environment
at [staging.mathswe.com](https://staging.mathswe.com). Despite the rumor that
cross-site cookies can only be applied from the APEX domain, I tried to set
them (all domains and subdomains `.mathswe.com`) from the staging subdomain, and
it worked for both staging and the APEX domain.

The Google Consent mode v2 was implemented according to the docs and tested
further with the Google Tag Assistance. So, GA is ready to run safely with the
integration to the cookie banner preferences.

The mathswe.com project initiated, and its legal domain is under development,
complying with cookie regulations. Legal developments include a complete cookie
banner aligned to current MathSwe UI/UX standards, Google Analytics 4 with
Google Consent Mode v2 implementation, and redaction of the cookie privacy.
Further, the staging platform for mathswe.com is available for testing before
merging to `main` (prod).

I have to say, **there are a bunch more of developments in `legal` that didn't
merge to `dev` yet** (19 commits) from Feb 29.

These developments will enable other recent projects like math.software MVP to
resume operations, but still, there's much more burden to complete, like the
cookie (expanded) preference, updating the cookie policy, and initialization of
microservices with a database included.
