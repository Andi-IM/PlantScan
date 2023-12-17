package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.ImageDocument
import com.github.andiim.plantscan.core.firestore.model.LabelPredictDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.core.model.data.LabelPredict
import com.github.andiim.plantscan.core.model.data.Suggestion
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class FirebaseDocumentKtTest {
    private val labelPredictDocument = LabelPredictDocument(
        objectClass = "class",
        confidence = 0.1f,
    )
    private val historyDocument = HistoryDocument(
        id = "id",
        plantRef = "plantRef",
        userId = "userId",
        accuracy = 100.0f,
        timestamp = Date.from(Clock.System.now().toJavaInstant()),
        image = "image",
        detections = listOf(labelPredictDocument),
    )

    @Test
    fun `historyDocument can be mapped to History`() {
        val model = historyDocument.asExternalModel()
        assertEquals(historyDocument.id, model.id)
        assertEquals(historyDocument.plantRef, model.plantRef)
        assertEquals(historyDocument.userId, model.userId)
        assertEquals(historyDocument.accuracy, model.accuracy)
        assertEquals(
            historyDocument.timestamp.toString(),
            // Date from kotlinx-datetime, so map it first to java.date.
            Date.from(model.timestamp.toJavaInstant()).toString(),
        )
        assertEquals(historyDocument.image, model.image)
    }

    @Test
    fun `labelPredictDocument can be mapped to LabelPredict`() {
        val model = labelPredictDocument.asExternalModel()
        assertEquals(labelPredictDocument.confidence, model.confidence)
        assertEquals(labelPredictDocument.objectClass, model.objectClass)
    }

    @Test
    fun `javaDate can be mapped to kotlinx datetime Instant`() {
        val actual = Clock.System.now()
        val expected = Date.from(actual.toJavaInstant()).toInstantKtx()
        // Note: assertion will be error because of different string long.
        assertEquals(
            expected.toEpochMilliseconds(),
            actual.toEpochMilliseconds(),
        )
    }

    private val labelPredict = LabelPredict(
        objectClass = "class",
        confidence = 0.1f,
    )

    private val detectionHistory = DetectionHistory(
        id = "id",
        timestamp = Clock.System.now(),
        plantRef = "plantRef",
        userId = "userId",
        accuracy = 100.0f,
        image = "image",
        detections = listOf(labelPredict),
        time = 1.0f,
    )

    @Test
    fun `DetectionHistory can be mapped to HistoryDocument`() {
        val document = detectionHistory.asDocument()
        assertEquals(detectionHistory.id, document.id)
        assertEquals(detectionHistory.plantRef, document.plantRef)
        assertEquals(detectionHistory.userId, document.userId)
        assertEquals(detectionHistory.accuracy, document.accuracy)

        // Note: assertion will be error because of different string long between java instant and
        // instant-ktx.
        assertEquals(
            detectionHistory.timestamp.toEpochMilliseconds().toString(),
            // Date from kotlinx-datetime, so map it first to java.date.
            document.timestamp?.toInstantKtx()?.toEpochMilliseconds().toString(),
        )
        assertEquals(detectionHistory.image, document.image)
    }

    private val commonName = PlantDocument.CommonName(name = "commonName")
    private val imageDocument = ImageDocument(
        id = 1_1,
        url = "url",
        date = Date.from(Clock.System.now().toJavaInstant()),
        attribution = "",
    )
    private val plantDocument = PlantDocument(
        id = "id",
        name = "name",
        species = "species",
        description = "description",
        thumbnail = "thumbnail",
        commonName = listOf(commonName),
        images = listOf(imageDocument),
    )

    @Test
    fun `PlantDocument can be mapped to Plant`() {
        val model = plantDocument.asExternalModel()
        val listOfCommonName = plantDocument.commonName.map(PlantDocument.CommonName::name)

        assertEquals(plantDocument.id, model.id)
        assertEquals(plantDocument.name, model.name)
        assertEquals(plantDocument.species, model.species)
        assertEquals(plantDocument.description, model.description)
        assertEquals(plantDocument.thumbnail, model.thumbnail)
        assertEquals(listOfCommonName, model.commonName)
    }

    @Test
    fun `ImageDocument can be mapped to PlantImage`() {
        val model = imageDocument.asExternalModel()
        assertEquals(imageDocument.id.toString(), model.id)
        assertEquals(imageDocument.description, model.description)
        assertEquals(imageDocument.attribution, model.attribution)
        assertEquals(imageDocument.url, model.url)
    }

    private val suggestion = Suggestion(
        id = "id",
        userId = "userId",
        date = Date.from(Clock.System.now().toJavaInstant()),
        description = "description",
        images = listOf("string")
    )

    @Test
    fun `Suggestion can be mapped to SuggestionDocument`() {
        val document = suggestion.asDocument()
        assertEquals(suggestion.id, document.id)
        assertEquals(suggestion.userId, document.userId)
        assertEquals(suggestion.description, document.description)
        assertEquals(suggestion.images, document.images)
    }
}
