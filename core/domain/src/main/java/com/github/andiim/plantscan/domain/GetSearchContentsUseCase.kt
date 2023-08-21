package com.github.andiim.plantscan.domain

import com.github.andiim.plantscan.data.repository.SearchContentsRepository
import javax.inject.Inject

class GetSearchContentsUseCase @Inject constructor(
    private val searchContentsRepository: SearchContentsRepository
) {

    // TODO CREATE DATA STORE / USER PREFERENCES
}