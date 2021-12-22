package com.yliu401.konosubaautomata.scripts

import com.yliu401.konosubaautomata.SupportImageKind
import com.yliu401.konosubaautomata.scripts.enums.MaterialEnum
import com.yliu401.libautomata.IPattern

interface IImageLoader {
    operator fun get(img: Images): IPattern

    fun loadSupportPattern(kind: SupportImageKind, name: String): List<IPattern>

    fun loadMaterial(material: MaterialEnum): IPattern

    fun clearImageCache()

    fun clearSupportCache()
}