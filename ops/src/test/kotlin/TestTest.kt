import kotlin.test.Test

class TestTest {

    fun <T> filter(p: (T) -> Boolean): (List<T>) -> List<T> =
        { list -> list.filter(p) }

    fun sum(list: List<Int>): Int = list.fold(0) { a, b -> a + b }

    fun log(msg: String) = println(msg)

    fun <T> show(value: T) = value.toString()

    @Test
    fun applicationOp() {
        val value = sum(filter { x: Int -> x % 2 == 0 }(listOf(1, 2, 3)))

        ::log `$` show(value)
    }
}
