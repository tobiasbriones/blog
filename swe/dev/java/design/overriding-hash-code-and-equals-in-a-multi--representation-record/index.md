<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# Overriding Hash Code and Equals in a Multi-Representation Record

## Multiple Email Representations

An email model consists of a **local** name followed by an **@** symbol and a
**domain** name. While this is pretty simple, they might also contain noise like
**dots** and **+** symbols for various purposes.

For example, `joedoe@place.com` is a canonical or base form of an email while
accepting the (infinitely many)
variants `joe.doe@place.com`, `joe.doe+aksfnsfs@place.com`, etc.

In programming terms, an email model can be a record with multiple
representations. That is different email values for readability or testing
purposes but corresponding to the same functionality. Therefore, emails can be
equal (or not) depending on your abstraction.

## Email Type

I created the `Email` record in Kotlin, thus using a `data class` and
overriding `toString`, `hashCode`, and `equals`.

`Email Implementation | Email.kt`

```kotlin
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
```

The function `normalizedLocal` will provide the base form of the `local`
field to remove the redundancy, so enabling **uniqueness** for that value.

The `equals` implementation matches the `other` generic object to an `Email`
type to check the rest of the field matching. This would be done via
`instanceof` (JDK16) in Java. The `hashCode` equals the normalized (main form
without repetition or canonical) email value.

Implementing `hashCode` is a key (pun intended) for discerning among `Email`
objects, like in a `Set` or `Map`.

The `hashCode` and `equals` methods have to be overridden in this case since
the `Email` type has many representations of the same model, so all the
redundant emails boil down to the main form and then compare for equality.

### Email Normalization Definitions

Important definitions are required for finishing the previous `Email`
implementation. They
regard [the definitions given first](#multiple-email-representations) for
dots (.) and plus (+) symbols.

`Email Normalization for the Local Component | Email.kt`

```kotlin
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
```

This way, `normalizedLocalDot` takes care of any dot by removing
it, `normalizedLocalPlus` filters out anything after any plus symbol,
and `normalizedLocal` composes both.
