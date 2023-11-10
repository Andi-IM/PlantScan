package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.testdoubles.TestPsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals

class DefaultDetectHistoryRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: DefaultDetectHistoryRepo
    private lateinit var firebase: TestPsFirebaseDataSource

    @Before
    fun setup() {
        firebase = TestPsFirebaseDataSource()
        subject = DefaultDetectHistoryRepo(
            firebase = firebase,
        )
    }

    @Test
    fun `defaultDetectHistoryRepo when recordDetection is returned an id`() = testScope.runTest {
        val id = UUID.randomUUID().toString()
        val testInput = HistoryDocument(
            id = id,
            timestamp = Date.from(Clock.System.now().toJavaInstant()),
            userId = "Userid",
            plantRef = "plantRef",
            accuracy = 0.1f,
            image = "image",
            detections = listOf(),
        )

        assertEquals(
            expected = firebase.recordDetection(testInput),
            actual = subject.recordDetection(testInput.asExternalModel()).first(),
        )
    }

    @Test
    fun `defaultDetectHistoryRepo when getDetectionHistories is returned a List of DetectionHistory`() =
        testScope.runTest {
            assertEquals(
                expected = firebase.getDetectionHistories("userId")
                    .map(HistoryDocument::asExternalModel),
                actual = subject.getDetectionHistories("userId").first(),
            )
        }

    @Test
    fun `defaultDetectHistoryRepo when getDetectionDetail is returned detail of DetectionHistories`() =
        testScope.runTest {
            val id = UUID.randomUUID().toString()
            val testInput = HistoryDocument(
                id = id,
                timestamp = Date.from(Clock.System.now().toJavaInstant()),
                userId = "Userid",
                plantRef = "plantRef",
                accuracy = 0.1f,
                image = "image",
                detections = listOf(),
            )
            subject.recordDetection(testInput.asExternalModel()).collect()

            assertEquals(
                expected = firebase.getDetectionDetail(id).asExternalModel(),
                actual = subject.getDetectionDetail(id).first(),
            )
        }
}
