package com.github.andiim.plantscan.domain

import com.github.andiim.plantscan.data.repository.SearchContentsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSearchContentCountUseCase
@Inject
constructor(private val searchContentsRepository: SearchContentsRepository) {
  operator fun invoke(): Flow<Int> = searchContentsRepository.getSearchContentsCount()
}
