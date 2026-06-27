package com.github.andiim.plantscan.core.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

class ResultKtTest {
    @Suppress("detekt:TooGenericExceptionThrown", "detekt:UseCheckOrError")
    @Test
    fun result_catches_errors() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }.asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())

                when (val errorResult = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test Done",
                        errorResult.exception?.message,
                    )

                    Result.Loading,
                    is Result.Success,
                    -> throw IllegalStateException(
                        "The flow should hve emitted an Error Result",
                    )
                }
                awaitComplete()
            }
    }
}
