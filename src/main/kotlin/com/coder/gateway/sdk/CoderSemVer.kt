package com.coder.gateway.sdk


class CoderSemVer(private val major: Long = 0, private val minor: Long = 0, private val patch: Long = 0) : Comparable<CoderSemVer> {

    init {
        require(major >= 0) { "Coder major version must be a positive number" }
        require(minor >= 0) { "Coder minor version must be a positive number" }
        require(patch >= 0) { "Coder minor version must be a positive number" }
    }

    fun isInClosedRange(start: CoderSemVer, endInclusive: CoderSemVer) = this in start..endInclusive
    fun isCompatibleWith(other: CoderSemVer): Boolean {
        // in the initial development phase minor changes when there are API incompatibilities
        if (this.major == 0L) {
            if (other.major > 0) return false
            return this.minor == other.minor
        }
        return this.major <= other.major
    }


    override fun toString(): String {
        return "CoderSemVer(major=$major, minor=$minor)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoderSemVer

        if (major != other.major) return false
        if (minor != other.minor) return false
        if (patch != other.patch) return false

        return true
    }

    override fun hashCode(): Int {
        var result = major.hashCode()
        result = 31 * result + minor.hashCode()
        result = 31 * result + patch.hashCode()
        return result
    }

    override fun compareTo(other: CoderSemVer): Int {
        if (major > other.major) return 1
        if (major < other.major) return -1
        if (minor > other.minor) return 1
        if (minor < other.minor) return -1
        if (patch > other.patch) return 1
        if (patch < other.patch) return -1

        return 0
    }

    companion object {
        private val pattern = """^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+([0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?$""".toRegex()

        @JvmStatic
        fun isValidVersion(semVer: String) = pattern.matchEntire(semVer.trimStart('v')) != null

        @JvmStatic
        fun parse(semVer: String): CoderSemVer {
            val matchResult = pattern.matchEntire(semVer.trimStart('v')) ?: throw IllegalArgumentException("$semVer could not be parsed")
            return CoderSemVer(
                if (matchResult.groupValues[1].isNotEmpty()) matchResult.groupValues[1].toLong() else 0,
                if (matchResult.groupValues[2].isNotEmpty()) matchResult.groupValues[2].toLong() else 0,
                if (matchResult.groupValues[3].isNotEmpty()) matchResult.groupValues[3].toLong() else 0,
            )
        }
    }
}