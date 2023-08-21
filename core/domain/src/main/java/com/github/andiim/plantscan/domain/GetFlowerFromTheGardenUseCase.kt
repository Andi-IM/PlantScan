package com.github.andiim.plantscan.domain

import com.github.andiim.plantscan.data.repository.MyGardenRepository
import com.github.andiim.plantscan.model.data.Plant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** A use case which obtains a list of plants which saved to storage */
class GetFlowerFromTheGardenUseCase
@Inject
constructor(private val myGardenRepository: MyGardenRepository) {
  operator fun invoke(): Flow<List<Plant>> = myGardenRepository.plants
}
