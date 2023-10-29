package com.github.andiim.plantscan.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result: Flow<ResultType> = flow {
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            try {
                val response = createCall().first()
                saveCallResult(response)
                emitAll(loadFromDB())
            } catch (e: Exception) {
                throw e
            }
        } else {
            emitAll(loadFromDB())
        }
    }

    protected open fun onFetchFailed() = Unit
    protected abstract fun loadFromDB(): Flow<ResultType>
    protected abstract fun shouldFetch(data: ResultType?): Boolean
    protected abstract suspend fun createCall(): Flow<RequestType>
    protected abstract suspend fun saveCallResult(data: RequestType)
    fun asFlow() = result
}
