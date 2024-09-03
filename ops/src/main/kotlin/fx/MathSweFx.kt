package fx

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.recover
import fx.CoverCmd.PrCover
import fx.CoverCmd.ReleaseCover

fun getBg(): Option<String> = getFilePath("cover/bg.png")

fun getSubdomainLogo(repo: String, subheading: Option<String>): Option<String> = subheading
    .map {
        it
            .lowercase()
            .removePrefix("\"")
            .removeSuffix("\"")
    }
    .map {
        getFilePath("cover/repo/$repo/${it}.png")
    }
    .getOrElse {
        getFilePath("cover/repo/$repo.png")
    }

fun getProfilePhoto(coverCmd: CoverCmd, org: String) = when (coverCmd) {
    PrCover -> getFilePath("cover/profile.jpeg")
    ReleaseCover -> getFilePath("cover/org/$org.png")
}

fun getBgColor(repo: String) = when(repo) {
    "mathswe.com" -> "#90A4AE"
    "legal" -> "#A1887F"
    "repsymo---mvp" -> "#4DD0E1"
    "mathswe-ops---mvp" -> "B0BEC5"
    else -> "white"
}
