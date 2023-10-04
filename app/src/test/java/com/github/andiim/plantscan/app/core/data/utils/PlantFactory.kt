package com.github.andiim.plantscan.app.core.data.utils

import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import java.util.concurrent.atomic.AtomicInteger

class PlantFactory {
    private val counter = AtomicInteger(0)
    fun createPlantItem(name: String): Plant {
        val id = counter.incrementAndGet()
        val taxon = Taxonomy(name, name, name, name, name)
        val knownNames = (0..3).map { "name$it" }

        return Plant(
            id = "$id",
            name = "$name@$id",
            species = "species",
            images = listOf(),
            commonName = knownNames,
            thumbnail = name,
            description = name,
            taxon = taxon
        )
    }
}