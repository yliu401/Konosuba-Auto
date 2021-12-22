package com.yliu401.konosubaautomata

import com.yliu401.libautomata.IPattern
import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface IStorageProvider {
    val supportImageTempDir: File

    fun writeSupportImage(kind: SupportImageKind, name: String): OutputStream

    fun readSupportImage(kind: SupportImageKind, name: String): List<InputStream>

    fun list(kind: SupportImageKind): List<String>

    fun dropScreenshot(patterns: List<IPattern>)

    /**
     * For debugging images
     */
    fun dump(name: String, image: IPattern)
}