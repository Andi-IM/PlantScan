package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.testdoubles.TestPsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultPlantRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: DefaultPlantRepository
    private lateinit var firebase: TestPsFirebaseDataSource

    @Before
    fun setup() {
        firebase = TestPsFirebaseDataSource()
        subject = DefaultPlantRepository(
            firebase = firebase,
        )
    }

    @Test
    fun `defaultPlantRepositoryTest when getPlants return Plants`() = testScope.runTest {
        assertEquals(
            expected = firebase.getPlants().map(PlantDocument::asExternalModel),
            actual = subject.getPlants().first(),
        )
    }

    @Test
    fun `defaultPlantRepositoryTest when getPlantById return Plant`() = testScope.runTest {
        assertEquals(
            expected = firebase.getPlantById("plantId").asExternalModel(),
            actual = subject.getPlantById("plantId").first(),
        )
    }
}
