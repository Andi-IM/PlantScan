package com.github.andiim.plantscan.testing.data

import com.github.andiim.plantscan.model.data.Plant

private var KNOWN_NAMES = (0..3).map { "name$it" }
val PLANTS: List<Plant> =
    List(10) {
      Plant(
          id = "id@$it",
          name = "name@$it",
          species = "species",
          type = "type",
          images = null,
          commonName = KNOWN_NAMES,
          detail = null)
    }
