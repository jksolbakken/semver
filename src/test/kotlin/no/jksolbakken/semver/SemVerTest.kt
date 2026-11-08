package no.jksolbakken.semver

import kotlin.test.Test
import no.jksolbakken.semver.VersionPart.MAJOR
import no.jksolbakken.semver.VersionPart.MINOR
import no.jksolbakken.semver.VersionPart.PATCH
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class SemVerTest {

    @Test
    fun `Must consist of 3 dot separated parts`() {
        val rawStringGood = "1.2.3"
        assertDoesNotThrow { SemVer(rawStringGood) }
        val rawStringFewerParts = "1.2"
        assertThrows<IllegalArgumentException> { SemVer(rawStringFewerParts) }
        val rawStringMoreParts = "1.2.3.4"
        assertThrows<IllegalArgumentException> { SemVer(rawStringMoreParts) }
    }

    @Test
    fun `All parts must be positive ints`() {
        listOf("1.2.a", "1.a.3", "a.2.3", "1.2.-3").forEach {
            assertThrows<IllegalArgumentException> { SemVer(it) }
        }
    }

    @Test
    fun `Correct parts are recognized`() {
        val semVer = SemVer("1.2.3")
        assertEquals(1u, semVer.major)
        assertEquals(2u, semVer.minor)
        assertEquals(3u, semVer.patch)
    }

    @Test
    fun `Prerelease versions are recognized`() {
        val semVer = SemVer("1.2.3-whatever.yolo")
        assertEquals(1u, semVer.major)
        assertEquals(2u, semVer.minor)
        assertEquals(3u, semVer.patch)
        assertEquals("whatever.yolo", semVer.prerelease)
    }

    @Test
    fun `Sort by major, minor and patch`() {
        val unsorted = listOf(
            SemVer("0.0.1"), SemVer("0.0.2"), SemVer("0.0.3"),
            SemVer("1.0.0"), SemVer("2.0.0"), SemVer("3.0.0"),
            SemVer("1.1.0"), SemVer("1.2.0"), SemVer("1.3.0"),
            SemVer("1.0.1"), SemVer("1.0.2"), SemVer("1.0.3")
        )
        val expected = listOf(
            SemVer("0.0.1"), SemVer("0.0.2"), SemVer("0.0.3"),
            SemVer("1.0.0"), SemVer("1.0.1"), SemVer("1.0.2"),
            SemVer("1.0.3"), SemVer("1.1.0"), SemVer("1.2.0"),
            SemVer("1.3.0"), SemVer("2.0.0"), SemVer("3.0.0")
        )
        assertEquals(expected, unsorted.sorted())
    }

    @Test
    fun `A release outranks a prelease `() {
        val unsorted = listOf(SemVer("1.0.0"), SemVer("1.0.0-alpha"))
        val expected = listOf(SemVer("1.0.0-alpha"), SemVer("1.0.0"))
        assertEquals(expected, unsorted.sorted())
    }
    
    @Test
    fun `Fewer prelease identifiers outrank many identifiers`() {
        val unsorted = listOf(SemVer("1.0.0-alpha"), SemVer("1.0.0-alpha.1"))
        val expected = listOf(SemVer("1.0.0-alpha.1"), SemVer("1.0.0-alpha"))
        assertEquals(expected, unsorted.sorted())
    }

    @Test
    fun `Preleases by digits (if present)`() {
        val unsorted = listOf(
            SemVer("1.0.0-beta.2"), SemVer("1.0.0-beta.22"),
            SemVer("1.0.0-beta.1"), SemVer("1.0.0-beta.11"),
        )
        val expected = listOf(
            SemVer("1.0.0-beta.22"), SemVer("1.0.0-beta.11"),
            SemVer("1.0.0-beta.2"), SemVer("1.0.0-beta.1"),
        )
        assertEquals(expected, unsorted.sorted())
    }

    @Test
    fun `increment it`() {
        val semVer = SemVer("0.0.0")
        assertEquals(SemVer("1.0.0"), semVer.inc(MAJOR))
        assertEquals(SemVer("0.1.0"), semVer.inc(MINOR))
        assertEquals(SemVer("0.0.1"), semVer.inc(PATCH))
    }

    @Test
    fun `toString() with no prerelease`() {
        val semVer = SemVer("1.0.0")
        assertEquals("1.0.0", semVer.toString())
    }

    @Test
    fun `toString() with prerelease`() {
        val semVer = SemVer("1.0.0-yolo")
        assertEquals("1.0.0-yolo", semVer.toString())
    }

}

