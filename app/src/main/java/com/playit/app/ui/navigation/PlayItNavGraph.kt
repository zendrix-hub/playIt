package com.playit.app.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.playit.app.PlayItApplication
import com.playit.app.data.preferences.SessionManager
import com.playit.app.ui.blendit.BlendItScreen
import com.playit.app.ui.blendit.BlendItViewModel
import com.playit.app.ui.findit.FindItScreen
import com.playit.app.ui.findit.FindItViewModel
import com.playit.app.ui.findit.FindItViewModelFactory
import com.playit.app.ui.hearit.HearItScreen
import com.playit.app.ui.map.MapScreen
import com.playit.app.ui.map.MapViewModel
import com.playit.app.ui.parent.ParentDashboardScreen
import com.playit.app.ui.parent.ParentViewModel
import com.playit.app.ui.profile.NamePromptScreen
import com.playit.app.ui.profile.ProfileSelectScreen
import com.playit.app.ui.profile.ProfileViewModel
import com.playit.app.ui.profile.SplashScreen
import com.playit.app.ui.sayit.SayItScreen

@Composable
fun PlayItNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splash"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ── SPLASH SCREEN ───────────────────────────────────────────────────
        composable("splash") {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("profile_select") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // ── PROFILE SELECTION ────────────────────────────────────────────────
        composable("profile_select") {
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication

            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.ProfileViewModelFactory(app.repository)
            )

            ProfileSelectScreen(
                viewModel = profileViewModel,
                onProfileSelected = {
                    navController.navigate("map") {
                        popUpTo("profile_select") { inclusive = true }
                    }
                },
                onNavigateToCreate = {
                    navController.navigate("name_prompt")
                }
            )
        }

        // ── CREATE PROFILE (NAME PROMPT) ─────────────────────────────────────
        composable("name_prompt") {
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication

            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.ProfileViewModelFactory(app.repository)
            )

            NamePromptScreen(
                viewModel = profileViewModel,
                onBack = { navController.popBackStack() },
                onProfileCreated = {
                    navController.navigate("map") {
                        popUpTo("profile_select") { inclusive = true }
                    }
                }
            )
        }

        // ── MAP ──────────────────────────────────────────────────────────────
        composable("map") {
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication
            val activeProfileId = SessionManager.activeProfileId

            val mapViewModel: MapViewModel = viewModel(
                factory = MapViewModel.MapViewModelFactory(app.repository, activeProfileId)
            )

            val nodesState by mapViewModel.mapNodes.collectAsState()
            val activeProfileState by mapViewModel.activeProfile.collectAsState()

            MapScreen(
                profileName = activeProfileState?.name ?: "Player",
                totalStars = activeProfileState?.totalStars ?: nodesState.sumOf { it.starsEarned },
                currentStreak = activeProfileState?.currentStreak ?: 0,
                nodes = nodesState,
                onNodeTapped = { node ->
                    if (node.isBlendIt) {
                        navController.navigate("blend_it/${node.label}")
                    } else {
                        navController.navigate("hear_it/${node.label}")
                    }
                },
                onParentClick = {
                    navController.navigate("parent_dashboard")
                }
            )
        }

        // ── PARENT DASHBOARD ─────────────────────────────────────────────────
        composable("parent_dashboard") {
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication
            val parentViewModel: ParentViewModel = viewModel(
                factory = ParentViewModel.ParentViewModelFactory(app, app.repository)
            )
            ParentDashboardScreen(
                viewModel = parentViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ── 1. HEAR IT ───────────────────────────────────────────────────────
        composable("hear_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            HearItScreen(
                phonemeId = phoneme,
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate("say_it/$phoneme") }
            )
        }

        // ── 2. SAY IT ────────────────────────────────────────────────────────
        composable("say_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            SayItScreen(
                phonemeId = phoneme,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("find_it/$phoneme") }
            )
        }

        // ── 3. FIND IT ───────────────────────────────────────────────────────
        composable("find_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication

            val viewModel: FindItViewModel = viewModel(
                factory = FindItViewModelFactory(
                    application = app,
                    repository = app.repository,
                    phonemeId = phoneme
                )
            )

            FindItScreen(
                phonemeId = phoneme,
                onBack = { navController.popBackStack() },
                onNext = {
                    navController.popBackStack("map", inclusive = false)
                },
                viewModel = viewModel
            )
        }

        // ── 4. BLEND IT ──────────────────────────────────────────────────────
        composable(
            route = "blend_it/{phonemeId}",
            arguments = listOf(navArgument("phonemeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val phonemeId = backStackEntry.arguments?.getString("phonemeId") ?: ""
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication
            val viewModel: BlendItViewModel = viewModel(
                factory = BlendItViewModel.BlendItViewModelFactory(
                    application = app,
                    repository = app.repository,
                    phonemeId = phonemeId
                )
            )

            BlendItScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSessionComplete = {
                    navController.popBackStack("map", inclusive = false)
                }
            )
        }
    }
}