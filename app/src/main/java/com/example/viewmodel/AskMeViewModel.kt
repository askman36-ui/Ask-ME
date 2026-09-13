package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Answer
import com.example.model.AppNotification
import com.example.model.AppTab
import com.example.model.ChatMessage
import com.example.model.Comment
import com.example.model.DayMetric
import com.example.model.FeedFilter
import com.example.model.Friend
import com.example.model.Post
import com.example.model.PostType
import com.example.model.SystemNotice
import com.example.model.UserProfile
import com.example.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class AskMeViewModel : ViewModel() {

    // All registered users directory
    private val _usersList = MutableStateFlow<List<UserProfile>>(emptyList())
    val usersList: StateFlow<List<UserProfile>> = _usersList.asStateFlow()

    // Active logged-in user
    private val _currentUser = MutableStateFlow(UserProfile())
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Platform notices broadcasted by the Admin
    private val _systemNotices = MutableStateFlow<List<SystemNotice>>(emptyList())
    val systemNotices: StateFlow<List<SystemNotice>> = _systemNotices.asStateFlow()

    // Super Admin Stealth Mode (Admin identity is completely hidden from public directories)
    private val _adminStealthMode = MutableStateFlow(true)
    val adminStealthMode: StateFlow<Boolean> = _adminStealthMode.asStateFlow()

    // Strict 1-Minute Admin Code Verification policy
    private val _strictRegistrationMode = MutableStateFlow(true)
    val strictRegistrationMode: StateFlow<Boolean> = _strictRegistrationMode.asStateFlow()

    // Real-time log of OTP verification codes for Admin security monitor
    private val _verificationRecords = MutableStateFlow<List<com.example.model.VerificationRecord>>(
        listOf(
            com.example.model.VerificationRecord("v1", "david@example.com", "849201", "12 mins ago", true),
            com.example.model.VerificationRecord("v2", "+880 1612-456789", "512934", "30 mins ago", true),
            com.example.model.VerificationRecord("v3", "sarah@example.com", "938104", "1 hour ago", true)
        )
    )
    val verificationRecords: StateFlow<List<com.example.model.VerificationRecord>> = _verificationRecords.asStateFlow()

    // Public users list with Admin ID hidden if stealth mode is ON
    val publicUsersList: StateFlow<List<UserProfile>> = combine(_usersList, _adminStealthMode) { users, stealth ->
        if (stealth) {
            users.filter { it.userRole != UserRole.SUPER_ADMIN && it.userRole != UserRole.ADMIN }
        } else {
            users
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _allPosts = MutableStateFlow<List<Post>>(emptyList())
    val allPosts: StateFlow<List<Post>> = _allPosts.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FeedFilter.ALL)
    val selectedFilter: StateFlow<FeedFilter> = _selectedFilter.asStateFlow()

    private val _activeTab = MutableStateFlow(AppTab.FEED)
    val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    private val _activeChatFriend = MutableStateFlow<Friend?>(null)
    val activeChatFriend: StateFlow<Friend?> = _activeChatFriend.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog: StateFlow<Boolean> = _showNotificationsDialog.asStateFlow()

    // 7-day metric data for chart in Pro Dashboard
    val weeklyMetrics: List<DayMetric> = listOf(
        DayMetric("Mon", 3200f, 1200f),
        DayMetric("Tue", 4500f, 1900f),
        DayMetric("Wed", 4100f, 2400f),
        DayMetric("Thu", 5800f, 2100f),
        DayMetric("Fri", 6200f, 3100f),
        DayMetric("Sat", 7900f, 4200f),
        DayMetric("Sun", 9100f, 4800f)
    )

    // Filtered posts based on active search, filter chips, with pinned posts first
    val filteredPosts: StateFlow<List<Post>> = combine(_allPosts, _searchQuery, _selectedFilter) { posts, query, filter ->
        var list = posts
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.content.lowercase().contains(q) ||
                it.author.lowercase().contains(q) ||
                it.answers.any { ans -> ans.text.lowercase().contains(q) }
            }
        }
        val byFilter = when (filter) {
            FeedFilter.ALL -> list
            FeedFilter.QUESTIONS -> list.filter { it.type == PostType.QUESTION }
            FeedFilter.MEDIA -> list.filter { !it.imageUrl.isNullOrBlank() }
        }
        // Pinned announcements always float on top
        byFilter.sortedWith(compareByDescending<Post> { it.isPinned })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPosts: StateFlow<List<Post>> = _allPosts.combine(_allPosts) { posts, _ ->
        posts.filter { it.isSaved }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = _notifications.combine(_notifications) { notifs, _ ->
        notifs.count { it.isUnread }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        // 1. Initial User Accounts
        val superAdmin = UserProfile(
            id = "user_admin",
            name = "Admin (Ask ME Owner)",
            firstName = "Ask",
            lastName = "Admin",
            email = "askman36@gmail.com",
            phone = "+880 1700-000000",
            gender = "Male",
            work = "Platform Administrator & Founder",
            college = "University of Dhaka",
            password = "admin123",
            userRole = UserRole.SUPER_ADMIN,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80",
            role = "Super Administrator",
            bio = "Main administrator with platform moderation and full user control permissions.",
            isSuspended = false,
            isVerified = true,
            joinedDate = "August 2026",
            reputation = 9999,
            answersGiven = 154,
            netFollowers = 18200,
            postReach = "180K",
            engagementRate = "14.2%"
        )

        val userSarah = UserProfile(
            id = "user_sarah",
            name = "Sarah Jenkins",
            firstName = "Sarah",
            lastName = "Jenkins",
            email = "sarah@example.com",
            phone = "+880 1812-345678",
            gender = "Female",
            work = "Staff Android Engineer",
            college = "BUET (Computer Science)",
            password = "password123",
            userRole = UserRole.MODERATOR,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
            role = "Pro Creator & Moderator",
            bio = "Staff Android Engineer & Compose Architecture Lead.",
            isSuspended = false,
            isVerified = true,
            joinedDate = "September 2026",
            reputation = 2420,
            answersGiven = 42,
            netFollowers = 3490,
            postReach = "24.5K",
            engagementRate = "8.7%"
        )

        val userDavid = UserProfile(
            id = "user_david",
            name = "David Chen",
            firstName = "David",
            lastName = "Chen",
            email = "david@example.com",
            phone = "+880 1912-987654",
            gender = "Male",
            work = "Mobile Application Developer",
            college = "Dhaka City College",
            password = "password123",
            userRole = UserRole.MEMBER,
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
            role = "Active Member",
            bio = "Full-stack mobile enthusiast & Kotlin coroutines fan.",
            isSuspended = false,
            isVerified = true,
            joinedDate = "September 2026",
            reputation = 980,
            answersGiven = 18,
            netFollowers = 1200,
            postReach = "9.1K",
            engagementRate = "6.5%"
        )

        val userEmily = UserProfile(
            id = "user_emily",
            name = "Emily Watson",
            firstName = "Emily",
            lastName = "Watson",
            email = "emily@example.com",
            phone = "+880 1612-456789",
            gender = "Female",
            work = "Product & UI/UX Designer",
            college = "Brac University",
            password = "password123",
            userRole = UserRole.MEMBER,
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&auto=format&fit=crop&q=80",
            role = "UI/UX Designer",
            bio = "Design system creator and typography specialist.",
            isSuspended = false,
            isVerified = true,
            joinedDate = "September 2026",
            reputation = 1450,
            answersGiven = 24,
            netFollowers = 2100,
            postReach = "14.2K",
            engagementRate = "7.8%"
        )

        val initialUsers = listOf(superAdmin, userSarah, userDavid, userEmily)
        _usersList.value = initialUsers
        // Super Admin (askman36@gmail.com) is default logged-in account
        _currentUser.value = superAdmin

        // 2. Initial System Broadcast Notice
        val welcomeNotice = SystemNotice(
            id = "notice_1",
            title = "Welcome to Ask ME - Community Guidelines & Creator Hub",
            message = "Welcome to the Ask ME professional community! Anyone can register an account, post technical queries, and share solutions. Respect community etiquette.",
            timestamp = "Today",
            author = "Ask ME Super Admin"
        )
        _systemNotices.value = listOf(welcomeNotice)

        val initialFriends = listOf(
            Friend(
                id = "f1",
                name = "Alex Morgan",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
                isOnline = true,
                status = "Coding Jetpack Compose app"
            ),
            Friend(
                id = "f2",
                name = "David Chen",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                isOnline = true,
                status = "Exploring AI prompt engineering"
            ),
            Friend(
                id = "f3",
                name = "Emily Watson",
                avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&auto=format&fit=crop&q=80",
                isOnline = false,
                status = "Offline"
            ),
            Friend(
                id = "f4",
                name = "Michael Brown",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80",
                isOnline = true,
                status = "Busy in team meeting"
            )
        )
        _friends.value = initialFriends

        val initialPosts = listOf(
            Post(
                id = "post_1",
                author = "David Chen",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                timeAgo = "2 hrs ago",
                privacy = "Public",
                type = PostType.QUESTION,
                content = "What is the most efficient way to handle global state management in modern Android apps without overcomplicating with external libraries?",
                imageUrl = null,
                reactionsCount = 17,
                isLiked = false,
                isPinned = false,
                answers = listOf(
                    Answer(
                        id = "a1",
                        author = "Sarah Jenkins",
                        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                        text = "ViewModel with Kotlin StateFlow and UI State sealed classes are the modern gold standard! No extra boilerplate needed.",
                        upvotes = 9,
                        isUpvoted = false
                    ),
                    Answer(
                        id = "a2",
                        author = "Alex Morgan",
                        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
                        text = "Pair StateFlow with collectAsStateWithLifecycle to avoid lifecycle leak cascades in Compose.",
                        upvotes = 4,
                        isUpvoted = false
                    )
                ),
                comments = emptyList(),
                isSaved = false
            ),
            Post(
                id = "post_2",
                author = "Emily Watson",
                avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&auto=format&fit=crop&q=80",
                timeAgo = "4 hrs ago",
                privacy = "Public",
                type = PostType.STANDARD,
                content = "Just launched our new design system! Check out the clean dark mode contrast and typography hierarchy. Thoughts on the spacing?",
                imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&auto=format&fit=crop&q=80",
                reactionsCount = 52,
                isLiked = true,
                isPinned = false,
                answers = emptyList(),
                comments = listOf(
                    Comment(
                        id = "c1",
                        author = "David Chen",
                        avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                        text = "The typography hierarchy and edge-to-edge padding look super crisp!",
                        timeAgo = "3 hrs ago"
                    )
                ),
                isSaved = true
            ),
            Post(
                id = "post_3",
                author = "Sarah Jenkins",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                timeAgo = "1 day ago",
                privacy = "Public",
                type = PostType.QUESTION,
                content = "What are your top techniques for architecting high-frequency real-time event feeds in modern mobile clients?",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&auto=format&fit=crop&q=80",
                reactionsCount = 38,
                isLiked = false,
                isPinned = false,
                answers = listOf(
                    Answer(
                        id = "a3",
                        author = "Michael Brown",
                        avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80",
                        text = "Buffer operators with debounce and distinctUntilChanged prevent UI recomposition storms.",
                        upvotes = 7,
                        isUpvoted = false
                    )
                ),
                comments = listOf(
                    Comment(
                        id = "c2",
                        author = "Alex Morgan",
                        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
                        text = "Great topic for this week's discussion thread!",
                        timeAgo = "12 hrs ago"
                    )
                ),
                isSaved = false
            )
        )
        _allPosts.value = initialPosts

        val initialNotifications = listOf(
            AppNotification("n1", "Alex Morgan answered your question about Compose Architecture", "10m ago", true),
            AppNotification("n2", "David Chen liked your response on Ask ME", "1h ago", true),
            AppNotification("n3", "You gained +50 reputation points in Professional Mode!", "3h ago", false),
            AppNotification("n4", "📢 [Notice] Welcome to the Ask ME Community Hub", "5h ago", false)
        )
        _notifications.value = initialNotifications

        val initialChat = mapOf(
            "f1" to listOf(
                ChatMessage("m1", "Alex Morgan", "Hey! Got a minute to answer a quick Compose UI question?", "10:24 AM", false),
                ChatMessage("m2", "Sarah Jenkins", "Sure! Fire away on Ask ME or right here!", "10:25 AM", true)
            ),
            "f2" to listOf(
                ChatMessage("m3", "David Chen", "Thanks for the tips on StateFlow!", "Yesterday", false),
                ChatMessage("m4", "Sarah Jenkins", "Anytime David! Let me know if you run into any issues.", "Yesterday", true)
            )
        )
        _chatMessages.value = initialChat
    }

    // --- Authentication & User Creation System (অন্য মানুষ একাউন্ট খুলতে পারবে) ---

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        gender: String,
        work: String,
        college: String,
        password: String,
        avatarUrl: String
    ): Pair<Boolean, String> {
        val cleanEmail = email.trim().lowercase()
        val cleanPhone = phone.trim()
        val normalizedPhone = cleanPhone.replace(" ", "").replace("-", "")

        if (cleanEmail.isNotBlank() && _usersList.value.any { it.email.lowercase() == cleanEmail }) {
            return Pair(false, "An account with this email ($cleanEmail) already exists!")
        }
        if (normalizedPhone.isNotBlank() && _usersList.value.any { it.phone.replace(" ", "").replace("-", "") == normalizedPhone }) {
            return Pair(false, "An account with this mobile number ($cleanPhone) already exists!")
        }

        val fullName = "${firstName.trim()} ${lastName.trim()}".trim()
        val newUser = UserProfile(
            id = "user_${UUID.randomUUID()}",
            name = fullName,
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = cleanEmail,
            phone = cleanPhone,
            gender = gender.ifBlank { "Male" },
            work = work.trim(),
            college = college.trim(),
            password = password,
            userRole = UserRole.MEMBER,
            avatarUrl = avatarUrl,
            role = if (work.isNotBlank()) work.trim() else "Verified Community Member",
            bio = if (college.isNotBlank()) "${work.trim()} • $college" else work.trim(),
            isSuspended = false,
            isVerified = true,
            joinedDate = "September 2026",
            reputation = 100,
            answersGiven = 0,
            netFollowers = 0,
            postReach = "0",
            engagementRate = "0.0%"
        )

        _usersList.value = _usersList.value + newUser
        _currentUser.value = newUser

        recordVerificationCode(cleanEmail.ifBlank { cleanPhone }, "VERIFIED-OTP")

        // Send welcome notification
        val welcomeNotif = AppNotification(
            id = "n_${UUID.randomUUID()}",
            text = "Welcome to Ask ME, ${newUser.name}! Your account has been verified via Admin Security Code and is now active.",
            timeAgo = "Just now",
            isUnread = true
        )
        _notifications.value = listOf(welcomeNotif) + _notifications.value

        return Pair(true, "Account verified & created successfully! Welcome to Ask ME.")
    }

    fun loginUser(identifier: String, password: String): Pair<Boolean, String> {
        val cleanId = identifier.trim().lowercase()
        val cleanNormalized = identifier.replace(" ", "").replace("-", "")

        val user = _usersList.value.find { 
            it.email.lowercase() == cleanId || 
            it.phone.trim() == identifier.trim() || 
            (cleanNormalized.length >= 6 && it.phone.replace(" ", "").replace("-", "") == cleanNormalized)
        } ?: return Pair(false, "No account found with this email or mobile number: $identifier")

        if (user.password != password) {
            return Pair(false, "Incorrect password. Please check and try again.")
        }

        if (user.isSuspended) {
            return Pair(false, "Your account has been suspended by the Admin. Please contact support.")
        }

        _currentUser.value = user
        return Pair(true, "Welcome back, ${user.name}!")
    }

    fun switchUser(user: UserProfile) {
        _currentUser.value = user
    }

    // --- Super Admin Control System (মূল অ্যাডমিন কন্ট্রোল ক্ষমতা) ---

    fun toggleSuspendUser(userId: String) {
        _usersList.value = _usersList.value.map { u ->
            if (u.id == userId) {
                val newSuspended = !u.isSuspended
                val updated = u.copy(isSuspended = newSuspended)
                if (_currentUser.value.id == userId) {
                    _currentUser.value = updated
                }
                updated
            } else {
                u
            }
        }
    }

    fun changeUserRole(userId: String, newRole: UserRole) {
        _usersList.value = _usersList.value.map { u ->
            if (u.id == userId) {
                val updated = u.copy(
                    userRole = newRole,
                    role = newRole.displayName
                )
                if (_currentUser.value.id == userId) {
                    _currentUser.value = updated
                }
                updated
            } else {
                u
            }
        }
    }

    fun deleteUser(userId: String) {
        _usersList.value = _usersList.value.filter { it.id != userId }
        // If deleted current user, reset to Super Admin
        if (_currentUser.value.id == userId) {
            _usersList.value.find { it.userRole == UserRole.SUPER_ADMIN }?.let {
                _currentUser.value = it
            }
        }
    }

    fun deletePost(postId: String) {
        _allPosts.value = _allPosts.value.filter { it.id != postId }
    }

    fun togglePinPost(postId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(isPinned = !post.isPinned)
            } else {
                post
            }
        }
    }

    fun deleteAnswer(postId: String, answerId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(answers = post.answers.filter { it.id != answerId })
            } else {
                post
            }
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(comments = post.comments.filter { it.id != commentId })
            } else {
                post
            }
        }
    }

    fun toggleUserVerification(userId: String) {
        _usersList.value = _usersList.value.map { u ->
            if (u.id == userId) {
                val updated = u.copy(isVerified = !u.isVerified)
                if (_currentUser.value.id == userId) {
                    _currentUser.value = updated
                }
                updated
            } else {
                u
            }
        }
    }

    fun adminResetPassword(userId: String, newPass: String): Boolean {
        if (newPass.isBlank()) return false
        _usersList.value = _usersList.value.map { u ->
            if (u.id == userId) {
                val updated = u.copy(password = newPass.trim())
                if (_currentUser.value.id == userId) {
                    _currentUser.value = updated
                }
                updated
            } else {
                u
            }
        }
        return true
    }

    fun toggleAdminStealthMode() {
        _adminStealthMode.value = !_adminStealthMode.value
    }

    fun toggleStrictRegistration() {
        _strictRegistrationMode.value = !_strictRegistrationMode.value
    }

    fun recordVerificationCode(target: String, code: String) {
        val record = com.example.model.VerificationRecord(
            id = "vr_${UUID.randomUUID()}",
            target = target,
            code = code,
            timestamp = "Just now",
            isVerified = true
        )
        _verificationRecords.value = listOf(record) + _verificationRecords.value
    }

    fun broadcastNotice(title: String, message: String) {
        val notice = SystemNotice(
            id = "notice_${UUID.randomUUID()}",
            title = title,
            message = message,
            timestamp = "Just now",
            author = _currentUser.value.name
        )
        _systemNotices.value = listOf(notice) + _systemNotices.value

        // Also broadcast to notifications for all users
        val notif = AppNotification(
            id = "notif_${UUID.randomUUID()}",
            text = "📢 [Notice from Admin] $title: $message",
            timeAgo = "Just now",
            isUnread = true
        )
        _notifications.value = listOf(notif) + _notifications.value
    }

    fun deleteNotice(noticeId: String) {
        _systemNotices.value = _systemNotices.value.filter { it.id != noticeId }
    }

    // --- Navigation, Filter & Theme ---

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: FeedFilter) {
        _selectedFilter.value = filter
    }

    fun setActiveTab(tab: AppTab) {
        _activeTab.value = tab
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleNotificationsDialog(show: Boolean? = null) {
        _showNotificationsDialog.value = show ?: !_showNotificationsDialog.value
    }

    fun markAllNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isUnread = false) }
    }

    fun createPost(content: String, imageUrl: String?, type: PostType, privacy: String) {
        if (content.isBlank()) return
        val current = _currentUser.value
        if (current.isSuspended) {
            // Suspended users cannot post
            return
        }
        val isAdminUser = current.userRole == UserRole.SUPER_ADMIN || current.userRole == UserRole.ADMIN
        val (effectiveAuthor, effectiveAvatar) = if (isAdminUser && _adminStealthMode.value) {
            Pair("Ask ME Official", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80")
        } else {
            Pair(current.name, current.avatarUrl)
        }
        val newPost = Post(
            id = "post_${UUID.randomUUID()}",
            author = effectiveAuthor,
            avatarUrl = effectiveAvatar,
            timeAgo = "Just now",
            privacy = privacy,
            type = type,
            content = content.trim(),
            imageUrl = if (imageUrl.isNullOrBlank()) null else imageUrl.trim(),
            reactionsCount = 0,
            isLiked = false,
            isPinned = false,
            answers = emptyList(),
            comments = emptyList(),
            isSaved = false
        )
        _allPosts.value = listOf(newPost) + _allPosts.value
    }

    fun toggleReaction(postId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.isLiked
                val newCount = if (newLiked) post.reactionsCount + 1 else (post.reactionsCount - 1).coerceAtLeast(0)
                post.copy(isLiked = newLiked, reactionsCount = newCount)
            } else {
                post
            }
        }
    }

    fun toggleSavePost(postId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(isSaved = !post.isSaved)
            } else {
                post
            }
        }
    }

    fun addAnswer(postId: String, answerText: String) {
        if (answerText.isBlank()) return
        val current = _currentUser.value
        if (current.isSuspended) return

        val isAdminUser = current.userRole == UserRole.SUPER_ADMIN || current.userRole == UserRole.ADMIN
        val (effectiveAuthor, effectiveAvatar) = if (isAdminUser && _adminStealthMode.value) {
            Pair("Ask ME Official", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80")
        } else {
            Pair(current.name, current.avatarUrl)
        }

        val newAnswer = Answer(
            id = "ans_${UUID.randomUUID()}",
            author = effectiveAuthor,
            avatarUrl = effectiveAvatar,
            text = answerText.trim(),
            upvotes = 1,
            isUpvoted = true
        )

        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(answers = post.answers + newAnswer)
            } else {
                post
            }
        }

        // Reward user reputation + answers count
        _currentUser.value = current.copy(
            answersGiven = current.answersGiven + 1,
            reputation = current.reputation + 10
        )
    }

    fun upvoteAnswer(postId: String, answerId: String) {
        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                val updatedAnswers = post.answers.map { ans ->
                    if (ans.id == answerId) {
                        val newUpvoted = !ans.isUpvoted
                        val count = if (newUpvoted) ans.upvotes + 1 else (ans.upvotes - 1).coerceAtLeast(0)
                        ans.copy(isUpvoted = newUpvoted, upvotes = count)
                    } else {
                        ans
                    }
                }
                post.copy(answers = updatedAnswers)
            } else {
                post
            }
        }
    }

    fun addComment(postId: String, commentText: String) {
        if (commentText.isBlank()) return
        val current = _currentUser.value
        if (current.isSuspended) return

        val isAdminUser = current.userRole == UserRole.SUPER_ADMIN || current.userRole == UserRole.ADMIN
        val (effectiveAuthor, effectiveAvatar) = if (isAdminUser && _adminStealthMode.value) {
            Pair("Ask ME Official", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200&auto=format&fit=crop&q=80")
        } else {
            Pair(current.name, current.avatarUrl)
        }

        val newComment = Comment(
            id = "comm_${UUID.randomUUID()}",
            author = effectiveAuthor,
            avatarUrl = effectiveAvatar,
            text = commentText.trim(),
            timeAgo = "Just now"
        )

        _allPosts.value = _allPosts.value.map { post ->
            if (post.id == postId) {
                post.copy(comments = post.comments + newComment)
            } else {
                post
            }
        }
    }

    fun openChatWith(friend: Friend) {
        _activeChatFriend.value = friend
    }

    fun closeChat() {
        _activeChatFriend.value = null
    }

    fun sendChatMessage(friendId: String, text: String) {
        if (text.isBlank()) return
        val current = _currentUser.value
        val msg = ChatMessage(
            id = "msg_${UUID.randomUUID()}",
            senderName = current.name,
            text = text.trim(),
            timestamp = "Just now",
            isFromMe = true
        )

        val currentList = _chatMessages.value[friendId].orEmpty()
        val updatedMap = _chatMessages.value.toMutableMap()
        updatedMap[friendId] = currentList + msg
        _chatMessages.value = updatedMap

        // Simulate friendly reply from contact after a short delay
        viewModelScope.launch {
            delay(1200)
            val friend = _friends.value.find { it.id == friendId }
            val replyText = when (friendId) {
                "f1" -> "Thanks! That really clears things up. Will test it in my current branch!"
                "f2" -> "Appreciate the quick insights! Your advice is always top notch."
                "f3" -> "Hey! Just saw your message, thanks for reaching out!"
                else -> "Got it! Thanks for connecting on Ask ME!"
            }
            val replyMsg = ChatMessage(
                id = "msg_${UUID.randomUUID()}",
                senderName = friend?.name ?: "Friend",
                text = replyText,
                timestamp = "Just now",
                isFromMe = false
            )
            val newList = _chatMessages.value[friendId].orEmpty() + replyMsg
            val newMap = _chatMessages.value.toMutableMap()
            newMap[friendId] = newList
            _chatMessages.value = newMap
        }
    }
}
