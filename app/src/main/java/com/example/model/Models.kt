package com.example.model

enum class UserRole(val displayName: String) {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    MODERATOR("Moderator"),
    MEMBER("Member")
}

enum class PostType {
    STANDARD,
    QUESTION
}

enum class FeedFilter {
    ALL,
    QUESTIONS,
    MEDIA
}

enum class AppTab(val title: String) {
    FEED("Feed"),
    FRIENDS("Friends"),
    MESSAGES("Messages"),
    PRO("Pro Dashboard"),
    SAVED("Saved"),
    ADMIN("Admin Panel")
}

data class UserProfile(
    val id: String = "user_admin",
    val name: String = "Admin (Owner)",
    val firstName: String = "Ask",
    val lastName: String = "Admin",
    val email: String = "askman36@gmail.com",
    val phone: String = "+880 1700-000000",
    val gender: String = "Male",
    val work: String = "Platform Administrator & Tech Lead",
    val college: String = "University of Engineering & Technology",
    val password: String = "admin123",
    val userRole: UserRole = UserRole.SUPER_ADMIN,
    val avatarUrl: String = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80",
    val role: String = "Super Administrator",
    val bio: String = "Platform creator and community overseer on Ask ME.",
    val isSuspended: Boolean = false,
    val isVerified: Boolean = true,
    val joinedDate: String = "September 2026",
    val reputation: Int = 9990,
    val answersGiven: Int = 120,
    val netFollowers: Int = 14500,
    val postReach: String = "150K",
    val engagementRate: String = "12.4%"
)

data class Answer(
    val id: String,
    val author: String,
    val avatarUrl: String,
    val text: String,
    val upvotes: Int,
    val isUpvoted: Boolean = false
)

data class Comment(
    val id: String,
    val author: String,
    val avatarUrl: String,
    val text: String,
    val timeAgo: String = "Just now"
)

data class Post(
    val id: String,
    val author: String,
    val avatarUrl: String,
    val timeAgo: String,
    val privacy: String = "Public", // "Public", "Friends", "Only Me"
    val type: PostType = PostType.STANDARD,
    val content: String,
    val imageUrl: String? = null,
    val reactionsCount: Int = 0,
    val isLiked: Boolean = false,
    val answers: List<Answer> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val isSaved: Boolean = false,
    val isPinned: Boolean = false
)

data class SystemNotice(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val author: String = "Ask ME Super Admin"
)

data class Friend(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isOnline: Boolean,
    val status: String
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean
)

data class AppNotification(
    val id: String,
    val text: String,
    val timeAgo: String,
    val isUnread: Boolean
)

data class DayMetric(
    val day: String,
    val impressions: Float,
    val qaReach: Float
)

data class VerificationRecord(
    val id: String,
    val target: String,
    val code: String,
    val timestamp: String,
    val isVerified: Boolean = false
)
