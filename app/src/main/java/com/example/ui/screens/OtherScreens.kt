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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.Friend
import com.example.model.Post
import com.example.model.PostType
import com.example.model.UserProfile
import com.example.model.UserRole
import androidx.compose.material.icons.filled.ManageAccounts
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandIndigoSecondary
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700

@Composable
fun FriendsScreen(
    friends: List<Friend>,
    onMessageFriend: (Friend) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("friends_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Friends & Connections",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Connect, collaborate, and exchange professional insights with peers.",
                        fontSize = 12.sp,
                        color = Slate400,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Active Now Banner
        item {
            val onlineCount = friends.count { it.isOnline }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BrandEmerald)
                )
                Text(
                    text = "ACTIVE CONTACTS ($onlineCount Online)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandEmerald,
                    letterSpacing = 0.5.sp
                )
            }
        }

        items(friends, key = { it.id }) { friend ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        UserAvatar(
                            avatarUrl = friend.avatarUrl,
                            name = friend.name,
                            size = 46.dp,
                            isOnline = friend.isOnline
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = friend.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (friend.isOnline) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandEmerald.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "Online",
                                            color = BrandEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = friend.status,
                                fontSize = 12.sp,
                                color = Slate400,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Button(
                        onClick = { onMessageFriend(friend) },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigoPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("message_friend_${friend.id}")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Message", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MessagesScreen(
    friends: List<Friend>,
    chatMessages: Map<String, List<ChatMessage>>,
    onOpenChat: (Friend) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("messages_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Messages Inbox",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Direct real-time conversations with your peers and collaborators.",
                        fontSize = 12.sp,
                        color = Slate400,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        items(friends, key = { it.id }) { friend ->
            val lastMsg = chatMessages[friend.id]?.lastOrNull()
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChat(friend) }
                    .testTag("inbox_item_${friend.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UserAvatar(
                        avatarUrl = friend.avatarUrl,
                        name = friend.name,
                        size = 46.dp,
                        isOnline = friend.isOnline
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = friend.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = lastMsg?.timestamp ?: if (friend.isOnline) "Active" else "Offline",
                                fontSize = 10.sp,
                                color = Slate400
                            )
                        }
                        Text(
                            text = lastMsg?.text ?: friend.status,
                            fontSize = 12.sp,
                            color = if (lastMsg != null) MaterialTheme.colorScheme.onSurfaceVariant else Slate400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedScreen(
    savedPosts: List<Post>,
    onRemoveSaved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("saved_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Saved Bookmarks",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Access your bookmarked questions, answers, and posts anytime.",
                        fontSize = 12.sp,
                        color = Slate400,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        if (savedPosts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = BrandAmber,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No saved posts yet",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tap the bookmark icon on any post to save it here for quick reference!",
                            fontSize = 12.sp,
                            color = Slate400
                        )
                    }
                }
            }
        } else {
            items(savedPosts, key = { it.id }) { post ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
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
                            Text(
                                text = post.content,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "By ${post.author} • ${post.timeAgo}",
                                fontSize = 11.sp,
                                color = Slate400,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        TextButton(onClick = { onRemoveSaved(post.id) }) {
                            Text("Remove", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDialog(
    user: UserProfile,
    onDismiss: () -> Unit,
    onOpenAccountSwitcher: (() -> Unit)? = null,
    onOpenAdminPanel: (() -> Unit)? = null
) {
    val isSuperAdmin = user.userRole == UserRole.SUPER_ADMIN
    val isAdmin = user.userRole == UserRole.ADMIN || isSuperAdmin

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UserAvatar(
                    avatarUrl = user.avatarUrl,
                    name = user.name,
                    size = 72.dp,
                    isOnline = true
                )

                Text(
                    text = if (isSuperAdmin) "Ask ME Administrator" else user.name,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isSuperAdmin) "🛡️ Stealth Mode (Admin ID Protected)" else user.email,
                    fontSize = 12.sp,
                    color = if (isSuperAdmin) BrandPurple else Slate400,
                    fontWeight = if (isSuperAdmin) FontWeight.Bold else FontWeight.Normal
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (user.userRole) {
                        UserRole.SUPER_ADMIN -> BrandPurpleLight
                        UserRole.ADMIN, UserRole.MODERATOR -> BrandAmber.copy(alpha = 0.2f)
                        else -> BrandIndigoLight
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAdmin) Icons.Default.Shield else Icons.Default.Verified,
                            contentDescription = null,
                            tint = when (user.userRole) {
                                UserRole.SUPER_ADMIN -> BrandPurple
                                UserRole.ADMIN, UserRole.MODERATOR -> BrandAmber
                                else -> BrandIndigoPrimary
                            },
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = when (user.userRole) {
                                UserRole.SUPER_ADMIN -> "Platform Super Admin (Main Owner)"
                                UserRole.ADMIN -> "Community Admin"
                                UserRole.MODERATOR -> "Moderator"
                                else -> user.role
                            },
                            color = when (user.userRole) {
                                UserRole.SUPER_ADMIN -> BrandPurple
                                UserRole.ADMIN, UserRole.MODERATOR -> BrandAmber
                                else -> BrandIndigoPrimary
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                if (user.isVerified) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrandEmerald.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(12.dp))
                            Text("Admin Code Verified Member", color = BrandEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Member Personal Criteria Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (user.work.isNotBlank()) {
                            Text("💼 Work: ${user.work}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        if (user.college.isNotBlank()) {
                            Text("🎓 College: ${user.college}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        if (user.gender.isNotBlank()) {
                            Text("👤 Gender: ${user.gender}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        if (user.phone.isNotBlank()) {
                            Text("📱 Mobile: ${user.phone}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${user.answersGiven}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Answers", fontSize = 11.sp, color = Slate400)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${user.reputation}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Reputation", fontSize = 11.sp, color = Slate400)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${user.netFollowers}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Followers", fontSize = 11.sp, color = Slate400)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (isAdmin && onOpenAdminPanel != null) {
                    Button(
                        onClick = {
                            onDismiss()
                            onOpenAdminPanel()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Admin Control Panel", fontWeight = FontWeight.Bold)
                    }
                }

                if (onOpenAccountSwitcher != null) {
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onOpenAccountSwitcher()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ManageAccounts, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Switch or Register Account", fontWeight = FontWeight.SemiBold)
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = Slate400)
                }
            }
        }
    }
}
