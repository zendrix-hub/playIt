package com.playit.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.app.ui.components.PlayItPrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamePromptScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onProfileCreated: () -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var selectedAvatarId by remember { mutableStateOf(1) }

    val bgBrush = Brush.verticalGradient(listOf(Color(0xFFEAF6FF), Color(0xFFCBD5E0)))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CREATE PROFILE", fontWeight = FontWeight.Black, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF2D3748),
                    navigationIconContentColor = Color(0xFF2D3748)
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Choose your character:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF718096),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar list selection grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(AvatarPresets) { avatar ->
                    val isSelected = selectedAvatarId == avatar.id
                    val borderStroke = if (isSelected) {
                        Modifier.border(4.dp, Color(0xFF4A90E2), CircleShape)
                    } else {
                        Modifier.border(2.dp, Color.Transparent, CircleShape)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(avatar.color)
                            .then(borderStroke)
                            .clickable { selectedAvatarId = avatar.id }
                    ) {
                        Icon(
                            imageVector = avatar.icon,
                            contentDescription = avatar.name,
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "What is your name?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF718096),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nameText,
                onValueChange = { if (it.length <= 15) nameText = it }, // Limit to 15 characters
                placeholder = { Text("Enter your name here") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color(0xFFCBD5E0)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            PlayItPrimaryButton(
                text = "LET'S PLAY!",
                isEnabled = nameText.trim().isNotEmpty(),
                onClick = {
                    viewModel.createProfile(nameText, selectedAvatarId) { _ ->
                        onProfileCreated()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 24.dp)
            )
        }
    }
}
