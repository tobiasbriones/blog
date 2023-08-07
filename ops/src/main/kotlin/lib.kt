import arrow.core.Either
import arrow.core.Either.*

infix fun <X, Y> X.`---`(f: (X) -> Y): Y = f(this)

infix fun <X, Y, Z> ((Y) -> Z).`o`(g: (X) -> Y): (X) -> Z = { this(g(it)) }

infix fun <X, Y> ((X) -> Y).`$`(x: X): Y = this(x)

sealed class Tree<out T> {
    data class Node<out T>(val value: T, val children: List<Tree<T>>) :
            Tree<T>()

    data class Leaf<out T>(val value: T) : Tree<T>()
}

fun <X> Tree<X>.traverse(level: Int = 0, f: (X) -> Unit) {
    when (this) {
        is Tree.Node -> traverse(level, f)
        is Tree.Leaf -> f `$` value
    }
}

interface Value<X> {
    val value: X
}

interface Finer<X> : Value<X>


data class Coarser<X>(override val value: X) : Value<X> {

}

fun interface Predicate<X> {
    fun predicate(x: X): Boolean
}

data class NamedPredicate<X>(val predicate: Predicate<X>, val name: String) {
    fun show(x: X): String = name.replace("x", x.toString())
}
//
//data class RequireAllPredicate<X>(val predicates: List<NamedPredicate<X>>) {
//    fun eval(x: X): Either<String, X> = predicates
//            .filterNot { it.predicate.predicate(x) }
//            .map { it.show() }
//
//            .reduce({ a, b -> {a + " and " + b} })
//
//}


abstract class Refinement<X, T : Finer<X>>(
        private val unsafeNew: (X) -> T
) : Predicate<X> {
    operator fun invoke(value: X): Either<String, T> =
            when (predicate(value)) {
                true -> Right(unsafeNew(value))
                false -> Left("No")
            }
}

//
//@JvmInline
//value class Id private constructor(override val value: Int) : Finer<Int> {
//    companion object : Refinement<Int, Id>(::Id), RequirePredicate<Int> {
//        override val predicates: List<NamedPredicate<Int>>
//            get() = TODO("Not yet implemented")
//
//        override fun predicate(x: Int): Boolean = true
//    }
//}

sealed interface StringRefinement {
    object Trim : StringRefinement
    data class Length(val value: Int) : StringRefinement
    data class Range(val value: ClosedRange<Int>) : StringRefinement
}
