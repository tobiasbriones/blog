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
