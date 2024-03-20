import arrow.core.Either.Right
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CmdTest {
    @Test
    fun parsesSimpleCmd() {
        val create = newOp("create")
        val deploy = newOp("deploy")

        assertEquals(create, Right(Cmd.Create))
        assertEquals(deploy, Right(Cmd.Deploy))
    }

    @Test
    fun parsesMultipleWordCmd() {
        val addPr = newOp("add-pr")

        assertEquals(addPr, Right(Cmd.AddPr))
    }
}