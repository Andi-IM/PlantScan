package com.github.andiim.plantscan.core.common

import app.cash.turbine.test
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.common.result.asResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceKtTest {
  @Test
  fun resource_catches_errors() = runTest {
    flow {
          emit(1)
          throw Exception("Test Done")
        }
        .asResult()
        .test {
          assertEquals(Resource.Loading, awaitItem())
          assertEquals(Resource.Success(1), awaitItem())

          when (val errorResource = awaitItem()) {
            is Resource.Error ->
                assertEquals(
                    "Test Done",
                    errorResource.exception?.message,
                )
            Resource.Loading,
            is Resource.Success, ->
                throw IllegalArgumentException(
                    "The flow should have emitted an Error Result",
                )
          }
          awaitComplete()
        }
  }
}
