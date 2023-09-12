package com.github.andiim.plantscan.app.core.utils

import org.jetbrains.annotations.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class AppExecutors @VisibleForTesting constructor(private val diskIO: Executor) {
    companion object;

    @Inject
    constructor() : this(
        Executors.newSingleThreadExecutor()
    )

    fun diskIO(): Executor = diskIO
}
