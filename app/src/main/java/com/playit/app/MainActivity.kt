package com.playit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.playit.app.ui.navigation.PlayItNavGraph
import com.playit.app.ui.theme.PlayItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // 1. The Global Wrapper: Enforces your child psychology color palette and Material3 rules
            PlayItTheme {

                // 2. The Global Surface: Automatically applies your background color to the root window
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // 3. The Navigation: This initializes your NavHost, starting automatically at Routes.MAP
                    PlayItNavGraph()

                }
            }
        }
    }
}