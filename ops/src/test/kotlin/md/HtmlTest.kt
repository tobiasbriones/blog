package md

import arrow.core.None
import arrow.core.Some
import org.junit.jupiter.api.Test

import java.io.File
import java.nio.file.Files

class HtmlTest {
    private val index: Index = Index(
        Markdown(
            Files.readString(
                File(
                    javaClass.classLoader.getResource("fp-in-kotlin.md").file
                ).toPath()
            )
        )
    )

    @Test
    fun navHtml() {
        val navHtml = index.generateNavHtml()
        val expected = """
            <nav>
                <a href="/" class="home">
                    <span class="material-symbols-rounded">home</span>
                    <span>Blog</span>
                </a>
    
                <div class="article">
                    <a href="#" class="title">FP in Kotlin</a>

                    <ul>
                        <li>
                            <a href="#pipe-operator">Pipe Operator</a>
                            <ul>
                                <li>
                                    <a href="#defining-a-pipe-operator">Defining a Pipe Operator</a>
                                    <ul>
                                        <li>
                                            <a href="#finding-a-suitable-symbol">Finding a Suitable Symbol</a>
                                        </li>
                                        <li>
                                            <a href="#language-features">Language Features</a>
                                        </li>
                                        <li>
                                            <a href="#operator-definition">Operator Definition</a>
                                        </li>
                                        <li>
                                            <a href="#usage-example">Usage Example</a>
                                        </li>
                                        <li>
                                            <a href="#functional-language-design">Functional Language Design</a>
                                        </li>
                                        <li>
                                            <a href="#custom-pipe-in-kotlin">Custom Pipe in Kotlin</a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="#options-for-a-pipe-operator-in-kotlin">Options for a Pipe Operator in Kotlin</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#designing-functional-languages-in-kotlin">Designing Functional Languages in Kotlin</a>
                        </li>
                    </ul>
                </div>
            </nav>
        """.trimIndent()

        println(navHtml)
        assert(navHtml == expected)
    }
}