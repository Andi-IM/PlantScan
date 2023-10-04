package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.ui.utils.DataDummy
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlantDocumentTest {
    @Test
    fun testPlantDocument() {
        val plant = DataDummy.PLANTS.first()

        val document = plant.toDocument()

        with(document){
            assertThat(id).isEqualTo(plant.id)
            assertThat(taxonomy).isEqualTo(plant.taxon.toDocument())
            assertThat(description).isEqualTo(plant.description)
            assertThat(species).isEqualTo(plant.species)
            assertThat(name).isEqualTo(plant.name)
            assertThat(images).isEqualTo(plant.images.map { it.toDocument() })
            assertThat(thumbnail).isEqualTo(plant.thumbnail)
            assertThat(commonName).isEqualTo(plant.commonName.map { PlantDocument.CommonName(it) })
        }
    }
}