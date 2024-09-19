package fx

import arrow.core.*
import fx.CoverCmd.PrCover
import fx.CoverCmd.ReleaseCover

fun getBg(): Option<String> = getFilePath("cover/bg.png")

fun getSubdomainLogo(repo: String, subheading: Option<String>): Option<String> = subheading
    .map {
        it
            .lowercase()
            .removePrefix("\\\"")
            .removeSuffix("\\\"")
    }
    .flatMap {
        getFilePath("cover/repo/$repo/${it}.png")
    }
    .recover {
        getFilePath("cover/repo/$repo.png").bind()
    }

fun getProfilePhoto(coverCmd: CoverCmd, org: String) = when (coverCmd) {
    PrCover -> getFilePath("cover/profile.jpeg")
    ReleaseCover -> getFilePath("cover/org/$org.png")
}

fun getBgColor(repo: String) = when(repo) {
    "mathswe.com" -> "#90A4AE"
    "legal" -> "#A1887F"
    "repsymo---mvp" -> "#4DD0E1"
    "mathswe-ops---mvp" -> "#B0BEC5"
    "services" -> "#90A4AE"
    else -> "white"
}
