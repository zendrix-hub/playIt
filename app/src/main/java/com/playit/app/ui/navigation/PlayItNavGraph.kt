package com.playit.app.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playit.app.ui.hearit.HearItScreen
import com.playit.app.ui.sayit.SayItScreen
import com.playit.app.ui.findit.FindItScreen
import com.playit.app.ui.blendit.BlendItScreen
import com.playit.app.ui.blendit.BlendItViewModel
import com.playit.app.ui.blendit.BlendItViewModelFactory
import com.playit.app.ui.map.MapScreen
import com.playit.app.ui.map.MapNodeState

// ─── Marungko sequence + BlendIt checkpoints ─────────────────────────────────
//
// BlendIt checkpoint nodes are inserted after letter groups:
//   Group 1: m, s, a, i, o          → BlendIt checkpoint (blend_1)
//   Group 2: b, e, u, t, k          → BlendIt checkpoint (blend_2)
//   Group 3: l, y, n, g, ng         → BlendIt checkpoint (blend_3)
//   Group 4: p, r, d, h, w          → BlendIt checkpoint (blend_4)
//   Group 5: c, f, j, ñ, q, v, x, z → BlendIt checkpoint (blend_5)
//
// For MVP: only the first node (m) is unlocked. As you wire Room DB progress,
// replace this hardcoded list with data from your UnlockManager/DAO.

private fun buildMvpNodes(): List<MapNodeState> {
    val sequence = listOf(
        // Group 1
        "m", "s", "a", "i", "o",
        "BLEND_1",
        // Group 2
        "b", "e", "u", "t", "k",
        "BLEND_2",
        // Group 3
        "l", "y", "n", "g", "ng",
        "BLEND_3",
        // Group 4
        "p", "r", "d", "h", "w",
        "BLEND_4",
        // Group 5
        "c", "f", "j", "ñ", "q", "v", "x", "z",
        "BLEND_5"
    )

    return sequence.mapIndexed { index, label ->
        val isBlend = label.startsWith("BLEND_")
        MapNodeState(
            id         = index,
            label      = label,
            isUnlocked = index == 0,   // MVP: only first node (m) unlocked
            starsEarned = 0,
            isBlendIt  = isBlend
        )
    }
}

// ─── Nav Graph ────────────────────────────────────────────────────────────────

@Composable
fun PlayItNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "map"
) {
    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        // ── MAP ──────────────────────────────────────────────────────────────
        composable("map") {
            MapScreen(
                profileName   = "Player 1",   // TODO: wire from SessionManager
                totalStars    = 0,            // TODO: wire from StarCalculator
                currentStreak = 0,            // TODO: wire from StreakTracker
                nodes         = buildMvpNodes(),
                onNodeTapped  = { node ->
                    if (node.isBlendIt) {
                        // BlendIt checkpoint — passes the blend group id as phonemeId
                        navController.navigate("blend_it/${node.label}")
                    } else {
                        // Normal letter node — starts the Hear It → Say It → Find It flow
                        navController.navigate("hear_it/${node.label}")
                    }
                }
            )
        }

        // ── 1. HEAR IT ───────────────────────────────────────────────────────
        composable("hear_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            HearItScreen(
                phonemeId  = phoneme,
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate("say_it/$phoneme") }
            )
        }

        // ── 2. SAY IT ────────────────────────────────────────────────────────
        composable("say_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            SayItScreen(
                phonemeId = phoneme,
                onBack    = { navController.popBackStack() },
                onNext    = { navController.navigate("find_it/$phoneme") }
            )
        }

        // ── 3. FIND IT ───────────────────────────────────────────────────────
        composable("find_it/{phonemeId}") { backStackEntry ->
            val phoneme = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            FindItScreen(
                phonemeId = phoneme,
                onBack    = { navController.popBackStack() },
                onNext    = { navController.navigate("blend_it/$phoneme") }
            )
        }

        // ── 4. BLEND IT ──────────────────────────────────────────────────────
        composable("blend_it/{phonemeId}") { backStackEntry ->
            val phoneme     = backStackEntry.arguments?.getString("phonemeId") ?: "m"
            val application = LocalContext.current.applicationContext as Application
            val viewModel: BlendItViewModel = viewModel(
                factory = BlendItViewModelFactory(application, phoneme)
            )
            BlendItScreen(
                viewModel         = viewModel,
                onBack            = { navController.popBackStack() },
                onSessionComplete = {
                    navController.navigate("map") {
                        popUpTo("map") { inclusive = false }
                    }
                }
            )
        }
    }
}