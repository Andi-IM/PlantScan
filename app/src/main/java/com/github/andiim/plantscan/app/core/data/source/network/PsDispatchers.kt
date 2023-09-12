package com.github.andiim.plantscan.app.core.data.source.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val psDispatcher: PsDispatchers)
enum class PsDispatchers {
    Default,
    IO
}
