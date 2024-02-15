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

### Measuring Email Uniqueness Challenge

This problem gives you a list of strings supposed to be email addresses
with [the dot and plus constraints](#multiple-email-representations) defined
before. You have to return the number of unique emails in the list.

These kinds of toy (interview) problems don't care much about realistic
requirements. For example, you can pass the tests even if the email is invalid,
but the count "passes." They're probably also full of imperative approaches that
are hard to maintain with real conditions.

By working out the subproblems declaratively with mathematical definitions, you
will scale a well-defined domain supporting any kind of requirements.

First, I needed to define a language to match any valid email address.

`Email Regex Pattern | Email.kt`

```kotlin
/**
 * Defines a regex for valid email addresses, capturing the "local" and
 * "domain" groups.
 */
val emailPattern: Regex = """
    (?<local>^[a-zA-Z0-9._%+-]+)@(?<domain>[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$)
    """
    .trimIndent()
    .toRegex()
```

The regex captures two groups, `local` and `domain`, for matching expressions.
The set of accepted inputs is **the language**. Of course, the language just
defined is that of all valid emails we required above.

Notice the email language defined by the regex might be actually integrated into
the `Email` type for building a DSL, for example, by using refinements.

Finally, checking redundancy can boil down to counting a `Set`.

`Counting Unique Emails | Email.kt`

```kotlin
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

The generic email list is mapped to matching expressions, that is, strings that
belong to the email language given by the regex. The two groups are destructured
to map the `String` to the domain type `Email` and then converted to a `Set` to
remove redundant values, providing the required count. This works
because `Email` already has the implementation for equality.

#### Testing Email Values

I generated and reviewed a bunch of tests to check my code.

`Tests for the Uniqueness Challange | class EmailTest | EmailTest.kt`

```kotlin
// ... //
@Test
fun `test uniqueEmailsNum with duplicate emails`() {
    val emails = arrayOf(
        "test.email@gmail.com",
        "test.email@gmail.com",
        "test.email@outlook.com"
    )
    assertEquals(2, uniqueEmailsNum(emails))
}

// ... //

@Test
fun `test uniqueEmailsNum with emails containing plus symbol and dots`() {
    val emails = arrayOf(
        "test.email+spam@gmail.com",
        "test.email+spam.news@yahoo.com",
        "testemail+spam.news1@yahoo.com",
        "test.email+update@outlook.com"
    )
    assertEquals(3, uniqueEmailsNum(emails))
}
```

With the given test suite, the `uniqueEmailsNum` function can be checked for
many cases.
