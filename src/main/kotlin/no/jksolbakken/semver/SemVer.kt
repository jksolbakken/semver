package no.jksolbakken.semver

import kotlin.math.min
import kotlin.math.sign

enum class VersionPart {
    MAJOR, MINOR, PATCH;
}

data class SemVer(val rawValue: String): Comparable<SemVer> {
    val major: UInt
    val minor: UInt
    val patch: UInt
    val prerelease: String?

    init {
        val periodIndices = buildList {
            for (idx in rawValue.indices) { if (rawValue[idx] == '.') add(idx) }
        }
        require(periodIndices.size >= 2)
        major = rawValue.substring(0, periodIndices[0]).toUInt()
        minor = rawValue.substring(periodIndices[0] + 1, periodIndices[1]).toUInt()
        val remainder = rawValue.substring(periodIndices[1] + 1)
        if (remainder.contains('-')) {
            patch = remainder.substringBefore('-').toUInt()
            prerelease = remainder.substringAfter('-').ifEmpty { null }
        } else {
            patch = remainder.toUInt()
            prerelease = null
        }
    }

    override fun compareTo(other: SemVer): Int {
        if (major != other.major) return major.compareTo(other.major)
        if (minor != other.minor) return minor.compareTo(other.minor)
        if (patch != other.patch) return patch.compareTo(other.patch)
        if (prerelease == null && other.prerelease == null) return 0
        if (prerelease == null) return 1
        if (other.prerelease == null) return -1

        val shortest = min(prerelease.length, other.prerelease.length)
        for (i in 0 until shortest) {
            val thisChar = prerelease[i]
            val otherChar = prerelease[i]
            if (thisChar == otherChar) continue
            val thisInt = thisChar.digitToIntOrNull()
            val otherInt = otherChar.digitToIntOrNull()
            if (thisInt != null && otherInt != null) return thisInt.compareTo(otherInt)
            if (thisInt != null) return 1
            if (otherInt != null) return -1
        }

        return sign((other.prerelease.length - prerelease.length).toDouble()).toInt()
    }

    fun inc(part: VersionPart):SemVer = when (part) {
        VersionPart.MAJOR -> SemVer("${major.inc()}.$minor.$patch")
        VersionPart.MINOR -> SemVer("$major.${minor.inc()}.$patch")
        VersionPart.PATCH -> SemVer("$major.$minor.${patch.inc()}")
    }

    override fun toString(): String {
        return "$major.$minor.$patch${if (prerelease != null) "-$prerelease" else ""}"
    }

}
