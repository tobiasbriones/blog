//fun <T> and(p1: Predicate<T>, p2: Predicate<T>): Predicate<T> = { value ->
//    p1(value) && p2(value)
//}
//
//fun <T> or(p1: Predicate<T>, p2: Predicate<T>): Predicate<T> = { value ->
//    p1(value) || p2(value)
//}
//
//fun <T> not(p: Predicate<T>): Predicate<T> = { value ->
//    !p(value)
//}
//
//// Higher-order function to wrap a predicate with logging behavior
//fun <T> log(predicate: Predicate<T>, message: String): Predicate<T> = { value ->
//    val result = predicate(value)
//    println("$message: $result")
//    result
//}
//
//// Higher-order function to wrap a predicate with string conversion behavior
//fun <T> stringify(predicate: Predicate<T>): Predicate<T> = { value ->
//    val result = predicate(value)
//    println("Predicate: ${predicate.toRawString()} = $result")
//    result
//}
//
//// Extension function to get a raw string representation of a predicate
//fun <T> Predicate<T>.toRawString(): String {
//    return toString().substringAfter(" ")
//}
//
//fun main() {
//    val isEven: Predicate<Int> = { number ->
//        number % 2 == 0
//    }
//
//    val isPositive: Predicate<Int> = { number ->
//        number > 0
//    }
//
//    val isEvenAndPositive = and(isEven, isPositive)
//    val isOddOrNegative = or(not(isEven), not(isPositive))
//
//    val loggedPredicate = log(isEvenAndPositive, "isEvenAndPositive")
//    val stringifiedPredicate = stringify(isOddOrNegative)
//
//    println(loggedPredicate(4))
//    println(stringifiedPredicate(3))
//}


//fun main() {
//    val root = Entry(Path.of(".."))
//    val entries = entries(root)
//
////    val titles = Tree.Node(
////        Title.Heading("Title" `---` title, 1),
////        listOf(
////            Tree.Node(
////                Title.Heading("Section", 2),
////                listOf(
////                    Tree.Node(
////                        Title.Heading("Subsection", 3),
////                        listOf(
////                            Tree.Leaf(
////                                Title.Heading("Subsubsection", 4)
////                            ),
////                            Tree.Leaf(
////                                Title.Caption("", 4)
////                            )
////                        )
////                    )
////                )
////            )
////        )
////    )
//
////    titles.traverse {
////        when (it) {
////            is Title.Heading -> println(" ".repeat((it.heading - 1) * 4))
////            is Title.Caption -> println(" ".repeat((it.heading - 1) * 4))
////        }
////    }
//
//
//}
//


//data class Product(val id: Id, val name: String) {
//    companion object {
//        operator fun invoke(id: Int, name: String): Option<Product> {
//
//        }
//    }
//}
//
//fun program() {
//    val id = 4
//    val name = "Product A"
//
//    val product = Product(id, name)
//    show(product)
//}
//
//fun show(product: Option<Product>) {
//    product.onSome { product: Product -> println(product) }
//}
//
//fun id(value: Int): Option<Id> {
//    val fold = Id(4)
//        .onLeft(::handle)
//        .getOrNone()
//
//}


sealed interface Title {
    @JvmInline
    value class Title private constructor(val value: String) {
        init {
            require(value.trim() == value) { "Value is not trimmed" }
            require(value.length in 1..25) { "Value not in rage [1, 25]" }
        }

        companion object {
            operator fun invoke(value: String?): Title {
                requireNotNull(value) { "Value must not be null" }
                return Title(value)
            }
        }
    }

    data class Heading(val value: Title, val heading: Int)

    data class Caption(val value: Title, val heading: Int)
}