package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Post
import com.example.model.PostType
import com.example.model.SystemNotice
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.model.VerificationRecord
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400

@Composable
fun AdminScreen(
    currentUser: UserProfile,
    allUsers: List<UserProfile>,
    allPosts: List<Post>,
    systemNotices: List<SystemNotice>,
    adminStealthMode: Boolean = true,
    strictRegistrationMode: Boolean = true,
    verificationRecords: List<VerificationRecord> = emptyList(),
    onToggleSuspendUser: (String) -> Unit,
    onChangeUserRole: (String, UserRole) -> Unit,
    onToggleVerifyUser: (String) -> Unit,
    onResetPassword: (String, String) -> Unit,
    onDeleteUser: (String) -> Unit,
    onDeletePost: (String) -> Unit,
    onTogglePinPost: (String) -> Unit,
    onDeleteAnswer: (String, String) -> Unit = { _, _ -> },
    onDeleteComment: (String, String) -> Unit = { _, _ -> },
    onToggleAdminStealth: () -> Unit = {},
    onToggleStrictRegistration: () -> Unit = {},
    onManualGenerateCode: (String) -> Unit = {},
    onBroadcastNotice: (String, String) -> Unit,
    onDeleteNotice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    var userSearchQuery by remember { mutableStateOf("") }
    var userFilterRole by remember { mutableStateOf<String>("All") }
    var postSearchQuery by remember { mutableStateOf("") }

    val adminSections = listOf("Users Control", "Content Moderation", "Security & OTP", "Broadcast Notices")

    val totalAnswers = remember(allPosts) { allPosts.sumOf { it.answers.size } }
    val suspendedUsersCount = remember(allUsers) { allUsers.count { it.isSuspended } }
    val verifiedUsersCount = remember(allUsers) { allUsers.count { it.isVerified } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("admin_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Super Admin Badge & Identity Card (Admin ID is strictly protected & hidden)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BrandPurple.copy(alpha = 0.08f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandPurple
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Ask ME Main Admin Control",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (adminStealthMode) "🛡️ Admin ID Hidden (askman36@gmail.com protected)" else "Logged in as: ${currentUser.email}",
                                    fontSize = 12.sp,
                                    color = BrandPurple,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandEmerald.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(12.dp))
                                Text(
                                    text = if (adminStealthMode) "STEALTH ACTIVE" else "FULL CONTROL",
                                    color = BrandEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Text(
                        text = "Everything on Ask ME can be controlled from your Super Admin ID. Your personal administrator identity and email remain hidden from public view.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    // 4 KPI Summary Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminKpiBadge(title = "Members", value = "${allUsers.size}", color = BrandIndigoPrimary, modifier = Modifier.weight(1f))
                        AdminKpiBadge(title = "Posts", value = "${allPosts.size}", color = BrandPurple, modifier = Modifier.weight(1f))
                        AdminKpiBadge(title = "Verified", value = "$verifiedUsersCount", color = BrandEmerald, modifier = Modifier.weight(1f))
                        AdminKpiBadge(title = "Suspended", value = "$suspendedUsersCount", color = Color.Red, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // 2. Navigation Tabs between Admin sections
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedSection,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                adminSections.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSection == index,
                        onClick = { selectedSection = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (selectedSection == index) BrandPurple else Slate400
                            )
                        }
                    )
                }
            }
        }

        // 3. Section Content
        when (selectedSection) {
            0 -> {
                // Section 0: User Management (পূর্ণ সদস্য নিয়ন্ত্রণ)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = userSearchQuery,
                            onValueChange = { userSearchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Search by name, email, phone, college...", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPurple,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        // Role & Status Filter Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("All", "Members", "Verified", "Suspended", "Admins").forEach { chip ->
                                FilterChip(
                                    selected = userFilterRole == chip,
                                    onClick = { userFilterRole = chip },
                                    label = { Text(chip, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandPurple,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                val filteredUsers = allUsers.filter { user ->
                    val matchesSearch = userSearchQuery.isBlank() ||
                            user.name.contains(userSearchQuery, ignoreCase = true) ||
                            user.email.contains(userSearchQuery, ignoreCase = true) ||
                            user.phone.contains(userSearchQuery, ignoreCase = true) ||
                            user.college.contains(userSearchQuery, ignoreCase = true) ||
                            user.work.contains(userSearchQuery, ignoreCase = true)

                    val matchesFilter = when (userFilterRole) {
                        "Members" -> user.userRole == UserRole.MEMBER
                        "Verified" -> user.isVerified
                        "Suspended" -> user.isSuspended
                        "Admins" -> user.userRole == UserRole.SUPER_ADMIN || user.userRole == UserRole.ADMIN
                        else -> true
                    }
                    matchesSearch && matchesFilter
                }

                if (filteredUsers.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No members found matching \"$userSearchQuery\"",
                                modifier = Modifier.padding(24.dp),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        }
                    }
                } else {
                    items(filteredUsers, key = { it.id }) { targetUser ->
                        AdminUserControlCard(
                            user = targetUser,
                            isSelf = targetUser.id == currentUser.id,
                            onToggleSuspend = { onToggleSuspendUser(targetUser.id) },
                            onChangeRole = { newRole -> onChangeUserRole(targetUser.id, newRole) },
                            onToggleVerify = { onToggleVerifyUser(targetUser.id) },
                            onResetPassword = { newPass -> onResetPassword(targetUser.id, newPass) },
                            onDelete = { onDeleteUser(targetUser.id) }
                        )
                    }
                }
            }

            1 -> {
                // Section 1: Content & Feed Moderation (পোস্ট, উত্তর ও মন্তব্য নিয়ন্ত্রণ)
                item {
                    OutlinedTextField(
                        value = postSearchQuery,
                        onValueChange = { postSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Filter posts or questions by content or author...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandPurple,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                val filteredPosts = allPosts.filter {
                    postSearchQuery.isBlank() ||
                            it.content.contains(postSearchQuery, ignoreCase = true) ||
                            it.author.contains(postSearchQuery, ignoreCase = true)
                }

                if (filteredPosts.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No posts found matching \"$postSearchQuery\"",
                                modifier = Modifier.padding(24.dp),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        }
                    }
                } else {
                    items(filteredPosts, key = { it.id }) { post ->
                        AdminPostModerationCard(
                            post = post,
                            onDelete = { onDeletePost(post.id) },
                            onTogglePin = { onTogglePinPost(post.id) },
                            onDeleteAnswer = { ansId -> onDeleteAnswer(post.id, ansId) },
                            onDeleteComment = { commId -> onDeleteComment(post.id, commId) }
                        )
                    }
                }
            }

            2 -> {
                // Section 2: Security & OTP Center (ভেরিফিকেশন কোড নিয়ন্ত্রণ ও স্টিলথ মোড)
                item {
                    AdminSecurityAndOtpCenter(
                        adminStealthMode = adminStealthMode,
                        strictRegistrationMode = strictRegistrationMode,
                        verificationRecords = verificationRecords,
                        onToggleAdminStealth = onToggleAdminStealth,
                        onToggleStrictRegistration = onToggleStrictRegistration,
                        onGenerateCode = onManualGenerateCode
                    )
                }
            }

            3 -> {
                // Section 3: Notice Broadcast (জরুরি নোটিশ ব্রডকাস্ট)
                item {
                    AdminNoticeBroadcastComposer(
                        onBroadcast = onBroadcastNotice
                    )
                }

                item {
                    Text(
                        text = "Active Broadcast Notices (${systemNotices.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (systemNotices.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No active notices broadcasted yet. Use the composer above to broadcast an official announcement to all users.",
                                modifier = Modifier.padding(20.dp),
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        }
                    }
                } else {
                    items(systemNotices, key = { it.id }) { notice ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = BrandPink.copy(alpha = 0.15f)
                                    ) {
                                        Icon(
                                            Icons.Default.Campaign,
                                            contentDescription = null,
                                            tint = BrandPink,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .size(20.dp)
                                        )
                                    }
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = notice.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = notice.message,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 17.sp
                                        )
                                        Text(
                                            text = "Broadcasted • ${notice.timestamp}",
                                            fontSize = 10.sp,
                                            color = Slate400
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { onDeleteNotice(notice.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Remove notice", tint = Color.Red, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminKpiBadge(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = color)
            Text(text = title, fontSize = 10.sp, color = Slate400, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun AdminUserControlCard(
    user: UserProfile,
    isSelf: Boolean,
    onToggleSuspend: () -> Unit,
    onChangeRole: (UserRole) -> Unit,
    onToggleVerify: () -> Unit,
    onResetPassword: (String) -> Unit,
    onDelete: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showResetPasswordDialog by remember { mutableStateOf(false) }
    var newPasswordInput by remember { mutableStateOf("") }
    var showPasswordPlain by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (user.isSuspended) Color.Red.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        ),
        border = if (user.isSuspended) androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_user_card_${user.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Avatar, User Details, Role Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    UserAvatar(
                        avatarUrl = user.avatarUrl,
                        name = user.name,
                        size = 44.dp,
                        isOnline = !user.isSuspended
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = user.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelf) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BrandPurple.copy(alpha = 0.15f)
                                ) {
                                    Text("You (Admin)", color = BrandPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                        Text(text = "${user.email.ifBlank { "No email" }} • ${user.phone.ifBlank { "No phone" }}", fontSize = 11.sp, color = Slate400)
                        
                        if (user.work.isNotBlank() || user.college.isNotBlank()) {
                            Text(
                                text = listOfNotNull(
                                    user.gender.takeIf { it.isNotBlank() },
                                    user.work.takeIf { it.isNotBlank() },
                                    user.college.takeIf { it.isNotBlank() }
                                ).joinToString(" • "),
                                fontSize = 10.sp,
                                color = BrandIndigoPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Status & Verification Badge
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (user.isSuspended) Color.Red.copy(alpha = 0.15f) else BrandEmerald.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (user.isSuspended) "SUSPENDED" else "ACTIVE",
                            color = if (user.isSuspended) Color.Red else BrandEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    if (user.isVerified) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandIndigoPrimary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "Code Verified ✓",
                                color = BrandIndigoPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Member Info & Password Row (Admin has full password visibility & override)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = Slate400, modifier = Modifier.size(13.dp))
                        Text(
                            text = "Password: " + if (showPasswordPlain) user.password else "••••••••",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                        IconButton(
                            onClick = { showPasswordPlain = !showPasswordPlain },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (showPasswordPlain) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password",
                                tint = Slate400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    TextButton(
                        onClick = { showResetPasswordDialog = true },
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.LockReset, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Pass", fontSize = 11.sp, color = BrandPurple, fontWeight = FontWeight.Bold)
                    }
                }
            }

            HorizontalDivider()

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Role Selector Button
                Box {
                    OutlinedButton(
                        onClick = { if (!isSelf) showRoleMenu = true },
                        enabled = !isSelf,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp), tint = BrandIndigoPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(user.userRole.displayName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Super Admin") },
                            onClick = {
                                onChangeRole(UserRole.SUPER_ADMIN)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Admin") },
                            onClick = {
                                onChangeRole(UserRole.ADMIN)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Moderator") },
                            onClick = {
                                onChangeRole(UserRole.MODERATOR)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Regular Member") },
                            onClick = {
                                onChangeRole(UserRole.MEMBER)
                                showRoleMenu = false
                            }
                        )
                    }
                }

                // Verify / Revoke Verification Button
                OutlinedButton(
                    onClick = onToggleVerify,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (user.isVerified) BrandEmerald else Slate400
                    )
                ) {
                    Icon(
                        imageVector = if (user.isVerified) Icons.Default.Verified else Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (user.isVerified) "Verified" else "Verify",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Suspend / Unsuspend & Delete
                if (!isSelf) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = onToggleSuspend,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user.isSuspended) BrandEmerald else Color.Red
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = if (user.isSuspended) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (user.isSuspended) "Unsuspend" else "Suspend",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = { showDeleteConfirm = true },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete user", tint = Color.Red, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }

    if (showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showResetPasswordDialog = false },
            title = { Text("Reset Password for ${user.name}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("As Super Admin, enter a new password for this member:")
                    OutlinedTextField(
                        value = newPasswordInput,
                        onValueChange = { newPasswordInput = it },
                        placeholder = { Text("Enter new password...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPasswordInput.isNotBlank()) {
                            onResetPassword(newPasswordInput.trim())
                            showResetPasswordDialog = false
                            newPasswordInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) {
                    Text("Save Password", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete User Account?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to completely remove ${user.name} (${user.email}) from Ask ME? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Account", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminPostModerationCard(
    post: Post,
    onDelete: () -> Unit,
    onTogglePin: () -> Unit,
    onDeleteAnswer: (String) -> Unit = {},
    onDeleteComment: (String) -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(avatarUrl = post.avatarUrl, name = post.author, size = 32.dp)
                    Column {
                        Text(text = post.author, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "${post.timeAgo} • ${post.privacy}", fontSize = 10.sp, color = Slate400)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (post.type == PostType.QUESTION) BrandPurpleLight else BrandIndigoLight
                    ) {
                        Text(
                            text = if (post.type == PostType.QUESTION) "❓ Question" else "Standard Post",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (post.type == PostType.QUESTION) BrandPurple else BrandIndigoPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (post.isPinned) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandAmber.copy(alpha = 0.2f)
                        ) {
                            Text("PINNED", color = BrandAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            Text(
                text = post.content,
                fontSize = 13.sp,
                maxLines = if (showDetails) 10 else 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            // Show answers & comments moderation list if expanded
            if (showDetails) {
                if (post.answers.isNotEmpty()) {
                    Text("Answers (${post.answers.size}):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandPurple)
                    post.answers.forEach { ans ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = ans.author, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(text = ans.text, fontSize = 11.sp, color = Slate400)
                                }
                                IconButton(
                                    onClick = { onDeleteAnswer(ans.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete answer", tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                if (post.comments.isNotEmpty()) {
                    Text("Comments (${post.comments.size}):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandIndigoPrimary)
                    post.comments.forEach { comm ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = comm.author, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(text = comm.text, fontSize = 11.sp, color = Slate400)
                                }
                                IconButton(
                                    onClick = { onDeleteComment(comm.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete comment", tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { showDetails = !showDetails },
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "${post.reactionsCount} reactions • ${post.answers.size} answers • ${post.comments.size} comments" + if (showDetails) " ▲" else " ▼",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onTogglePin,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin,
                            contentDescription = "Pin post",
                            tint = if (post.isPinned) BrandAmber else Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete post", tint = Color.Red, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Post as Admin?", fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently remove this post and all its answers/comments from the Ask ME platform.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Post", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminSecurityAndOtpCenter(
    adminStealthMode: Boolean,
    strictRegistrationMode: Boolean,
    verificationRecords: List<VerificationRecord>,
    onToggleAdminStealth: () -> Unit,
    onToggleStrictRegistration: () -> Unit,
    onGenerateCode: (String) -> Unit
) {
    var targetInput by remember { mutableStateOf("") }
    var lastGeneratedCode by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // 1. Admin Stealth Mode & ID Concealment
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BrandPurple.copy(alpha = 0.08f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = BrandPurple)
                        Column {
                            Text("Admin Stealth Mode (আইডি গোপনীয়তা)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = if (adminStealthMode) "HIDDEN: askman36@gmail.com is concealed" else "VISIBLE to public",
                                fontSize = 11.sp,
                                color = if (adminStealthMode) BrandEmerald else Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Switch(
                        checked = adminStealthMode,
                        onCheckedChange = { onToggleAdminStealth() },
                        colors = SwitchDefaults.colors(checkedThumbColor = BrandPurple, checkedTrackColor = BrandPurpleLight)
                    )
                }

                Text(
                    text = "When Stealth Mode is ON, regular members cannot see the Super Admin profile in user directories, public search, or quick-switch dialogs. Admin interactions on the community feed appear masked under 'Ask ME Official'.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        // 2. Strict 1-Minute Verification Rule
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BrandIndigoLight.copy(alpha = 0.4f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandIndigoPrimary.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandIndigoPrimary)
                        Column {
                            Text("1-Minute Admin Code Requirement", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = if (strictRegistrationMode) "STRICT: 60-Sec Countdown Active" else "OPTIONAL",
                                fontSize = 11.sp,
                                color = BrandIndigoPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Switch(
                        checked = strictRegistrationMode,
                        onCheckedChange = { onToggleStrictRegistration() },
                        colors = SwitchDefaults.colors(checkedThumbColor = BrandIndigoPrimary, checkedTrackColor = BrandIndigoLight)
                    )
                }

                Text(
                    text = "Requires newly registering members to supply Mobile Number/Gmail and enter the 6-digit verification code within 1 minute (60 seconds) before account activation.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        // 3. Instant VIP Code Generator
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Generate Instant Activation Code", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("As Super Admin, you can manually generate and dispatch a 6-digit VIP activation code for any member mobile or Gmail:", fontSize = 11.sp, color = Slate400)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        placeholder = { Text("Gmail or Phone (+880...)", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (targetInput.isNotBlank()) {
                                val code = (100000..999999).random().toString()
                                lastGeneratedCode = code
                                onGenerateCode(targetInput.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Issue Code", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (lastGeneratedCode != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BrandEmerald.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Generated Code: $lastGeneratedCode (Valid for 1 min)", color = BrandEmerald, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Icon(Icons.Default.Verified, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // 4. Real-time Verification Code Log Table
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dispatched Verification Logs", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BrandPurple.copy(alpha = 0.12f)
                    ) {
                        Text("${verificationRecords.size} Records", color = BrandPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                if (verificationRecords.isEmpty()) {
                    Text("No verification codes issued yet.", fontSize = 12.sp, color = Slate400)
                } else {
                    verificationRecords.take(10).forEach { record ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = record.target, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(text = "Code: ${record.code} • ${record.timestamp}", fontSize = 10.sp, color = Slate400)
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BrandEmerald.copy(alpha = 0.15f)
                                ) {
                                    Text("VERIFIED ✓", color = BrandEmerald, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminNoticeBroadcastComposer(
    onBroadcast: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = BrandPink.copy(alpha = 0.15f)
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = BrandPink, modifier = Modifier.padding(6.dp).size(18.dp))
                }
                Text("Broadcast Platform Announcement", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Notice Title", fontSize = 12.sp) },
                placeholder = { Text("e.g. Scheduled System Maintenance / New Feature Alert") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Notice Message", fontSize = 12.sp) },
                placeholder = { Text("Write the detailed message that all community members will receive...") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (showSuccess) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandEmerald.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Announcement broadcasted successfully to all users!",
                        color = BrandEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (title.isNotBlank() && message.isNotBlank()) {
                        onBroadcast(title.trim(), message.trim())
                        title = ""
                        message = ""
                        showSuccess = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPink),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Broadcast Notice", fontWeight = FontWeight.Bold)
            }
        }
    }
}
