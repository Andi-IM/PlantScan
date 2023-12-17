package com.github.andiim.plantscan.feature.account.validators

interface BaseUseCase<In, Out> {
    fun execute(input: In): Out
}
