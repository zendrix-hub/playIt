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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

data class AvatarItem(val id: Int, val icon: ImageVector, val color: Color, val name: String)

val AvatarPresets = listOf(
    AvatarItem(1, Icons.Default.Face, Color(0xFFBA68C8), "Purple Face"),
    AvatarItem(2, Icons.Default.Star, Color(0xFFFFD54F), "Gold Star"),
    AvatarItem(3, Icons.Default.Favorite, Color(0xFFF48FB1), "Pink Heart"),
    AvatarItem(4, Icons.Default.ThumbUp, Color(0xFF81C784), "Green Thumb"),
    AvatarItem(5, Icons.Default.Home, Color(0xFF4FC3F7), "Blue House"),
    AvatarItem(6, Icons.Default.Face, Color(0xFFFF8A80), "Orange Face")
)

@Composable
fun ProfileSelectScreen(
    viewModel: ProfileViewModel,
    onProfileSelected: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()

    var showDeleteGuard by remember { mutableStateOf<Long?>(null) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var mathAnswerText by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }

    fun triggerDelete(profileId: Long) {
        val num1 = (2..9).random()
        val num2 = (2..9).random()
        mathProblem = Pair(num1, num2)
        mathAnswerText = ""
        mathError = false
        showDeleteGuard = profileId
    }

    val bgBrush = Brush.verticalGradient(listOf(Color(0xFFEAF6FF), Color(0xFFCBD5E0)))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "WHO IS PLAYING?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2D3748)
            )
            Text(
                text = "Select your profile to start learning!",
                fontSize = 18.sp,
                color = Color(0xFF718096)
            )
            Spacer(modifier = Modifier.height(48.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(profiles) { profile ->
                    val avatar = AvatarPresets.find { it.id == profile.avatarResId } ?: AvatarPresets[0]
                    ProfileGridCard(
                        name = profile.name,
                        avatarColor = avatar.color,
                        avatarIcon = avatar.icon,
                        stars = profile.totalStars,
                        onSelect = {
                            viewModel.selectProfile(profile.profileId)
                            onProfileSelected()
                        },
                        onDeleteClick = {
                            triggerDelete(profile.profileId)
                        }
                    )
                }

                if (profiles.size < 6) {
                    item {
                        AddProfileCard(onClick = onNavigateToCreate)
                    }
                }
            }
        }
    }

    // Delete confirmation with simple arithmetic challenge (gating for parents)
    if (showDeleteGuard != null) {
        AlertDialog(
            onDismissRequest = { showDeleteGuard = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Parent Verification", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("This action is destructive and will erase all profile data. Please solve this problem to proceed:")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "What is ${mathProblem.first} + ${mathProblem.second}?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = mathAnswerText,
                        onValueChange = { mathAnswerText = it },
                        label = { Text("Answer") },
                        singleLine = true,
                        isError = mathError
                    )
                    if (mathError) {
                        Text(
                            text = "Incorrect answer. Please try again.",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val expected = mathProblem.first + mathProblem.second
                        val inputVal = mathAnswerText.toIntOrNull()
                        if (inputVal == expected) {
                            showDeleteGuard?.let { id -> viewModel.deleteProfile(id) }
                            showDeleteGuard = null
                        } else {
                            mathError = true
                        }
                    }
                ) {
                    Text("Verify & Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteGuard = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileGridCard(
    name: String,
    avatarColor: Color,
    avatarIcon: ImageVector,
    stars: Int,
    onSelect: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onSelect() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Delete button at top right
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Profile",
                    tint = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .background(avatarColor, CircleShape)
                        .border(3.dp, Color(0xFFF7FAFC), CircleShape)
                ) {
                    Icon(
                        imageVector = avatarIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "⭐ $stars Stars",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF718096)
                )
            }
        }
    }
}

@Composable
fun AddProfileCard(
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color(0xFFCBD5E0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Profile",
                    tint = Color(0xFF4A90E2),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Add Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2)
            )
        }
    }
}
