package com.github.andiim.plantscan.domain

import com.github.andiim.plantscan.data.repository.AccountRepository
import javax.inject.Inject

class HasUserAccountUseCase @Inject constructor(private val repository: AccountRepository) {
  operator fun invoke(): Boolean = repository.hasUser
}
