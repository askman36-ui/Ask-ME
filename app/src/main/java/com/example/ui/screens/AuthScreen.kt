package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.components.AskMeLogo
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400
import kotlinx.coroutines.delay

val PRESET_AVATARS = listOf(
    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80",
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80",
    "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&auto=format&fit=crop&q=80"
)

@Composable
fun AuthDialog(
    onDismiss: () -> Unit,
    onLogin: (String, String) -> Pair<Boolean, String>,
    onRegister: (
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        gender: String,
        work: String,
        college: String,
        password: String,
        avatarUrl: String
    ) -> Pair<Boolean, String>,
    onQuickLogin: (UserProfile) -> Unit,
    allUsers: List<UserProfile>,
    onRecordVerificationCode: ((String, String) -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(vertical = 20.dp)
                .testTag("auth_dialog")
        ) {
            var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with Logo
                AskMeLogo()

                Text(
                    text = if (selectedTab == 0) "Sign in to Ask ME" else "Member Registration (শর্তাবলী ও কোড যাচাই)",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (selectedTab == 0)
                        "Login with your Gmail address or Mobile number."
                    else
                        "অ্যাকাউন্ট খুলতে প্রয়োজনীয় সকল শর্ত পূরণ করুন এবং ১ মিনিটের মধ্যে অ্যাডমিন কোড যাচাই করুন।",
                    fontSize = 12.sp,
                    color = Slate400,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                // Tabs: Login vs Register
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    divider = {},
                    indicator = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Sign In (লগইন)",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) BrandIndigoPrimary else Slate400,
                                fontSize = 13.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Register (নতুন অ্যাকাউন্ট)",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) BrandIndigoPrimary else Slate400,
                                fontSize = 13.sp
                            )
                        }
                    )
                }

                if (selectedTab == 0) {
                    // Login Tab
                    LoginForm(
                        onLogin = { id, pass ->
                            val (success, msg) = onLogin(id, pass)
                            if (success) onDismiss()
                            Pair(success, msg)
                        },
                        allUsers = allUsers,
                        onQuickLogin = {
                            onQuickLogin(it)
                            onDismiss()
                        }
                    )
                } else {
                    // Register Tab with strict criteria & 1-minute admin OTP verification
                    RegisterForm(
                        onRegister = { firstName, lastName, email, phone, gender, work, college, pass, avatar ->
                            val (success, msg) = onRegister(firstName, lastName, email, phone, gender, work, college, pass, avatar)
                            if (success) onDismiss()
                            Pair(success, msg)
                        },
                        onRecordVerificationCode = onRecordVerificationCode
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text("Close", color = Slate400)
                }
            }
        }
    }
}

@Composable
fun LoginForm(
    onLogin: (String, String) -> Pair<Boolean, String>,
    allUsers: List<UserProfile>,
    onQuickLogin: (UserProfile) -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = identifier,
            onValueChange = {
                identifier = it
                errorMessage = null
            },
            label = { Text("Gmail or Mobile Number") },
            placeholder = { Text("e.g. askman36@gmail.com or 017xxxxxxxx") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrandIndigoPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandIndigoPrimary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Slate400
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage.orEmpty(),
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                if (identifier.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter both Gmail/Mobile and password."
                    return@Button
                }
                val (success, msg) = onLogin(identifier.trim(), password)
                if (!success) {
                    errorMessage = msg
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrandIndigoPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("login_submit_button")
        ) {
            Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // Quick 1-tap Account Login (Regular members only - Admin ID is hidden)
        Text(
            text = "Quick One-Tap Login (Saved Profiles):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate400
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            val publicSavedUsers = allUsers.filter { it.userRole != UserRole.SUPER_ADMIN && it.userRole != UserRole.ADMIN }.take(4)
            publicSavedUsers.forEach { demoUser ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onQuickLogin(demoUser) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            UserAvatar(avatarUrl = demoUser.avatarUrl, name = demoUser.name, size = 32.dp)
                            Column {
                                Text(text = demoUser.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = demoUser.email.ifBlank { demoUser.phone }, fontSize = 11.sp, color = Slate400)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (demoUser.userRole) {
                                UserRole.SUPER_ADMIN -> BrandPurple
                                UserRole.ADMIN -> BrandIndigoPrimary
                                UserRole.MODERATOR -> BrandPink
                                UserRole.MEMBER -> BrandEmerald
                            }
                        ) {
                            Text(
                                text = demoUser.userRole.displayName,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterForm(
    onRegister: (
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        gender: String,
        work: String,
        college: String,
        password: String,
        avatarUrl: String
    ) -> Pair<Boolean, String>,
    onRecordVerificationCode: ((String, String) -> Unit)? = null
) {
    // Member required condition fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") } // "Male" or "Female"
    var work by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableStateOf(PRESET_AVATARS[1]) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Step state: 0 = Form with conditions, 1 = 1-minute Admin Code Verification
    var currentStep by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Admin Verification Code & 1-minute countdown state
    var generatedCode by remember { mutableStateOf("") }
    var enteredCode by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var isCodeExpired by remember { mutableStateOf(false) }
    var codeSentTarget by remember { mutableStateOf("") }

    // Countdown timer for 1 minute (60 seconds)
    LaunchedEffect(currentStep, generatedCode) {
        if (currentStep == 1) {
            remainingSeconds = 60
            isCodeExpired = false
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--
            }
            isCodeExpired = true
        }
    }

    if (currentStep == 0) {
        // STEP 1: Registration Form with strict member conditions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Conditions Checklist Header Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandIndigoLight.copy(alpha = 0.5f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandIndigoPrimary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = BrandIndigoPrimary, modifier = Modifier.size(16.dp))
                        Text("মেম্বার একাউন্ট খোলার আবশ্যিক শর্তাবলী:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BrandIndigoPrimary)
                    }
                    Text("✓ First Name এবং Last Name নিশ্চিত করা", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("✓ Mobile Number অথবা Gmail Account প্রদান", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("✓ Gender (Male / Female) নির্বাচন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("✓ Work (পেশা) এবং College (শিক্ষাপ্রতিষ্ঠান) নিশ্চিত করা", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("✓ Admin থেকে অটোমেটিক কোড ১ মিনিটের মধ্যে ভেরিফাই করা", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // 1. First Name & Last Name (Side by Side)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        errorMessage = null
                    },
                    label = { Text("First Name *") },
                    placeholder = { Text("e.g. তানভীর / Alex") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrandIndigoPrimary, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        errorMessage = null
                    },
                    label = { Text("Last Name *") },
                    placeholder = { Text("e.g. হাসান / Chen") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // 2. Gender Selection (Male or Female)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Gender (লিঙ্গ নির্বাচন করুন) *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isMale = gender == "Male"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isMale) BrandIndigoPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isMale) 2.dp else 1.dp,
                            color = if (isMale) BrandIndigoPrimary else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { gender = "Male" }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("👨 Male (পুরুষ)", fontWeight = if (isMale) FontWeight.Bold else FontWeight.Normal, color = if (isMale) BrandIndigoPrimary else MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                            if (isMale) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandIndigoPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    val isFemale = gender == "Female"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isFemale) BrandPink.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isFemale) 2.dp else 1.dp,
                            color = if (isFemale) BrandPink else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { gender = "Female" }
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("👩 Female (মহিলা)", fontWeight = if (isFemale) FontWeight.Bold else FontWeight.Normal, color = if (isFemale) BrandPink else MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                            if (isFemale) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandPink, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // 3. Mobile Number (Required for 1-minute verification)
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    errorMessage = null
                },
                label = { Text("Mobile Number (মোবাইল নম্বর) *") },
                placeholder = { Text("e.g. 01712-345678") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BrandIndigoPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // 4. Gmail / Email Account
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Gmail / Email Account *") },
                placeholder = { Text("e.g. username@gmail.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BrandIndigoPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // 5. Work (কর্মক্ষেত্র বা পেশা)
            OutlinedTextField(
                value = work,
                onValueChange = {
                    work = it
                    errorMessage = null
                },
                label = { Text("Work / Occupation (পেশা বা কর্মক্ষেত্র) *") },
                placeholder = { Text("e.g. Software Engineer, Doctor, Teacher, Student") },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = BrandIndigoPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // 6. College (কলেজ বা শিক্ষাপ্রতিষ্ঠান)
            OutlinedTextField(
                value = college,
                onValueChange = {
                    college = it
                    errorMessage = null
                },
                label = { Text("College / Educational Institute (কলেজ নিশ্চিত করুন) *") },
                placeholder = { Text("e.g. Dhaka College, BUET, University of Dhaka") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = BrandIndigoPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // 7. Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Password (পাসওয়ার্ড - কমপক্ষে ৬ অক্ষর) *") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandIndigoPrimary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Slate400
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // 8. Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
                label = { Text("Confirm Password *") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandIndigoPrimary) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Avatar Selection
            Text(text = "Choose Profile Picture:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(PRESET_AVATARS) { avatarUrl ->
                    val isSelected = selectedAvatar == avatarUrl
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) BrandIndigoPrimary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedAvatar = avatarUrl },
                        contentAlignment = Alignment.Center
                    ) {
                        UserAvatar(avatarUrl = avatarUrl, name = "Avatar", size = 44.dp)
                        if (isSelected) {
                            Surface(
                                shape = CircleShape,
                                color = BrandIndigoPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Terms Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { termsAccepted = !termsAccepted }
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = BrandIndigoPrimary)
                )
                Text(
                    text = "আমি Ask ME এর সকল সদস্য নীতি, লিঙ্গ, কর্মক্ষেত্র ও কলেজ নিশ্চিত করে শর্ত মেনে একাউন্ট খুলতে সম্মত।",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Button to Proceed to Step 2 (Admin Verification Code)
            Button(
                onClick = {
                    if (firstName.isBlank()) {
                        errorMessage = "দয়া করে First Name প্রদান করুন।"
                        return@Button
                    }
                    if (lastName.isBlank()) {
                        errorMessage = "দয়া করে Last Name প্রদান করুন।"
                        return@Button
                    }
                    if (phone.isBlank() && email.isBlank()) {
                        errorMessage = "মোবাইল নম্বর অথবা জিমেইল যেকোনো একটি প্রদান আবশ্যক।"
                        return@Button
                    }
                    if (phone.isNotBlank() && phone.replace(" ", "").replace("-", "").length < 8) {
                        errorMessage = "সঠিক মোবাইল নম্বর লিখুন (যেমন: 017xxxxxxxx)।"
                        return@Button
                    }
                    if (email.isNotBlank() && (!email.contains("@") || !email.contains("."))) {
                        errorMessage = "সঠিক জিমেইল বা ইমেইল ঠিকানা লিখুন।"
                        return@Button
                    }
                    if (work.isBlank()) {
                        errorMessage = "আপনার পেশা বা কর্মক্ষেত্র (Work) নিশ্চিত করুন।"
                        return@Button
                    }
                    if (college.isBlank()) {
                        errorMessage = "আপনার কলেজ বা শিক্ষাপ্রতিষ্ঠান (College) নিশ্চিত করুন।"
                        return@Button
                    }
                    if (password.length < 6) {
                        errorMessage = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে।"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "পাসওয়ার্ড দুটি মিলছে না! পুনরায় চেক করুন।"
                        return@Button
                    }
                    if (!termsAccepted) {
                        errorMessage = "একাউন্ট খোলার পূর্বে শর্তাবলীর সম্মতি বক্সে টিক দিন।"
                        return@Button
                    }

                    // All conditions satisfied! Generate 6-digit verification code from Admin
                    val newCode = (100000..999999).random().toString()
                    generatedCode = newCode
                    val target = if (phone.isNotBlank()) phone.trim() else email.trim()
                    codeSentTarget = target
                    onRecordVerificationCode?.invoke(target, newCode)
                    enteredCode = ""
                    errorMessage = null
                    currentStep = 1
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigoPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("proceed_verification_button")
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceed to Admin Code Verification", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    } else {
        // STEP 2: 1-Minute Admin Automated Code Verification
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Admin Dispatch Badge Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandPurple.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                                .size(22.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "ADMIN AUTOMATED DISPATCH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = BrandPurple
                        )
                        Text(
                            text = "Admin থেকে ভেরিফিকেশন কোড পাঠানো হয়েছে",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "প্রেরিত মাধ্যম: $codeSentTarget",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }
            }

            // Incoming Admin Simulation Notification Box
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = BrandAmber, modifier = Modifier.size(16.dp))
                            Text("Incoming Message from Admin", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = BrandEmerald.copy(alpha = 0.2f)
                        ) {
                            Text("Delivered", color = BrandEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Text(
                        text = "আপনার Ask ME মেম্বার সিকিউরিটি কোড:",
                        fontSize = 12.sp,
                        color = Slate400
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandIndigoPrimary.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = generatedCode,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = BrandIndigoPrimary,
                                letterSpacing = 4.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }

                        // Auto-fill button for smooth user experience
                        OutlinedButton(
                            onClick = {
                                enteredCode = generatedCode
                                errorMessage = null
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto-Fill", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 1-Minute (60s) Countdown Timer Display
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCodeExpired) Color.Red.copy(alpha = 0.08f)
                    else if (remainingSeconds <= 15) BrandAmber.copy(alpha = 0.08f)
                    else BrandEmerald.copy(alpha = 0.08f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isCodeExpired) Color.Red.copy(alpha = 0.4f)
                    else if (remainingSeconds <= 15) BrandAmber.copy(alpha = 0.4f)
                    else BrandEmerald.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isCodeExpired) "সময় শেষ (Time Expired)" else "ভেরিফিকেশন কোডের মেয়াদ (1 Minute)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCodeExpired) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = if (isCodeExpired) Color.Red else if (remainingSeconds <= 15) BrandAmber else BrandEmerald
                        )
                    }

                    LinearProgressIndicator(
                        progress = { remainingSeconds / 60f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (remainingSeconds <= 15) Color.Red else BrandEmerald,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    if (isCodeExpired) {
                        Text(
                            text = "১ মিনিট শেষ হয়েছে। দয়া করে পুনরায় কোড পাঠানোর বাটনে চাপ দিন।",
                            fontSize = 11.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "১ মিনিটের মধ্যে ওপরের কোডটি নিচে এন্ট্রি করে একাউন্ট সক্রিয় করুন।",
                            fontSize = 11.sp,
                            color = Slate400,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // OTP Code Input Field
            OutlinedTextField(
                value = enteredCode,
                onValueChange = {
                    if (it.length <= 6) {
                        enteredCode = it.filter { char -> char.isDigit() }
                        errorMessage = null
                    }
                },
                label = { Text("Enter 6-Digit Code (কোডটি লিখুন)") },
                placeholder = { Text("6 digits code") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandIndigoPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // Verify & Activate Account Button
            Button(
                onClick = {
                    if (isCodeExpired) {
                        errorMessage = "কোডটির ১ মিনিটের মেয়াদ শেষ হয়ে গেছে। পুনরায় কোড পাঠান।"
                        return@Button
                    }
                    if (enteredCode.length != 6) {
                        errorMessage = "সম্পূর্ণ ৬-সংখ্যার কোডটি প্রবেশ করান।"
                        return@Button
                    }
                    if (enteredCode != generatedCode) {
                        errorMessage = "ভুল কোড! অ্যাডমিন থেকে প্রেরিত কোডের সাথে মিলছে না।"
                        return@Button
                    }

                    // Code matched! Create the account
                    val (success, msg) = onRegister(
                        firstName.trim(),
                        lastName.trim(),
                        email.trim().lowercase(),
                        phone.trim(),
                        gender,
                        work.trim(),
                        college.trim(),
                        password,
                        selectedAvatar
                    )

                    if (!success) {
                        errorMessage = msg
                    }
                },
                enabled = !isCodeExpired && enteredCode.length == 6,
                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_activate_button")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verify & Activate Account (অ্যাকাউন্ট সক্রিয় করুন)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            // Resend Code & Back Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val freshCode = (100000..999999).random().toString()
                        generatedCode = freshCode
                        onRecordVerificationCode?.invoke(codeSentTarget, freshCode)
                        enteredCode = ""
                        errorMessage = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Resend Code", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        currentStep = 0
                        errorMessage = null
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Info", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AccountSwitcherDialog(
    currentUser: UserProfile,
    allUsers: List<UserProfile>,
    onDismiss: () -> Unit,
    onSwitchUser: (UserProfile) -> Unit,
    onOpenRegister: () -> Unit,
    onOpenAdminPanel: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Accounts & Identity",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onOpenRegister) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp), tint = BrandIndigoPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandIndigoPrimary)
                    }
                }

                // Current Active User
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BrandIndigoLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandIndigoPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val isSuperAdmin = currentUser.userRole == UserRole.SUPER_ADMIN
                            UserAvatar(avatarUrl = currentUser.avatarUrl, name = if (isSuperAdmin) "Admin" else currentUser.name, size = 42.dp)
                            Column {
                                Text(
                                    text = if (isSuperAdmin) "System Administrator" else currentUser.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (isSuperAdmin) "🛡️ Identity Hidden (Stealth Mode)" else currentUser.email.ifBlank { currentUser.phone },
                                    fontSize = 11.sp,
                                    color = if (isSuperAdmin) BrandPurple else Slate400,
                                    fontWeight = if (isSuperAdmin) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (currentUser.userRole == UserRole.SUPER_ADMIN) BrandPurple else BrandIndigoPrimary
                        ) {
                            Text(
                                text = "ACTIVE • ${currentUser.userRole.displayName}",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // If user is Admin, show shortcut to Admin Panel
                if (currentUser.userRole == UserRole.SUPER_ADMIN || currentUser.userRole == UserRole.ADMIN) {
                    Button(
                        onClick = {
                            onOpenAdminPanel()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Main Admin Control Panel", fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Switch to another profile:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400
                )

                // Other accounts list (Admin ID is strictly hidden from regular members)
                val isViewerAdmin = currentUser.userRole == UserRole.SUPER_ADMIN || currentUser.userRole == UserRole.ADMIN
                val availableSwitchProfiles = if (isViewerAdmin) {
                    allUsers.filter { it.id != currentUser.id }
                } else {
                    allUsers.filter { it.id != currentUser.id && it.userRole != UserRole.SUPER_ADMIN && it.userRole != UserRole.ADMIN }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableSwitchProfiles.forEach { user ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(user)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val isUserAdmin = user.userRole == UserRole.SUPER_ADMIN
                                    UserAvatar(avatarUrl = user.avatarUrl, name = if (isUserAdmin) "Admin" else user.name, size = 34.dp)
                                    Column {
                                        Text(text = if (isUserAdmin) "System Administrator" else user.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = if (isUserAdmin) "🛡️ Hidden Admin ID" else user.email.ifBlank { user.phone }, fontSize = 10.sp, color = Slate400)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (user.isSuspended) Color.Red.copy(alpha = 0.2f) else BrandIndigoLight
                                ) {
                                    Text(
                                        text = if (user.isSuspended) "SUSPENDED" else user.userRole.displayName,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (user.isSuspended) Color.Red else BrandIndigoPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
