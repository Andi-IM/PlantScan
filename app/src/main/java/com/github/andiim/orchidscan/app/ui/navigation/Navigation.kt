package com.github.andiim.orchidscan.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.andiim.orchidscan.app.ui.screens.auth.login.LoginScreen
import com.github.andiim.orchidscan.app.ui.screens.auth.login.LoginViewModel
import com.github.andiim.orchidscan.app.ui.screens.auth.signUp.SignUpScreen
import com.github.andiim.orchidscan.app.ui.screens.auth.signUp.SignUpViewModel
import com.github.andiim.orchidscan.app.ui.screens.detail.DetailScreen
import com.github.andiim.orchidscan.app.ui.screens.detail.DetailViewModel
import com.github.andiim.orchidscan.app.ui.screens.detect.DetectScreen
import com.github.andiim.orchidscan.app.ui.screens.detect.DetectViewModel
import com.github.andiim.orchidscan.app.ui.screens.home.findPlant.FindPlantElement
import com.github.andiim.orchidscan.app.ui.screens.home.findPlant.FindPlantViewModel
import com.github.andiim.orchidscan.app.ui.screens.home.myGarden.MyGardenElement
import com.github.andiim.orchidscan.app.ui.screens.home.myGarden.MyGardenViewModel
import com.github.andiim.orchidscan.app.ui.screens.home.settings.SettingsElement
import com.github.andiim.orchidscan.app.ui.screens.home.settings.SettingsViewModel
import com.github.andiim.orchidscan.app.ui.screens.list.PlantListScreen
import com.github.andiim.orchidscan.app.ui.screens.list.PlantListViewModel
import com.github.andiim.orchidscan.app.ui.screens.splash.SplashScreen
import com.github.andiim.orchidscan.app.ui.screens.web.WebScreen
import com.github.andiim.orchidscan.app.ui.states.PlantScanAppState

@Composable
fun SetupRootNavGraph(appState: PlantScanAppState, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = Direction.Splash.route,
    ) {
        navigation(startDestination = Direction.Login.route, route = Direction.AccountNav.route) {
            authLoginScreen(appState)
            authSignUpScreen(appState)
            webViewScreen(appState)
        }

        detailScreen(appState)
        detectScreen(appState)

        navigation(
            startDestination = Direction.FindPlant.route,
            route = Direction.MainNav.route
        ) {
            homeFindPlantElement(appState)
            homeMyGardenElement(appState)
            homeSettingsElement(appState)
        }

        listScreen(appState)
        splashScreen(appState)
    }
}

private fun NavGraphBuilder.authLoginScreen(appState: PlantScanAppState) {
    composable(route = Direction.Login.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.AccountNav.route)
            }
        val viewModel: LoginViewModel = hiltViewModel(parentEntry)
        LoginScreen(
            openAndPopUp = appState::navigateAndPopUp,
            openWeb = { url ->
                appState.navigate(Direction.Web.setUrl(url), singleTopLaunch = false)
            },
            viewModel = viewModel
        )
    }
}

private fun NavGraphBuilder.authSignUpScreen(appState: PlantScanAppState) {
    composable(route = Direction.SignUp.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.AccountNav.route)
            }
        val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
        SignUpScreen(openAndPopUp = appState::navigateAndPopUp, viewModel = viewModel)
    }
}

private fun NavGraphBuilder.detailScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.Detail.route,
        arguments = listOf(navArgument("orchid_id") { type = NavType.StringType })
    ) { backStackEntry ->
        val viewModel: DetailViewModel = hiltViewModel()
        val id = backStackEntry.arguments?.getString("orchid_id")
        DetailScreen(id = id, popUpScreen = appState::popUp, viewModel = viewModel)
    }
}

private fun NavGraphBuilder.detectScreen(appState: PlantScanAppState) {
    composable(route = Direction.Detect.route) {
        val viewModel: DetectViewModel = hiltViewModel()
        DetectScreen(popUpScreen = appState::popUp, viewModel = viewModel)
    }
}

private fun NavGraphBuilder.homeMyGardenElement(appState: PlantScanAppState) {
    composable(route = Direction.MyGarden.route) {
        val viewModel: MyGardenViewModel = hiltViewModel()
        MyGardenElement(
            toDetail = { appState.navigate(Direction.Detect.route) },
            viewModel = viewModel
        )
    }
}

private fun NavGraphBuilder.homeFindPlantElement(appState: PlantScanAppState) {
    composable(route = Direction.FindPlant.route) {
        val viewModel: FindPlantViewModel = hiltViewModel()
        FindPlantElement(
            onDetails = { appState.navigate(Direction.Detail.createRoute(it)) },
            viewModel = viewModel,
            toDetect = { appState.navigate(Direction.Detect.route) },
            toPlantType = {})
    }
}

private fun NavGraphBuilder.homeSettingsElement(appState: PlantScanAppState) {
    composable(route = Direction.Settings.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.MainNav.route)
            }
        val viewModel: SettingsViewModel = hiltViewModel(parentEntry)
        SettingsElement(
            restartApp = appState::clearAndNavigate,
            openScreen = appState::navigate,
            viewModel = viewModel
        )
    }
}

private fun NavGraphBuilder.listScreen(appState: PlantScanAppState) {
    composable(route = Direction.List.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.MainNav.route)
            }
        val viewModel: PlantListViewModel = hiltViewModel(parentEntry)
        PlantListScreen(
            toDetails = { appState.navigate(Direction.Detail.createRoute(it)) },
            popUpScreen = appState::popUp,
            viewModel = viewModel
        )
    }
}

private fun NavGraphBuilder.splashScreen(appState: PlantScanAppState) {
    composable(route = Direction.Splash.route) {
        SplashScreen(openAndPopUp = appState::navigateAndPopUp)
    }
}

private fun NavGraphBuilder.webViewScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.Web.route,
        arguments = listOf(navArgument("url") { type = NavType.StringType })
    ) { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url")
        url?.let { WebScreen(url = it, name = "Testing", popUpScreen = appState::popUp) }
    }
}
