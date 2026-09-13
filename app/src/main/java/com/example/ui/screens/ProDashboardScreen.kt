package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DayMetric
import com.example.model.UserProfile
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandAmberBg
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandEmeraldBg
import com.example.ui.theme.BrandIndigoLight
import com.example.ui.theme.BrandIndigoPrimary
import com.example.ui.theme.BrandIndigoSecondary
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700

@Composable
fun ProDashboardScreen(
    user: UserProfile,
    metrics: List<DayMetric>,
    onFilterTopicClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("pro_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Pro Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PROFESSIONAL DASHBOARD",
                            color = BrandIndigoSecondary,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BrandEmeraldBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandEmerald.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(BrandEmerald)
                                )
                                Text(
                                    text = "Status: Active",
                                    color = BrandEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = "Creator Analytics & Growth",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Track your question reach, community answers engagement, and monetization growth in real time.",
                        color = Slate400,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // 2. Creator Level Progress Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BrandIndigoPrimary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            UserAvatar(avatarUrl = user.avatarUrl, name = user.name, size = 44.dp)
                            Column {
                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Level 4 Pro Creator",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${user.reputation} Rep pts",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    // Progress to Next Level
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Next milestone: Master Answerer (3,000 pts)",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                            Text(
                                text = "80%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        LinearProgressIndicator(
                            progress = { 2420f / 3000f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = BrandPink,
                            trackColor = Color.White.copy(alpha = 0.25f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${user.answersGiven}", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(text = "Answers Given", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "98.4%", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(text = "Helpful Rating", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+10 pts", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(text = "Per Answer", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 3. 4-Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProMetricCard(
                        title = "Post Reach",
                        value = user.postReach,
                        change = "+14.2% this week",
                        modifier = Modifier.weight(1f)
                    )
                    ProMetricCard(
                        title = "Q&A Answers",
                        value = "1,280",
                        change = "+8.1% this week",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProMetricCard(
                        title = "Net Followers",
                        value = "3,490",
                        change = "+320 new",
                        modifier = Modifier.weight(1f)
                    )
                    ProMetricCard(
                        title = "Engagement Rate",
                        value = user.engagementRate,
                        change = "Top 5% Creators",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Interactive Line Chart (Audience & Reach Growth)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("growth_chart_card")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Audience & Reach Growth",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tap points to inspect values",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Last 7 Days",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(BrandIndigoSecondary)
                            )
                            Text(text = "Post Impressions", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate400)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(BrandPurple)
                            )
                            Text(text = "Q&A Reach", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate400)
                        }
                    }

                    // Custom Canvas Chart
                    ReachGrowthCanvas(metrics = metrics)
                }
            }
        }

        // 5. Trending Topics to Answer
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = BrandAmber
                        )
                        Text(
                            text = "High-Demand \"Ask ME\" Topics",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    TrendingTopicRow(
                        topic = "#WebDevelopment",
                        stats = "1.4k Questions asked today",
                        onClick = { onFilterTopicClick("WebDevelopment") }
                    )
                    TrendingTopicRow(
                        topic = "#ArtificialIntelligence",
                        stats = "980 Questions asked today",
                        onClick = { onFilterTopicClick("ArtificialIntelligence") }
                    )
                    TrendingTopicRow(
                        topic = "#CareerAdvice",
                        stats = "650 Questions asked today",
                        onClick = { onFilterTopicClick("CareerAdvice") }
                    )
                }
            }
        }

        // 6. Creator Monetization Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BrandEmeraldBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = BrandEmerald)
                        }
                        Column {
                            Text(text = "Estimated Rewards", fontSize = 12.sp, color = Slate400)
                            Text(text = "$348.50 USD", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Withdraw", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProMetricCard(
    title: String,
    value: String,
    change: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Slate400
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = null,
                    tint = BrandEmerald,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = change,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandEmerald
                )
            }
        }
    }
}

@Composable
fun TrendingTopicRow(
    topic: String,
    stats: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = topic,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BrandIndigoSecondary
            )
            Text(
                text = stats,
                fontSize = 11.sp,
                color = Slate400
            )
        }
        Icon(
            imageVector = Icons.Default.ArrowOutward,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ReachGrowthCanvas(
    metrics: List<DayMetric>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val maxVal = 10000f

    Column(modifier = modifier.fillMaxWidth()) {
        if (selectedIndex != null && selectedIndex!! in metrics.indices) {
            val selected = metrics[selectedIndex!!]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandIndigoLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandIndigoSecondary)
                ) {
                    Text(
                        text = "${selected.day}: Impressions ${selected.impressions.toInt()} | Reach ${selected.qaReach.toInt()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandIndigoPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .pointerInput(metrics) {
                    detectTapGestures { offset ->
                        val stepX = size.width / (metrics.size - 1)
                        val index = ((offset.x + stepX / 2) / stepX).toInt().coerceIn(0, metrics.size - 1)
                        selectedIndex = index
                    }
                }
        ) {
            val width = size.width
            val height = size.height
            val bottomPadding = 30f
            val topPadding = 20f
            val graphHeight = height - bottomPadding - topPadding

            val count = metrics.size
            val stepX = width / (count - 1)

            // Draw horizontal light grid lines
            for (i in 0..3) {
                val y = topPadding + (graphHeight / 3) * i
                drawLine(
                    color = Color.Gray.copy(alpha = 0.15f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }

            // Generate Path for Impressions (Indigo)
            val path1 = Path()
            val fillPath1 = Path()
            fillPath1.moveTo(0f, height - bottomPadding)

            for (i in 0 until count) {
                val x = i * stepX
                val normalizedY = (metrics[i].impressions / maxVal).coerceIn(0f, 1f)
                val y = topPadding + graphHeight * (1f - normalizedY)

                if (i == 0) {
                    path1.moveTo(x, y)
                    fillPath1.lineTo(x, y)
                } else {
                    val prevX = (i - 1) * stepX
                    val prevNormY = (metrics[i - 1].impressions / maxVal).coerceIn(0f, 1f)
                    val prevY = topPadding + graphHeight * (1f - prevNormY)
                    val cx = (prevX + x) / 2f
                    path1.cubicTo(cx, prevY, cx, y, x, y)
                    fillPath1.cubicTo(cx, prevY, cx, y, x, y)
                }
            }
            fillPath1.lineTo((count - 1) * stepX, height - bottomPadding)
            fillPath1.close()

            // Fill under curve 1
            drawPath(
                path = fillPath1,
                brush = Brush.verticalGradient(
                    colors = listOf(BrandIndigoSecondary.copy(alpha = 0.25f), Color.Transparent),
                    startY = topPadding,
                    endY = height - bottomPadding
                )
            )

            // Stroke curve 1
            drawPath(
                path = path1,
                color = BrandIndigoSecondary,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Generate Path for QA Reach (Purple)
            val path2 = Path()
            val fillPath2 = Path()
            fillPath2.moveTo(0f, height - bottomPadding)

            for (i in 0 until count) {
                val x = i * stepX
                val normalizedY = (metrics[i].qaReach / maxVal).coerceIn(0f, 1f)
                val y = topPadding + graphHeight * (1f - normalizedY)

                if (i == 0) {
                    path2.moveTo(x, y)
                    fillPath2.lineTo(x, y)
                } else {
                    val prevX = (i - 1) * stepX
                    val prevNormY = (metrics[i - 1].qaReach / maxVal).coerceIn(0f, 1f)
                    val prevY = topPadding + graphHeight * (1f - prevNormY)
                    val cx = (prevX + x) / 2f
                    path2.cubicTo(cx, prevY, cx, y, x, y)
                    fillPath2.cubicTo(cx, prevY, cx, y, x, y)
                }
            }
            fillPath2.lineTo((count - 1) * stepX, height - bottomPadding)
            fillPath2.close()

            // Fill under curve 2
            drawPath(
                path = fillPath2,
                brush = Brush.verticalGradient(
                    colors = listOf(BrandPurple.copy(alpha = 0.2f), Color.Transparent),
                    startY = topPadding,
                    endY = height - bottomPadding
                )
            )

            // Stroke curve 2
            drawPath(
                path = path2,
                color = BrandPurple,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw data points & day text
            for (i in 0 until count) {
                val x = i * stepX
                val y1 = topPadding + graphHeight * (1f - (metrics[i].impressions / maxVal).coerceIn(0f, 1f))
                val y2 = topPadding + graphHeight * (1f - (metrics[i].qaReach / maxVal).coerceIn(0f, 1f))

                // Point 1
                drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(x, y1))
                drawCircle(color = BrandIndigoSecondary, radius = 3.dp.toPx(), center = Offset(x, y1))

                // Point 2
                drawCircle(color = Color.White, radius = 5.dp.toPx(), center = Offset(x, y2))
                drawCircle(color = BrandPurple, radius = 3.dp.toPx(), center = Offset(x, y2))

                // Draw Day Label at bottom
                drawContext.canvas.nativeCanvas.drawText(
                    metrics[i].day,
                    x,
                    height - 5f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }
        }
    }
}
