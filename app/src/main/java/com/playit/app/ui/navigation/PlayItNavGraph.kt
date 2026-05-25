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
import com.playit.app.ui.blendit.BlendItScreen
import com.playit.app.ui.blendit.BlendItViewModel
import com.playit.app.ui.findit.FindItScreen
import com.playit.app.ui.findit.FindItViewModel
import com.playit.app.ui.findit.FindItViewModelFactory
import com.playit.app.ui.hearit.HearItScreen
import com.playit.app.ui.map.MapScreen
import com.playit.app.ui.map.MapViewModel
import com.playit.app.ui.sayit.SayItScreen

@Composable
fun PlayItNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "map"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ── MAP ──────────────────────────────────────────────────────────────
        composable("map") {
            val context = LocalContext.current
            val app = context.applicationContext as PlayItApplication

            val mapViewModel: MapViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return MapViewModel(app.repository) as T
                    }
                }
            )

            val nodesState by mapViewModel.mapNodes.collectAsState()

            MapScreen(
                profileName = "Player 1",
                totalStars = nodesState.sumOf { it.starsEarned },
                currentStreak = 0,
                nodes = nodesState,
                onNodeTapped = { node ->
                    // Logic: If it's a BlendIt node, go to BlendIt. Otherwise, start the letter sequence.
                    if (node.isBlendIt) {
                        navController.navigate("blend_it/${node.label}")
                    } else {
                        navController.navigate("hear_it/${node.label}")
                    }
                }
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
                // FIX: Instead of navigating to blend_it, we go back to the map.
                // The map logic handles the unlocking of the next letter or the blend node.
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

            val viewModel: BlendItViewModel = viewModel(
                factory = BlendItViewModel.BlendItViewModelFactory(
                    application = context.applicationContext as Application,
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