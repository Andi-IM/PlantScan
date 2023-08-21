package com.github.andiim.plantscan.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val plantDispatcher: PlantScantDispatchers)

enum class PlantScantDispatchers {
  Default,
  IO
}
