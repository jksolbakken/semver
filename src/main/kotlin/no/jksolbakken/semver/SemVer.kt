package no.jksolbakken.semver

data class SemVer(val rawValue: String): Comparable<SemVer> {
    val major: Int
    val minor: Int
    val patch: Int
    val prerelease: String?

    init {
        val periodIndices = buildList {
            for (idx in rawValue.indices) { if (rawValue[idx] == '.') add(idx) }
        }
        require(periodIndices.size >= 2) {
            "Invalid SemVer '$rawValue'"
        }
        major = rawValue.substring(0, periodIndices[0]).toInt()
        minor = rawValue.substring(periodIndices[0] + 1, periodIndices[1]).toInt()
        val remainder = rawValue.substring(periodIndices[1] + 1)
        if (remainder.contains('-')) {
            val patchString = remainder.substringBefore('-')
            patch = patchString.toInt()
            prerelease = remainder.substringAfter('-').ifEmpty { null }
        } else {
            patch = remainder.toInt()
            prerelease = null
        }
    }

    override fun compareTo(other: SemVer): Int {
        if (major != other.major) return major.compareTo(other.major)
        if (minor != other.minor) return minor.compareTo(other.minor)
        if (patch != other.patch) return patch.compareTo(other.patch)
        return 0
    }

}
