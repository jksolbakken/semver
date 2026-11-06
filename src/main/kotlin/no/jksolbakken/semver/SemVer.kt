package no.jksolbakken.semver

class SemVer(rawValue: String): Comparable<SemVer> {
    val major: Int
    val minor: Int
    val patch: Int
    val prerelease: String?

    init {
        val parts = rawValue.split(".").also {
            require(it.size == 3 && it.all { it.isNotBlank() })
        }
        major = parts[0].toInt()
        minor = parts[1].toInt()
        if (parts[2].contains('-')) {
            val patchString = parts[2].substringBefore('-')
            patch = patchString.toInt()
            prerelease = parts[2].substringAfter('-').ifEmpty { null }
        } else {
            patch = parts[2].toInt()
            prerelease = null
        }
    }

    override fun compareTo(other: SemVer): Int {
        TODO("Not yet implemented")
    }

}
