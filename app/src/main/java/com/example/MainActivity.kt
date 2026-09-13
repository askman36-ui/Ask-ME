package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppTab
import com.example.model.UserRole
import com.example.ui.components.BottomNavBarAskMe
import com.example.ui.components.InteractiveChatDialog
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.TopNavBarAskMe
import com.example.ui.screens.AccountSwitcherDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.ProDashboardScreen
import com.example.ui.screens.ProfileDialog
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AskMeViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: AskMeViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
      MyApplicationTheme(darkTheme = isDarkTheme) {
        AskMeApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun AskMeApp(viewModel: AskMeViewModel) {
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val allUsers by viewModel.usersList.collectAsStateWithLifecycle()
  val allPosts by viewModel.allPosts.collectAsStateWithLifecycle()
  val systemNotices by viewModel.systemNotices.collectAsStateWithLifecycle()
  val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
  val filteredPosts by viewModel.filteredPosts.collectAsStateWithLifecycle()
  val savedPosts by viewModel.savedPosts.collectAsStateWithLifecycle()
  val friends by viewModel.friends.collectAsStateWithLifecycle()
  val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
  val activeChatFriend by viewModel.activeChatFriend.collectAsStateWithLifecycle()
  val notifications by viewModel.notifications.collectAsStateWithLifecycle()
  val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
  val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
  val showNotificationsDialog by viewModel.showNotificationsDialog.collectAsStateWithLifecycle()
  val adminStealthMode by viewModel.adminStealthMode.collectAsStateWithLifecycle()
  val strictRegistrationMode by viewModel.strictRegistrationMode.collectAsStateWithLifecycle()
  val verificationRecords by viewModel.verificationRecords.collectAsStateWithLifecycle()

  var showProfileDialog by remember { mutableStateOf(false) }
  var showAuthDialog by remember { mutableStateOf(false) }
  var showAccountSwitcher by remember { mutableStateOf(false) }

  val isAdmin = currentUser.userRole == UserRole.SUPER_ADMIN || currentUser.userRole == UserRole.ADMIN

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopNavBarAskMe(
        user = currentUser,
        searchQuery = searchQuery,
        onSearchChange = { viewModel.setSearchQuery(it) },
        unreadNotificationsCount = unreadNotifsCount,
        onNotificationsClick = { viewModel.toggleNotificationsDialog(true) },
        isDarkTheme = isDarkTheme,
        onToggleDarkTheme = { viewModel.toggleDarkTheme() },
        onLogoClick = {
          viewModel.setActiveTab(AppTab.FEED)
          viewModel.setSearchQuery("")
        },
        onProfileClick = { showProfileDialog = true },
        onAdminClick = { viewModel.setActiveTab(AppTab.ADMIN) },
        onAccountsClick = null
      )
    },
    bottomBar = {
      BottomNavBarAskMe(
        activeTab = activeTab,
        onTabSelected = { viewModel.setActiveTab(it) },
        isAdmin = isAdmin && !adminStealthMode
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(innerPadding)
    ) {
      when (activeTab) {
        AppTab.FEED -> {
          FeedScreen(
            user = currentUser,
            posts = filteredPosts,
            selectedFilter = selectedFilter,
            onFilterSelect = { viewModel.setFilter(it) },
            onCreatePost = { content, img, type, priv ->
              viewModel.createPost(content, img, type, priv)
            },
            onToggleReaction = { viewModel.toggleReaction(it) },
            onToggleSave = { viewModel.toggleSavePost(it) },
            onAddAnswer = { postId, text -> viewModel.addAnswer(postId, text) },
            onUpvoteAnswer = { postId, ansId -> viewModel.upvoteAnswer(postId, ansId) },
            onAddComment = { postId, text -> viewModel.addComment(postId, text) },
            systemNotices = systemNotices,
            onDeletePost = { viewModel.deletePost(it) },
            onOpenAuth = { showAuthDialog = true }
          )
        }
        AppTab.FRIENDS -> {
          FriendsScreen(
            friends = friends,
            onMessageFriend = { viewModel.openChatWith(it) }
          )
        }
        AppTab.MESSAGES -> {
          MessagesScreen(
            friends = friends,
            chatMessages = chatMessages,
            onOpenChat = { viewModel.openChatWith(it) }
          )
        }
        AppTab.PRO -> {
          ProDashboardScreen(
            user = currentUser,
            metrics = viewModel.weeklyMetrics,
            onFilterTopicClick = { topic ->
              viewModel.setActiveTab(AppTab.FEED)
              viewModel.setSearchQuery(topic)
            }
          )
        }
        AppTab.SAVED -> {
          SavedScreen(
            savedPosts = savedPosts,
            onRemoveSaved = { viewModel.toggleSavePost(it) }
          )
        }
        AppTab.ADMIN -> {
          AdminScreen(
            currentUser = currentUser,
            allUsers = allUsers,
            allPosts = allPosts,
            systemNotices = systemNotices,
            adminStealthMode = adminStealthMode,
            strictRegistrationMode = strictRegistrationMode,
            verificationRecords = verificationRecords,
            onToggleSuspendUser = { viewModel.toggleSuspendUser(it) },
            onChangeUserRole = { userId, role -> viewModel.changeUserRole(userId, role) },
            onToggleVerifyUser = { viewModel.toggleUserVerification(it) },
            onResetPassword = { userId, pass -> viewModel.adminResetPassword(userId, pass) },
            onDeleteUser = { viewModel.deleteUser(it) },
            onDeletePost = { viewModel.deletePost(it) },
            onTogglePinPost = { viewModel.togglePinPost(it) },
            onDeleteAnswer = { postId, ansId -> viewModel.deleteAnswer(postId, ansId) },
            onDeleteComment = { postId, commId -> viewModel.deleteComment(postId, commId) },
            onToggleAdminStealth = { viewModel.toggleAdminStealthMode() },
            onToggleStrictRegistration = { viewModel.toggleStrictRegistration() },
            onManualGenerateCode = { target -> viewModel.recordVerificationCode(target, (100000..999999).random().toString()) },
            onBroadcastNotice = { title, msg -> viewModel.broadcastNotice(title, msg) },
            onDeleteNotice = { viewModel.deleteNotice(it) }
          )
        }
      }
    }
  }

  // Auth Dialog (Create Account / Login)
  if (showAuthDialog) {
    AuthDialog(
      onDismiss = { showAuthDialog = false },
      onLogin = { id, pass -> viewModel.loginUser(id, pass) },
      onRegister = { firstName, lastName, email, phone, gender, work, college, pass, avatar ->
        viewModel.registerUser(firstName, lastName, email, phone, gender, work, college, pass, avatar)
      },
      onQuickLogin = { viewModel.switchUser(it) },
      allUsers = allUsers,
      onRecordVerificationCode = { target, code -> viewModel.recordVerificationCode(target, code) }
    )
  }

  // Accounts Switcher Dialog
  if (showAccountSwitcher) {
    AccountSwitcherDialog(
      currentUser = currentUser,
      allUsers = allUsers,
      onDismiss = { showAccountSwitcher = false },
      onSwitchUser = { viewModel.switchUser(it) },
      onOpenRegister = {
        showAccountSwitcher = false
        showAuthDialog = true
      },
      onOpenAdminPanel = {
        viewModel.setActiveTab(AppTab.ADMIN)
      }
    )
  }

  // Notifications Dialog
  if (showNotificationsDialog) {
    NotificationsDialog(
      notifications = notifications,
      onDismiss = { viewModel.toggleNotificationsDialog(false) },
      onMarkAllRead = { viewModel.markAllNotificationsAsRead() }
    )
  }

  // Interactive Live Chat Dialog
  activeChatFriend?.let { friend ->
    val messages = chatMessages[friend.id].orEmpty()
    InteractiveChatDialog(
      friend = friend,
      messages = messages,
      onSendMessage = { text -> viewModel.sendChatMessage(friend.id, text) },
      onDismiss = { viewModel.closeChat() }
    )
  }

  // Profile Dialog
  if (showProfileDialog) {
    ProfileDialog(
      user = currentUser,
      onDismiss = { showProfileDialog = false },
      onOpenAccountSwitcher = { showAccountSwitcher = true },
      onOpenAdminPanel = { viewModel.setActiveTab(AppTab.ADMIN) }
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}

