---
permalink: overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/src/main/kotlin/Email.kt.html
title: "overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/src/main/kotlin/Email.kt"
---

# Email.kt
```kotlin
// Copyright (c) 2024 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

/**
 * It removes the redundancy of an email local value containing dots (.).
 */
fun normalizedLocalDot(local: String): String =
    local.replace(".", "")

/**
 * It removes the redundancy of an email local value containing a plus (+)
 * symbol, by eliminating everything after the first + occurrence.
 */
fun normalizedLocalPlus(local: String): String =
    local.takeWhile { char -> char != '+' }

/**
 * It normalizes the email local value by removing any redundancy.
 */
fun normalizedLocal(local: String): String =
    normalizedLocalDot(normalizedLocalPlus(local))

/**
 * Represents an email address consisting of a local part and a domain name.
 * The local part may contain periods (".") or plus signs ("+"), where any
 * content after a plus sign ("+") and any additional periods (".") are
 * ignored for the purpose of determining email equality.
 */
data class Email(val local: String, val domain: String) {
    val normalized: String = "${normalizedLocal(local)}@$domain"

    override fun toString(): String = "$local@$domain"

    override fun hashCode(): Int = normalized.hashCode()

    override fun equals(other: Any?): Boolean = other is Email
      && domain == other.domain
      && normalizedLocal(local) == normalizedLocal(other.local)
}

/**
 * Defines a regex for valid email addresses, capturing the "local" and
 * "domain" groups.
 */
val emailPattern: Regex = """
        (?<local>^[a-zA-Z0-9._%+-]+)@(?<domain>[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$)
        """
    .trimIndent()
    .toRegex()

/**
 * Returns the number of unique email addresses.
 */
fun uniqueEmailsNum(emails: Array<String>): Int = emails
    .mapNotNull { emailPattern.find(it) }
    .map { match ->
        val (local, domain) = match.destructured
        Email(local, domain)
    }
    .toSet()
    .size

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/swe/dev/java/design/overriding-hash-code-and-equals-in-a-multi--representation-record/email---ep/src/main/kotlin/Email.kt" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>