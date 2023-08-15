package md

import `---`
import Entry
import arrow.core.*
import arrow.core.Either.Left
import name
import java.util.*

data class Dictionary(
    val uppercase: Set<String> = setOf(),
    val composed: Map<String, List<String>> = mapOf(),
    val acronym: Map<String, String> = mapOf(),
    val custom: Map<String, String> = mapOf(),
)

fun Dictionary.isUppercaseWord(word: String): Boolean = uppercase.contains(word.lowercase())

fun Dictionary.isComposedWord(word: String): Boolean = composed.contains(word.lowercase())

fun Dictionary.compositionOf(word: String): List<String> =
    composed[word.lowercase()] ?: listOf(word)

fun Dictionary.isAcronym(word: String): Boolean =
    acronym.contains(word.lowercase())

fun Dictionary.acronymOf(word: String): String =
    acronym[word.lowercase()] ?: word.uppercase()

fun Dictionary.isCustom(word: String): Boolean =
    custom.contains(word.lowercase())

fun Dictionary.split(input: String): List<String> {
    val matches = "\\b(${custom.keys.joinToString("|")})\\b"
        .toRegex()
        .findAll(input)
        .map { it.range }

    val tokens = mutableListOf<String>()
    var currentIndex = 0

    for (matchRange in matches) {
        if (matchRange.first > currentIndex) {
            tokens.addAll(input
                .substring(currentIndex, matchRange.first)
                .trim()
                .split(" ")
            )
        }
        tokens.add(input.substring(matchRange))
        currentIndex = matchRange.last + 1
    }

    if (currentIndex < input.length) {
        tokens.addAll(input.substring(currentIndex)
            .trim()
            .split(" ")
        )
    }

    return tokens
}

fun Dictionary.match(word: String): Option<DictionaryMatch> = when {
    isUppercaseWord(word) -> Uppercase(word).some()
    isComposedWord(word) -> Composed(compositionOf(word)).some()
    isAcronym(word) -> Acronym(acronymOf(word)).some()
    isCustom(word) -> custom[word].toOption().map(::Custom)
    else -> None
}

sealed interface DictionaryMatch

fun DictionaryMatch.toTitleWord(): TitleWord = when (this) {
    is Uppercase -> TitleWord(word.uppercase())
    is Composed -> composition
        .map(::StringWord)
        .joinToString("", transform = StringWord::capitalize) `---`
        ::TitleWord

    is Acronym -> TitleWord(acronym)
    is Custom -> TitleWord(custom)
}

@JvmInline
value class Uppercase(val word: String) : DictionaryMatch

@JvmInline
value class Composed(val composition: List<String>) : DictionaryMatch

@JvmInline
value class Acronym(val acronym: String) : DictionaryMatch

@JvmInline
value class Custom(val custom: String) : DictionaryMatch

sealed interface Word {
    val word: String
}

data class StringWord(override val word: String) : Word

fun StringWord.hasHyphen(): Boolean = word.contains("-")

fun StringWord.capitalize(): String = word.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
    else it.toString()
}

fun StringWord.hyphenToSpace(): String = word.replace("-", " ")

fun StringWord.matchDictionary(
    dic: Dictionary
): Either<TitleWord, StringWord> = dic
    .match(word)
    .map(DictionaryMatch::toTitleWord)
    .toEither { this }
    .swap()

fun StringWord.toTitleWord(dic: Dictionary): Left<TitleWord> = right()
    .flatMap { it.matchDictionary(dic) }
    .map(::majorWordOf)
    .flatMap { it.toTitleWord(dic) }
    .merge() `---`
    ::Left

data class IndexedStringWord(val index: Int, override val word: String) : Word

fun IndexedStringWord.isMinorWord(): Boolean =
    index > 0 && word in minorWords()

fun IndexedStringWord.matchMinorWord(): Either<TitleWord, StringWord> =
    if (isMinorWord()) TitleWord(word).left() else StringWord(word).right()

data class TitleWord(override val word: String) : Word

sealed interface MajorWord {
    val word: StringWord
}

fun majorWordOf(word: StringWord): MajorWord =
    if (word.hasHyphen()) HyphenedWord(word)
    else NormalWord(word)

fun MajorWord.toTitleWord(dic: Dictionary): Left<TitleWord> = when (this) {
    is HyphenedWord -> TitleWord(
        separate()
            .fromLowercaseToTitleCase(dic) // Recursive call
            .replace(" ", "-")
    )

    is NormalWord -> toTitleWord()
} `---` ::Left

@JvmInline
value class NormalWord(override val word: StringWord) : MajorWord

fun NormalWord.toTitleWord(): TitleWord = TitleWord(word.capitalize())

@JvmInline
value class HyphenedWord(override val word: StringWord) : MajorWord

fun HyphenedWord.separate(): String = word.hyphenToSpace()

fun Entry.toTitleCase(dic: Dictionary = Dictionary()): String =
    name()
        .replace(Regex("(?<![-_])-(?![-_])"), " ")
        .replace(Regex("(?<![-_])--(?![-_])"), "-")
        .replace(Regex("(?<![-_])-_-(?![-_])"), ": ")
        .replace(Regex("(?<![-_])--_--(?![-_])"), ", ")
        .replace(Regex("(?<![-_])---(?![-_])"), " | ")
        .replace(Regex("""(\d{4}\s\d{2}\s\d{2})(?=$)""")) {
            "(${it.groupValues[1].replace(" ", "/")})"
        }
        .fromLowercaseToTitleCase(dic)

fun String.fromLowercaseToTitleCase(
    dic: Dictionary = Dictionary()
): String = dic
    .split(this)
    .asSequence()
    .mapIndexed(::IndexedStringWord)
    .map { word ->
        word
            .right()
            .flatMap(IndexedStringWord::matchMinorWord)
            .flatMap { it.toTitleWord(dic) }
            .merge()
    }
    .joinToString(" ", transform = TitleWord::word)

private fun minorWords(): List<String> = listOf(
    "a",
    "an",
    "the",
    "and",
    "but",
    "or",
    "for",
    "nor",
    "on",
    "at",
    "to",
    "from",
    "by",
    "is",
    "with",
    "vs",
    "my",
    "our",
    "ours",
    "their",
    "theirs",
    "you",
    "yours",
    "mine",
    "his",
    "her",
    "hers",
    "it",
    "its",
    "of",
    "in",
    "by",
    "as",
    "off",
    "up",
    "over",
    "under",
    "into",
    "onto",
    "upon",
    "out",
    "down",
    "around",
    "through",
    "about",
)
