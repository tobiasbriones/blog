package md

import org.junit.jupiter.api.Test
import wrapText
import kotlin.test.assertEquals

class WrapKtTest {
    @Test
    fun wrapsPrBodyWithList() {
        val body =
            "It defines the initial structure that the source code is going to leverage with the following paths:\\r\\n\\r\\n- `sys`: TS functionalities.\\r\\n- `math`: Mathematical code (in-house/fast prototypes).\\r\\n- `ui`: Standalone React components.\\r\\n- `app`: Web app integration.\\r\\n"

        assertEquals(
            """
                |It defines the initial structure that the source code is going to leverage with
                |the following paths:
                |
                |- `sys`: TS functionalities.
                |- `math`: Mathematical code (in-house/fast prototypes).
                |- `ui`: Standalone React components.
                |- `app`: Web app integration.
                |
            """.trimMargin("|"),
            body.wrapText()
        )
    }
}