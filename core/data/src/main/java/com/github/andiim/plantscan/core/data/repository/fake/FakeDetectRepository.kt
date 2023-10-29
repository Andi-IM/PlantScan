package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.data.repository.DetectRepository
import com.github.andiim.plantscan.core.model.data.ObjectDetection
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import com.github.andiim.plantscan.core.network.fake.FakePsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Fake implementation of the [DetectRepository].
 */
class FakeDetectRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: FakePsNetworkDataSource,
) : DetectRepository {
    override fun detect(base64ImageData: String, confidence: Int): Flow<ObjectDetection> = flow {
        emit(dataSource.detect(base64ImageData, confidence).asExternalModel())
    }.flowOn(ioDispatcher)
}
