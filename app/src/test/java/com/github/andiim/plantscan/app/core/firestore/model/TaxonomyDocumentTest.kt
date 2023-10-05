package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.ui.utils.DataDummy
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TaxonomyDocumentTest {
    @Test
    fun assertMappingSuccess() {
        val taxonomy = DataDummy.TAXONOMY
        val document = taxonomy.toDocument()

        with(document) {
            assertThat(genus).isEqualTo(taxonomy.genus)
            assertThat(className).isEqualTo(taxonomy.className)
            assertThat(family).isEqualTo(taxonomy.family)
            assertThat(order).isEqualTo(taxonomy.order)
            assertThat(phylum).isEqualTo(taxonomy.phylum)
        }
    }
}
