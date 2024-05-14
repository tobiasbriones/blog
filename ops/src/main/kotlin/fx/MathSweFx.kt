package fx

import arrow.core.Option
import fx.CoverCmd.PrCover
import fx.CoverCmd.ReleaseCover

fun getBg(): Option<String> = getFilePath("cover/bg.png")

fun getSubdomainLogo(repo: String): Option<String> =
    getFilePath("cover/repo/$repo.png")

fun getProfilePhoto(coverCmd: CoverCmd, org: String) = when (coverCmd) {
    PrCover -> getFilePath("cover/profile.jpeg")
    ReleaseCover -> getFilePath("cover/org/$org.png")
}

fun getBgColor(repo: String) = when(repo) {
    "mathswe.com" -> "#90A4AE"
    "legal" -> "#A1887F"
    "repsymo---mvp" -> "#4DD0E1"
    else -> "white"
}
