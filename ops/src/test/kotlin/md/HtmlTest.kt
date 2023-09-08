package md

import TestResources
import html.toHtmlString
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.assertEquals

class HtmlTest {
    private val index: Index = Index(
        Markdown(
            TestResources
                .pathOf(Path.of("fp-in-kotlin", "index.md"))
                .readText()
        )
    )

    @Test
    fun navHtml() {
        val navHtml = index.generateNav("Blog").toHtmlString()
        val expected = """
            <nav aria-label="Navigation">
              <a class="home" href="/">
                <span class="material-symbols-rounded">
                  home
                </span>
                <span>
                  Blog
                </span>
              </a>
            </nav>
        """.trimIndent()

        assertEquals(expected, navHtml)
    }

    @Test
    fun tocHtml() {
        val tocHtml = index.generateToC().toHtmlString()
        val expected = """
              <nav class="toc user-select-none" aria-label="Table of Contents">
                <a class="title" href="#">
                  FP in Kotlin
                </a>
                <ul>
                  <li>
                    <a href="#pipe-operator">
                      Pipe Operator
                    </a>
                    <ul>
                      <li>
                        <a href="#defining-a-pipe-operator">
                          Defining a Pipe Operator
                        </a>
                        <ul>
                          <li>
                            <a href="#finding-a-suitable-symbol">
                              Finding a Suitable Symbol
                            </a>
                          </li>
                          <li>
                            <a href="#language-features">
                              Language Features
                            </a>
                          </li>
                          <li>
                            <a href="#operator-definition">
                              Operator Definition
                            </a>
                          </li>
                          <li>
                            <a href="#usage-example">
                              Usage Example
                            </a>
                          </li>
                          <li>
                            <a href="#functional-language-design">
                              Functional Language Design
                            </a>
                          </li>
                          <li>
                            <a href="#custom-pipe-in-kotlin">
                              Custom Pipe in Kotlin
                            </a>
                          </li>
                        </ul>
                      </li>
                      <li>
                        <a href="#options-for-a-pipe-operator-in-kotlin">
                          Options for a Pipe Operator in Kotlin
                        </a>
                      </li>
                    </ul>
                  </li>
                  <li>
                    <a href="#designing-functional-languages-in-kotlin">
                      Designing Functional Languages in Kotlin
                    </a>
                  </li>
                </ul>
              </nav>
        """.trimIndent()

        assertEquals(expected, tocHtml)
    }
}