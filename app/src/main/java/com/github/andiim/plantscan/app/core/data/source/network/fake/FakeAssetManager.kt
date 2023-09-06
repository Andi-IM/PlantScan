package com.github.andiim.plantscan.app.core.data.source.network.fake

import java.io.InputStream

fun interface FakeAssetManager {
    fun open(fileName: String): InputStream
}