package com.yliu401.konosubaautomata.scripts.supportSelection

import com.yliu401.libautomata.Region

sealed class SpecificSupportSearchResult {
    object NotFound : SpecificSupportSearchResult()
    open class Found(val Support: Region) : SpecificSupportSearchResult()
    class FoundWithBounds(Support: Region, val Bounds: Region) : Found(Support)
}