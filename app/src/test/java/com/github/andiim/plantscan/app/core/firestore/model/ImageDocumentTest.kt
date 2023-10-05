package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.ui.utils.DataDummy
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImageDocumentTest {
    @Test
    fun assertMappingSuccess() {
        val image = DataDummy.IMAGE
        val document = image.toDocument()

        with(document) {
            assertThat(url).isEqualTo(image.url)
            assertThat(date).isNotNull()
            assertThat(date).isEqualTo(image.date?.toDate())
        }
    }
}
