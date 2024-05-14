package fx

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import java.util.regex.Pattern

fun extractOrgAndRepo(index: String): Pair<String, String>? {
    val pattern = Regex(
        """https://github\.com/([^/]+)/([^/]+)/(pull|releases)/"""
    )
    val matchResult = pattern.find(index)

    return matchResult?.let {
        val (org, repo, _) = it.destructured
        org to repo
    }.let {
        if (it == null) {
            println("Fail to infer Org and Repo from Article")
        }
        it
    }
}

fun inferRepoVersion(index: String, repoPath: String): Option<String> {
    val githubUrlPattern =
        "https://github.com/$repoPath/releases/tag/v(\\d+\\.\\d+\\.\\d+)"
    val pattern = Pattern.compile(githubUrlPattern)
    val matcher = pattern.matcher(index)

    return if (matcher.find()) Some(matcher.group(1)) else None
}
