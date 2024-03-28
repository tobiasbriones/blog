<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# New Cookie Consent Microservice Ready | MathSwe Legal (2024/03/28)

---

**Publish cookie-consent v0.1.0**

Mar 28: PR [#1](https://github.com/mathswe/legal/pull/1) merged
into `main <- cookie-consent`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It creates and thoroughly implements the `cookie-consent` microservice that
allows the storage of consent records.

It implements the following general plan:

- Create Rust `lib` project with WASM.
- Initialize Cloudflare Worker.
- Integrate Cloudflare KV.
- Support staging environment.
- Add `Geolocation` support.
- Add `AnonymousIpv4` support.
- Define domain in mod `consent`.
- Implement method `POST` at `/` for registering consents.
- Implement CORS with allowed origins.
- Update documentation in `README.md`.

The service was tested locally, from `staging.mathswe.com` and
`staging.math.software`, which proves correct working.

It provides the API established in the `README.md` file in the root directory of
the `cookie-consent` service. It is ready to support processing the consent
records in production for storage from the cookie banners.

---

**Move cookie-consent to root dir**

Mar 28: PR [#2](https://github.com/mathswe/legal/pull/2) merged
into `main <- cookie-consent`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The repository was renamed from "lambda" to "legal" to include only first-class
legal microservices instead of all microservices.


---

**Add README.md to root project**

Mar 28: PR [#3](https://github.com/mathswe/legal/pull/3) merged
into `main <- legal/docs` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

It adds an updated `README.md` to the project's root.


---

**Update copyright header with new repo link**

Mar 28: PR [#4](https://github.com/mathswe/legal/pull/4) merged
into `main <- legal/legal` by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The repo changed from "mathswe/lambda" to "mathswe/legal."

---
