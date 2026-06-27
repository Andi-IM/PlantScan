package com.github.andiim.plantscan.core.model.data

data class UserData(
    val isLogin: Boolean,
    val userId: String,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)
