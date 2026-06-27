package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asDocument
import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.testdoubles.TestPsFirebaseDataSource
import com.github.andiim.plantscan.core.data.testdoubles.TestPsNetworkDataSource
import com.github.andiim.plantscan.core.model.data.Suggestion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class JustOnlineDetectRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: JustOnlineDetectRepository
    private lateinit var network: TestPsNetworkDataSource
    private lateinit var firebase: TestPsFirebaseDataSource

    @Before
    fun setup() {
        network = TestPsNetworkDataSource()
        firebase = TestPsFirebaseDataSource()
        subject = JustOnlineDetectRepository(
            network = network,
            firebase = firebase,
        )
    }

    @Test
    fun `justOnlineDetectRepository when detect returns detection result`() = testScope.runTest {
        assertEquals(
            expected = network.detect("image", 1, 1).asExternalModel(),
            actual = subject.detect("image", 1, 1).first(),
        )
    }

    @Test
    fun `justOnlineDetectRepository when sendSuggestion return result`() = testScope.runTest {
        val suggestion = Suggestion()
        assertEquals(
            expected = firebase.sendSuggestion(suggestion.asDocument()),
            actual = subject.sendSuggestion(suggestion = suggestion).first(),
        )
    }
}
