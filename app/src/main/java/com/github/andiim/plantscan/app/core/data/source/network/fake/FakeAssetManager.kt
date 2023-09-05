package com.github.andiim.plantscan.app.core.data.source.network.fake

import java.io.InputStream

interface FakeAssetManager {
    fun open(fileName: String): InputStream
}