package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Shield
import com.example.model.UserRole
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.AppNotification
import com.example.model.AppTab
import com.example.model.ChatMessage
import com.example.model.Friend
import com.example.model.UserProfile
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandIndigoSecondary
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun UserAvatar(
    avatarUrl: String?,
    name: String,
    size: Dp = 40.dp,
    isOnline: Boolean? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "$name avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .border(1.5.dp, BrandIndigoSecondary, CircleShape)
            )
        } else {
            val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(BrandIndigoPrimary, BrandPurple)
                        )
                    )
                    .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifBlank { "?" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.4f).sp
                )
            }
        }

        if (isOnline == true) {
            Box(
                modifier = Modifier
                    .size((size.value * 0.3f).coerceAtLeast(8f).dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(BrandEmerald)
                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Composable
fun AskMeLogo(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(BrandIndigoPrimary, BrandPurple, BrandPink)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "?",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
        }
        Column {
            Text(
                text = "Ask ME",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = BrandIndigoSecondary
            )
        }
    }
}

@Composable
fun TopNavBarAskMe(
    user: UserProfile,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    unreadNotificationsCount: Int,
    onNotificationsClick: () -> Unit,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onLogoClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAdminClick: (() -> Unit)? = null,
    onAccountsClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var searchExpanded by remember { mutableStateOf(false) }
    val isAdmin = user.userRole == UserRole.SUPER_ADMIN || user.userRole == UserRole.ADMIN

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo
                AskMeLogo(onClick = onLogoClick)

                // Search Bar in center (compact or expand toggle on small screens)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { searchExpanded = !searchExpanded },
                        modifier = Modifier.testTag("search_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = if (searchExpanded) BrandIndigoPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Admin Quick Shield Button (Visible for Super Admin / Admin)
                    if (isAdmin && onAdminClick != null) {
                        IconButton(
                            onClick = onAdminClick,
                            modifier = Modifier.testTag("admin_panel_top_button")
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandPurple.copy(alpha = 0.15f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Admin Control",
                                    tint = BrandPurple,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(18.dp)
                                )
                            }
                        }
                    }

                    // Account Switcher Button
                    if (onAccountsClick != null) {
                        IconButton(
                            onClick = onAccountsClick,
                            modifier = Modifier.testTag("accounts_switch_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = "Accounts & Identity",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Notifications Button with badge
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.testTag("notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationsCount > 0) {
                                    Badge(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = if (unreadNotificationsCount > 9) "9+" else unreadNotificationsCount.toString(),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Dark/Light Theme toggle
                    IconButton(
                        onClick = onToggleDarkTheme,
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Profile Avatar
                    UserAvatar(
                        avatarUrl = user.avatarUrl,
                        name = user.name,
                        size = 32.dp,
                        isOnline = !user.isSuspended,
                        onClick = onProfileClick,
                        modifier = Modifier.testTag("profile_avatar_top")
                    )
                }
            }

            AnimatedVisibility(visible = searchExpanded) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .testTag("global_search_input"),
                    placeholder = { Text("Search posts, Q&A topics, people...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandIndigoSecondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                )
            }
        }
    }
}

@Composable
fun BottomNavBarAskMe(
    activeTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = activeTab == AppTab.FEED,
            onClick = { onTabSelected(AppTab.FEED) },
            icon = {
                Icon(
                    imageVector = if (activeTab == AppTab.FEED) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Feed"
                )
            },
            label = {
                Text(
                    text = "Feed",
                    fontWeight = if (activeTab == AppTab.FEED) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandIndigoPrimary,
                selectedTextColor = BrandIndigoPrimary,
                indicatorColor = BrandIndigoLight
            ),
            modifier = Modifier.testTag("nav_feed_item")
        )

        NavigationBarItem(
            selected = activeTab == AppTab.FRIENDS,
            onClick = { onTabSelected(AppTab.FRIENDS) },
            icon = {
                Icon(
                    imageVector = if (activeTab == AppTab.FRIENDS) Icons.Filled.People else Icons.Outlined.People,
                    contentDescription = "Friends"
                )
            },
            label = {
                Text(
                    text = "Friends",
                    fontWeight = if (activeTab == AppTab.FRIENDS) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandIndigoPrimary,
                selectedTextColor = BrandIndigoPrimary,
                indicatorColor = BrandIndigoLight
            ),
            modifier = Modifier.testTag("nav_friends_item")
        )

        NavigationBarItem(
            selected = activeTab == AppTab.MESSAGES,
            onClick = { onTabSelected(AppTab.MESSAGES) },
            icon = {
                Icon(
                    imageVector = if (activeTab == AppTab.MESSAGES) Icons.Filled.Message else Icons.Outlined.Message,
                    contentDescription = "Chats"
                )
            },
            label = {
                Text(
                    text = "Chats",
                    fontWeight = if (activeTab == AppTab.MESSAGES) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandIndigoPrimary,
                selectedTextColor = BrandIndigoPrimary,
                indicatorColor = BrandIndigoLight
            ),
            modifier = Modifier.testTag("nav_messages_item")
        )

        NavigationBarItem(
            selected = activeTab == AppTab.PRO,
            onClick = { onTabSelected(AppTab.PRO) },
            icon = {
                Icon(
                    imageVector = if (activeTab == AppTab.PRO) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                    contentDescription = "Pro Dashboard"
                )
            },
            label = {
                Text(
                    text = "Pro",
                    fontWeight = if (activeTab == AppTab.PRO) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPurple,
                selectedTextColor = BrandPurple,
                indicatorColor = BrandPurpleLight
            ),
            modifier = Modifier.testTag("nav_pro_item")
        )

        NavigationBarItem(
            selected = activeTab == AppTab.SAVED,
            onClick = { onTabSelected(AppTab.SAVED) },
            icon = {
                Icon(
                    imageVector = if (activeTab == AppTab.SAVED) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved"
                )
            },
            label = {
                Text(
                    text = "Saved",
                    fontWeight = if (activeTab == AppTab.SAVED) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    softWrap = false
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandIndigoPrimary,
                selectedTextColor = BrandIndigoPrimary,
                indicatorColor = BrandIndigoLight
            ),
            modifier = Modifier.testTag("nav_saved_item")
        )

        if (isAdmin) {
            NavigationBarItem(
                selected = activeTab == AppTab.ADMIN,
                onClick = { onTabSelected(AppTab.ADMIN) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Admin Control"
                    )
                },
                label = {
                    Text(
                        text = "Admin",
                        fontWeight = if (activeTab == AppTab.ADMIN) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandPurple,
                    selectedTextColor = BrandPurple,
                    indicatorColor = BrandPurpleLight
                ),
                modifier = Modifier.testTag("nav_admin_item")
            )
        }
    }
}

@Composable
fun NotificationsDialog(
    notifications: List<AppNotification>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp)
                .padding(8.dp)
                .testTag("notifications_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark all read", fontSize = 12.sp, color = BrandIndigoSecondary, fontWeight = FontWeight.SemiBold)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (notifications.isEmpty()) {
                    Text(
                        text = "No notifications yet",
                        color = Slate400,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.height(260.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notifications) { notif ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (notif.isUnread) BrandIndigoLight.copy(alpha = 0.5f) else Color.Transparent
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.text,
                                        fontSize = 13.sp,
                                        fontWeight = if (notif.isUnread) FontWeight.Bold else FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = notif.timeAgo,
                                        fontSize = 11.sp,
                                        color = Slate400,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                                if (notif.isUnread) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(BrandIndigoPrimary)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close", color = BrandIndigoPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InteractiveChatDialog(
    friend: Friend,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textState by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.75f)
                .widthIn(max = 480.dp)
                .testTag("chat_dialog")
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Chat Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandIndigoPrimary)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        UserAvatar(
                            avatarUrl = friend.avatarUrl,
                            name = friend.name,
                            size = 36.dp,
                            isOnline = friend.isOnline
                        )
                        Column {
                            Text(
                                text = friend.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (friend.isOnline) "Active Now" else "Offline",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close chat", tint = Color.White)
                    }
                }

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 280.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (msg.isFromMe) 16.dp else 4.dp,
                                            bottomEnd = if (msg.isFromMe) 4.dp else 16.dp
                                        )
                                    )
                                    .background(
                                        if (msg.isFromMe) BrandIndigoPrimary else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = msg.text,
                                        fontSize = 13.sp,
                                        color = if (msg.isFromMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = msg.timestamp,
                                        fontSize = 9.sp,
                                        color = if (msg.isFromMe) Color.White.copy(alpha = 0.7f) else Slate400,
                                        modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Chat Input Field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = textState,
                        onValueChange = { textState = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_text_input"),
                        placeholder = { Text("Write a message...", fontSize = 13.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (textState.isNotBlank()) {
                                onSendMessage(textState)
                                textState = ""
                            }
                        }),
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )

                    IconButton(
                        onClick = {
                            if (textState.isNotBlank()) {
                                onSendMessage(textState)
                                textState = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(BrandIndigoPrimary)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
