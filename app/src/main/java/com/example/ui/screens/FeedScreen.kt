package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Warning
import com.example.model.SystemNotice
import com.example.model.UserRole
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbUp
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Answer
import com.example.model.FeedFilter
import com.example.model.Post
import com.example.model.PostType
import com.example.model.UserProfile
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandIndigoSecondary
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700

@Composable
fun FeedScreen(
    user: UserProfile,
    posts: List<Post>,
    selectedFilter: FeedFilter,
    onFilterSelect: (FeedFilter) -> Unit,
    onCreatePost: (content: String, imageUrl: String?, type: PostType, privacy: String) -> Unit,
    onToggleReaction: (String) -> Unit,
    onToggleSave: (String) -> Unit,
    onAddAnswer: (postId: String, answerText: String) -> Unit,
    onUpvoteAnswer: (postId: String, answerId: String) -> Unit,
    onAddComment: (postId: String, commentText: String) -> Unit,
    systemNotices: List<SystemNotice> = emptyList(),
    onDeletePost: ((String) -> Unit)? = null,
    onOpenAuth: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAnnouncementDismissed by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("feed_screen_column"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Warning Banner if User is Suspended
        if (user.isSuspended) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                        Column {
                            Text("Account Suspended by Admin", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 14.sp)
                            Text("Your account privileges have been suspended. Posting and answering are currently disabled.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Active Platform Announcement from Admin
        if (systemNotices.isNotEmpty()) {
            item {
                val latestNotice = systemNotices.first()
                if (!isAnnouncementDismissed) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandPurple.copy(alpha = 0.08f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandPurple
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.padding(6.dp).size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("ADMIN ANNOUNCEMENT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = BrandPurple)
                                        Text("• ${latestNotice.timestamp}", fontSize = 10.sp, color = Slate400)
                                    }
                                    IconButton(
                                        onClick = { isAnnouncementDismissed = true },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss announcement",
                                            tint = BrandPurple.copy(alpha = 0.8f),
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                                Text(latestNotice.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(latestNotice.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
                            }
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BrandPurple.copy(alpha = 0.08f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isAnnouncementDismissed = false }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(14.dp))
                                Text("📢 Notice from Admin: ${latestNotice.title}", fontSize = 11.sp, color = BrandPurple, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                            Text("Show", fontSize = 10.sp, color = BrandPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 1. Post Composer Card
        item {
            PostComposerCard(
                user = user,
                onPostCreated = onCreatePost
            )
        }

        // 2. Filter Tabs Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = selectedFilter == FeedFilter.ALL,
                            onClick = { onFilterSelect(FeedFilter.ALL) },
                            label = { Text("All Posts", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandIndigoPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("filter_all_chip")
                        )

                        FilterChip(
                            selected = selectedFilter == FeedFilter.QUESTIONS,
                            onClick = { onFilterSelect(FeedFilter.QUESTIONS) },
                            label = { Text("❓ Questions", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPurple,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("filter_questions_chip")
                        )

                        FilterChip(
                            selected = selectedFilter == FeedFilter.MEDIA,
                            onClick = { onFilterSelect(FeedFilter.MEDIA) },
                            label = { Text("📷 Photos", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandIndigoPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("filter_media_chip")
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Showing ${posts.size}",
                        fontSize = 11.sp,
                        color = Slate400,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }

        // 3. Posts Stream
        if (posts.isEmpty()) {
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
                        Text(
                            text = "No posts found in this section",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate400
                        )
                        Text(
                            text = "Try clearing search or posting a question!",
                            fontSize = 12.sp,
                            color = Slate400
                        )
                    }
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    user = user,
                    onToggleReaction = { onToggleReaction(post.id) },
                    onToggleSave = { onToggleSave(post.id) },
                    onAddAnswer = { text -> onAddAnswer(post.id, text) },
                    onUpvoteAnswer = { ansId -> onUpvoteAnswer(post.id, ansId) },
                    onAddComment = { text -> onAddComment(post.id, text) },
                    onDeletePost = if (onDeletePost != null) { { onDeletePost(post.id) } } else null,
                    onShare = {
                        Toast.makeText(context, "Post link copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun PostComposerCard(
    user: UserProfile,
    onPostCreated: (content: String, imageUrl: String?, type: PostType, privacy: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var contentText by remember { mutableStateOf("") }
    var imageUrlText by remember { mutableStateOf("") }
    var postType by remember { mutableStateOf(PostType.STANDARD) }
    var privacy by remember { mutableStateOf("Public") }
    var isExpanded by remember { mutableStateOf(false) }
    var showImageInput by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("post_composer_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UserAvatar(avatarUrl = user.avatarUrl, name = user.name, size = 42.dp)
                OutlinedTextField(
                    value = contentText,
                    onValueChange = {
                        contentText = it
                        if (it.isNotEmpty() && !isExpanded) isExpanded = true
                    },
                    placeholder = {
                        Text(
                            text = if (postType == PostType.QUESTION) "Ask community a question..." else "Ask a question or share thoughts...",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { if (it.isFocused) isExpanded = true }
                        .testTag("composer_text_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (postType == PostType.QUESTION) BrandPurple else BrandIndigoSecondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    maxLines = 4
                )
                if (isExpanded) {
                    IconButton(
                        onClick = {
                            isExpanded = false
                            showImageInput = false
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Collapse", tint = Slate400)
                    }
                }
            }

            // Expanded Options
            AnimatedVisibility(visible = isExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Image URL Input Field if toggled
                    AnimatedVisibility(visible = showImageInput) {
                        OutlinedTextField(
                            value = imageUrlText,
                            onValueChange = { imageUrlText = it },
                            placeholder = { Text("Paste image URL (Unsplash, HTTPS link)...", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("composer_image_url_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    imageUrlText = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&auto=format&fit=crop&q=80"
                                }) {
                                    Text("Demo", fontSize = 10.sp, color = BrandIndigoSecondary, fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }

                    // Post Type & Privacy Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Post Type Switcher
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = postType == PostType.STANDARD,
                                onClick = { postType = PostType.STANDARD },
                                label = { Text("Standard", fontSize = 11.sp) },
                                shape = RoundedCornerShape(8.dp)
                            )
                            FilterChip(
                                selected = postType == PostType.QUESTION,
                                onClick = { postType = PostType.QUESTION },
                                label = { Text("❓ Ask ME", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandPurple,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        // Privacy selector chips
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("Public", "Friends", "Only Me").forEach { p ->
                                val isSelected = privacy == p
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) BrandIndigoLight else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clickable { privacy = p }
                                ) {
                                    Text(
                                        text = when(p) {
                                            "Public" -> "🌐"
                                            "Friends" -> "👥"
                                            else -> "🔒"
                                        },
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))

            // Action bar: Add photo, Ask Question, and Post submit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(
                        onClick = {
                            isExpanded = true
                            showImageInput = true
                        }
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Photo", fontSize = 12.sp, color = BrandEmerald, fontWeight = FontWeight.SemiBold)
                    }

                    TextButton(
                        onClick = {
                            isExpanded = true
                            postType = PostType.QUESTION
                        }
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ask Question", fontSize = 12.sp, color = BrandPurple, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = {
                        if (!user.isSuspended && contentText.isNotBlank()) {
                            onPostCreated(contentText, imageUrlText, postType, privacy)
                            contentText = ""
                            imageUrlText = ""
                            isExpanded = false
                            showImageInput = false
                        }
                    },
                    enabled = !user.isSuspended && contentText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (user.isSuspended) Color.Gray else if (postType == PostType.QUESTION) BrandPurple else BrandIndigoPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("post_submit_button")
                ) {
                    Text(if (user.isSuspended) "Suspended" else "Post", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun PostItemCard(
    post: Post,
    user: UserProfile,
    onToggleReaction: () -> Unit,
    onToggleSave: () -> Unit,
    onAddAnswer: (String) -> Unit,
    onUpvoteAnswer: (String) -> Unit,
    onAddComment: (String) -> Unit,
    onDeletePost: (() -> Unit)? = null,
    onShare: () -> Unit
) {
    var showComments by remember { mutableStateOf(false) }
    var answerInputText by remember { mutableStateOf("") }
    var commentInputText by remember { mutableStateOf("") }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val isQuestion = post.type == PostType.QUESTION
    val canDelete = (user.userRole == UserRole.SUPER_ADMIN || user.userRole == UserRole.ADMIN || post.author == user.name) && onDeletePost != null

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Post Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(avatarUrl = post.avatarUrl, name = post.author, size = 42.dp)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = post.author,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isQuestion) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BrandPurpleLight
                                ) {
                                    Text(
                                        text = "❓ Ask ME Question",
                                        color = BrandPurple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = post.timeAgo, fontSize = 11.sp, color = Slate400)
                            Text(text = "•", fontSize = 11.sp, color = Slate400)
                            Text(
                                text = when (post.privacy) {
                                    "Public" -> "🌐 Public"
                                    "Friends" -> "👥 Friends"
                                    else -> "🔒 Only Me"
                                },
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.isPinned) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandAmber.copy(alpha = 0.2f),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(Icons.Default.PushPin, contentDescription = null, tint = BrandAmber, modifier = Modifier.size(12.dp))
                                Text("PINNED", color = BrandAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (post.isSaved) BrandAmber else Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { showOptionsMenu = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Slate400,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Share Post") },
                                onClick = {
                                    showOptionsMenu = false
                                    onShare()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Share, contentDescription = null, tint = BrandIndigoPrimary)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(if (post.isSaved) "Remove from Saved" else "Save to Bookmarks") },
                                onClick = {
                                    showOptionsMenu = false
                                    onToggleSave()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = null,
                                        tint = if (post.isSaved) BrandAmber else Slate400
                                    )
                                }
                            )

                            if (canDelete) {
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Delete Post", color = Color.Red, fontWeight = FontWeight.SemiBold) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showDeleteConfirmation = true
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color.Red)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 2. Post Content
            Text(
                text = post.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 3. Post Image (if present)
            if (!post.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Post image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
            }

            // 4. "Ask ME" Specific Community Answers Box
            if (isQuestion) {
                var showAnswersExpanded by rememberSaveable { mutableStateOf(post.answers.isNotEmpty()) }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandPurpleLight.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showAnswersExpanded = !showAnswersExpanded }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.QuestionAnswer,
                                    contentDescription = null,
                                    tint = BrandPurple,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Answers (${post.answers.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BrandPurple
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = if (showAnswersExpanded) "Hide ▲" else "View / Answer (${post.answers.size}) ▼",
                                    fontSize = 11.sp,
                                    color = BrandPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        AnimatedVisibility(visible = showAnswersExpanded) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // List of answers
                                post.answers.forEach { ans ->
                                    Card(
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                UserAvatar(avatarUrl = ans.avatarUrl, name = ans.author, size = 30.dp)
                                                Column {
                                                    Text(
                                                        text = ans.author,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = ans.text,
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                            // Upvote Button
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (ans.isUpvoted) BrandPurpleLight else MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .clickable { onUpvoteAnswer(ans.id) }
                                                    .padding(start = 6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.KeyboardArrowUp,
                                                        contentDescription = "Upvote",
                                                        tint = if (ans.isUpvoted) BrandPurple else Slate400,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = "${ans.upvotes}",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (ans.isUpvoted) BrandPurple else Slate400
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Answer input
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    TextField(
                                        value = answerInputText,
                                        onValueChange = { answerInputText = it },
                                        placeholder = { Text("Write an answer...", fontSize = 12.sp) },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            if (answerInputText.isNotBlank()) {
                                                onAddAnswer(answerInputText)
                                                answerInputText = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Answer", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(BrandIndigoSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👍", fontSize = 9.sp)
                    }
                    Text(
                        text = "${post.reactionsCount} Reactions",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Text(
                    text = "${post.comments.size} Comments",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            HorizontalDivider()

            // 6. Action Bar: Like, Comment, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = onToggleReaction) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        tint = if (post.isLiked) BrandIndigoPrimary else Slate400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (post.isLiked) "Liked" else "Like",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (post.isLiked) BrandIndigoPrimary else Slate400
                    )
                }

                TextButton(onClick = { showComments = !showComments }) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = Slate400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Comment",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400
                    )
                }

                TextButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Slate400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Share",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400
                    )
                }
            }

            // 7. Comments Section (Expanded)
            AnimatedVisibility(visible = showComments || post.comments.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    post.comments.forEach { comment ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            UserAvatar(avatarUrl = comment.avatarUrl, name = comment.author, size = 26.dp)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                    Text(
                                        text = comment.author,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = comment.text,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Comment Input Box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserAvatar(avatarUrl = user.avatarUrl, name = user.name, size = 28.dp)
                        TextField(
                            value = commentInputText,
                            onValueChange = { commentInputText = it },
                            placeholder = { Text("Write a comment...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                if (commentInputText.isNotBlank()) {
                                    onAddComment(commentInputText)
                                    commentInputText = ""
                                }
                            }),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        )
                        IconButton(
                            onClick = {
                                if (commentInputText.isNotBlank()) {
                                    onAddComment(commentInputText)
                                    commentInputText = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = BrandIndigoSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Post?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove this post? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeletePost?.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
