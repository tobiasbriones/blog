// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

infix fun <X, Y> X.`---`(f: (X) -> Y): Y = f(this)

data class Article(val title: Title, val content: String)

@JvmInline
value class Title(val value: String) {
    override fun toString(): String = value
}

val title: (String) -> Title = { Title(it) }

fun main() {
    val inputTitle = "FP in Kotlin: Defining a Pipe   Operator  "
    val inputContent = "Lorem ipsum dolor sit amet..."

    val clean: (String) -> String =
        { it.trim().replace("\\s+".toRegex(), " ") }
    val uppercase: (String) -> String = { it.uppercase() }
    val markdownTitle: (String) -> String = { "# $it" }
    val formatTitle: (String) -> String =
        { it `---` clean `---` uppercase `---` markdownTitle }

    print(
        Article(
            inputTitle `---` formatTitle `---` title,
            inputContent
        )
    )
}
