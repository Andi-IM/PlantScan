package com.github.andiim.plantscan.app.core.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import org.jetbrains.annotations.VisibleForTesting

class AppExecutors @VisibleForTesting constructor(private val diskIO: Executor) {
    companion object;

    @Inject
    constructor(): this(
        Executors.newSingleThreadExecutor()
    )

    fun diskIO(): Executor = diskIO
}