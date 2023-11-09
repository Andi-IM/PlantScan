package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.auth.AuthHelper
import javax.inject.Inject

class GetUserIdUsecase @Inject constructor(
    private val authHelper: AuthHelper,
) {
    operator fun invoke(): String = authHelper.currentUserId
}