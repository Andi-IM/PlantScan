package com.github.andiim.plantscan.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.feature.signup.SignUpScreen

const val signUpRoute = "signup"

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
  this.navigate(com.github.andiim.plantscan.feature.signup.navigation.signUpRoute, navOptions)
}

fun NavGraphBuilder.authSignUpScreen(navigateAndPopUp: (String, String) -> Unit) {
  composable(route = com.github.andiim.plantscan.feature.signup.navigation.signUpRoute) { SignUpScreen(openAndPopUp = navigateAndPopUp) }
}
