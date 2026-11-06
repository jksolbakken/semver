package no.jksolbakken.semver

import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
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
    fun `All parts must be ints`() {
        listOf("1.2.a", "1.a.3", "a.2.3").forEach {
            assertThrows<IllegalArgumentException> { SemVer(it) }
        }
    }

    @Test
    fun `Correct parts are recognized`() {
        val semVer = SemVer("1.2.3")
        assertEquals(1, semVer.major)
        assertEquals(2, semVer.minor)
        assertEquals(3, semVer.patch)
    }

}