package com.github.andiim.plantscan.core.firestore.fake

import java.io.InputStream

fun interface FakeAssetManager {
    fun open(fileName: String): InputStream
}
