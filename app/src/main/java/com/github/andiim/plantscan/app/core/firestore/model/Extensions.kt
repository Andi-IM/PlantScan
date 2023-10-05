package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.Image
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import kotlinx.datetime.toInstant

fun TaxonomyDocument.toModel(): Taxonomy = Taxonomy(
    phylum = this.phylum,
    className = this.className,
    order = this.order,
    family = this.family,
    genus = this.genus
)

fun Taxonomy.toDocument(): TaxonomyDocument = TaxonomyDocument(
    phylum = this.phylum,
    order = this.order,
    family = this.family,
    genus = this.genus,
    this.className
)

fun ImageDocument.toModel(): Image = Image(
    url = this.url,
    date = this.date?.toInstant()?.toString()?.toInstant()!!,
    description = this.description,
    attribution = this.attribution
)

fun Image.toDocument(): ImageDocument =
    ImageDocument(url = this.url, date = this.date?.toDate()!!)

fun Plant.toDocument(): PlantDocument = PlantDocument(
    this.id,
    this.taxon.toDocument(),
    this.description,
    this.species,
    this.name,
    this.images.map { it.toDocument() },
    this.thumbnail,
    this.commonName
)

fun PlantDocument.toModel(): Plant = Plant(
    id = this.id,
    taxon = this.taxonomy?.toModel()!!,
    species = this.species,
    name = this.name,
    images = this.images.map {
        it.toModel()
    },
    commonName = this.commonName.map { it.name },
    thumbnail = this.thumbnail,
    description = this.description,
)
