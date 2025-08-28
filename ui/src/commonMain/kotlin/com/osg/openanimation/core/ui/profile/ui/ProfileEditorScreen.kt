@file:OptIn(ExperimentalMaterial3Api::class)

package com.osg.openanimation.core.ui.profile.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osg.openanimation.core.ui.components.loading.LoadingAnimation
import com.osg.openanimation.core.ui.components.signin.SignInReasoningDialogView
import com.osg.openanimation.core.ui.profile.state.ProfileScreenState
import com.osg.openanimation.core.ui.profile.state.UserProfileUi
import com.osg.openanimation.core.ui.util.icons.Description
import com.osg.openanimation.core.ui.util.icons.githubVector
import com.osg.openanimation.core.ui.util.icons.social.LinkedinMonoVec
import com.osg.openanimation.core.ui.util.icons.social.SocialIconPack

@Composable
fun ProfileEditorScreenWrapper(
    modifier: Modifier = Modifier,
    profileScreenState: ProfileScreenState,
    saveProfile: (UserProfileUi) -> Unit,
) {
    when (profileScreenState) {
        is ProfileScreenState.Loading -> {
            LoadingAnimation(
                modifier = modifier
            )
        }

        is ProfileScreenState.Success -> {
            var displayName by remember(profileScreenState.userProfileUi.displayName) {
                mutableStateOf(profileScreenState.userProfileUi.displayName)
            }
            var bio by remember(profileScreenState.userProfileUi.bio) {
                mutableStateOf(profileScreenState.userProfileUi.bio)
            }
            var linkedinUrl by remember(profileScreenState.userProfileUi.linkedinUrl) {
                mutableStateOf(profileScreenState.userProfileUi.linkedinUrl)
            }
            var githubUrl by remember(profileScreenState.userProfileUi.githubUrl) {
                mutableStateOf(profileScreenState.userProfileUi.githubUrl)
            }
            var imageBitmap by remember(profileScreenState.userProfileUi.imageBitmap) {
                mutableStateOf(profileScreenState.userProfileUi.imageBitmap)
            }
            val profileUiDerived = UserProfileUi(
                displayName = displayName,
                bio = bio,
                linkedinUrl = linkedinUrl,
                githubUrl = githubUrl,
                imageBitmap = imageBitmap
            )
            ProfileEditorScreen(
                modifier = modifier,
                profileUi = profileUiDerived,
                onImageChanged = {
                    imageBitmap = it
                },
                onDisplayNameChange = {
                    displayName = it
                },
                onBioChange = {
                    bio = it
                },
                onGithubChange = {
                    githubUrl = it
                },
                onLinkedinChange = {
                    linkedinUrl = it
                },
                saveProfile = {
                    saveProfile(
                        profileUiDerived
                    )
                }
            )
        }

        ProfileScreenState.SignedOut -> {
            Box(
                modifier = modifier
            ) {
                SignInReasoningDialogView(
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }

}

@Composable
fun ProfileEditorScreen(
    modifier: Modifier = Modifier,
    profileUi: UserProfileUi,
    onImageChanged: (ImageBitmap) -> Unit = {},
    onDisplayNameChange: (String) -> Unit = {},
    onBioChange: (String) -> Unit = {},
    onGithubChange: (String) -> Unit = {},
    onLinkedinChange: (String) -> Unit = {},
    saveProfile: () -> Unit,
) {
    var isPreviewMode by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 500.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProfileHeader(
                    isPreviewMode = isPreviewMode,
                    onModeChange = { isPreviewMode = it }
                )
            }

            item {
                Box(
                    modifier = Modifier.animateContentSize()
                ) {
                    if (isPreviewMode) {
                        PreviewCard(
                            displayName = profileUi.displayName,
                            bio = profileUi.bio ?: "",
                            linkedinUrl = profileUi.linkedinUrl ?: "",
                            githubUrl = profileUi.githubUrl ?: "",
                            imageLoader = profileUi.imageBitmap,
                            onImageChanged = onImageChanged,
                        )
                    } else {
                        EditForm(
                            displayName = profileUi.displayName,
                            onDisplayNameChange = onDisplayNameChange,
                            bio = profileUi.bio ?: "",
                            onBioChange = onBioChange,
                            linkedinUrl = profileUi.linkedinUrl ?: "",
                            onLinkedinChange = onLinkedinChange,
                            githubUrl = profileUi.githubUrl ?: "",
                            onGithubChange = onGithubChange,
                            saveProfile = saveProfile
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    isPreviewMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Account Settings",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        SegmentedButton(
            isPreviewMode = isPreviewMode,
            onModeChange = onModeChange
        )
    }
}

@Composable
fun SegmentedButton(
    isPreviewMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            SegmentButton(
                text = "Edit",
                isSelected = !isPreviewMode,
                onClick = { onModeChange(false) },
                modifier = Modifier
            )
            
            SegmentButton(
                text = "Preview",
                isSelected = isPreviewMode,
                onClick = { onModeChange(true) },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SegmentButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .clickable { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
            color = textColor,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditForm(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    linkedinUrl: String,
    onLinkedinChange: (String) -> Unit,
    githubUrl: String,
    onGithubChange: (String) -> Unit,
    saveProfile: () -> Unit,
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Avatar Section
            AvatarSection(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                displayName = displayName
            )
            
            // Display Name
            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                label = { Text("Display Name") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
            
            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = onBioChange,
                label = { Text("Bio") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                maxLines = 4,
            )

            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )

            // Social Links Section
            Text(
                text = "Social Links",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // LinkedIn
            SocialLinkField(
                value = linkedinUrl,
                onValueChange = onLinkedinChange,
                label = "LinkedIn",
                icon = SocialIconPack.LinkedinMonoVec,
                modifier = Modifier.fillMaxWidth()
            )
            
            // GitHub
            SocialLinkField(
                value = githubUrl,
                onValueChange = onGithubChange,
                label = "GitHub",
                icon = githubVector,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { saveProfile() },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun SocialLinkField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun AvatarSection(
    modifier: Modifier = Modifier,
    displayName: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            // Avatar
            AvatarLetters(
                displayName = displayName,
                modifier = Modifier.size(100.dp)
            )
            
            // Upload button
            FloatingActionButton(
                onClick = { /* TODO: Handle avatar upload */ },
                modifier = Modifier.size(32.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Upload photo",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Text(
            text = "Tap the camera to upload a new photo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AvatarLetters(
    displayName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        val letter = displayName.split(' ')
        val firstChar = letter.getOrNull(0)?.firstOrNull() ?: 'O'
        val secondChar = letter.getOrNull(1)?.firstOrNull() ?: 'K'
        Text(
            text = "$firstChar$secondChar".uppercase(),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun PreviewCard(
    displayName: String,
    bio: String,
    linkedinUrl: String,
    githubUrl: String,
    imageLoader: ImageBitmap? = null,
    onImageChanged: (ImageBitmap) -> Unit = {},
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageLoader != null) {
                    Image(
                        bitmap = imageLoader,
                        contentDescription = "Profile Image",
                        modifier = Modifier.clip(CircleShape).size(76.dp)
                    )
                } else {
                    Text(
                        text = displayName.take(2).uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            // Name
            Text(
                text = displayName.ifEmpty { "Your Name" },
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (bio.isNotEmpty()){
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Social Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val uriHandler = LocalUriHandler.current
                if (linkedinUrl.isNotEmpty()) {
                    SocialIconButton(
                        icon = SocialIconPack.LinkedinMonoVec, // Replace with LinkedIn icon if available
                        contentDescription = "LinkedIn",
                        onClick = {
                            uriHandler.openUri(linkedinUrl)
                        }
                    )
                }
                if (githubUrl.isNotEmpty()) {
                    SocialIconButton(
                        icon = githubVector, // Replace with GitHub icon if available
                        contentDescription = "GitHub",
                        onClick = {
                            uriHandler.openUri(githubUrl)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SocialIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
        )
    }
}

