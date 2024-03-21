import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Base(
    val ref: String
)

@Serializable
data class Repo(
    val full_name: String
)

@Serializable
data class Head(
    val ref: String,
    val repo: Repo,
)

@Serializable
data class User(
    val login: String
)

@Serializable
data class PullRequest(
    val url: String,
    val html_url: String,
    val id: Int,
    val number: Int,
    val state: String,
    val title: String,
    val body: String?,
    val created_at: String,
    val updated_at: String,
    val closed_at: String,
    val merged_at: String,
    val base: Base,
    val head: Head,
    val user: User,
)

suspend fun fetchClosedPullRequests(
    path: String,
    from: Int,
): Either<HttpStatusCode, List<PullRequest>> {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val url = "https://api.github.com/repos/$path/pulls"
    val response = client.get(url) {
        parameter("state", "closed")
        parameter("number", ">$from")
    }

    return when (response.status) {
        HttpStatusCode.OK -> response
            .body<List<PullRequest>>()
            .filter { it.number >= from }
            .right()

        else -> response.status.left()
    }
}

fun PullRequest.titleMd(citation: Int) =
    "**${title}** | `${base.ref} <- ${head.ref}` PR [#$number]($html_url) [$citation]"

fun PullRequest.referenceLinkTitle() =
    "$title by ${user.login} · Pull Request #$number · ${head.repo.full_name}"

fun PullRequest.referenceItemMd(idx: Int) = """
    |[$idx] [${referenceLinkTitle()}]($html_url).
    |GitHub.
""".trimMargin("|")
