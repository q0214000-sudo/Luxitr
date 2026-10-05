package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Perfume
import com.example.ui.components.HomeCategoryTile
import com.example.ui.components.LuxitrTopBar
import com.example.ui.components.LuxuryBuyNowPillButton
import com.example.ui.components.PerfumeBottleArtwork
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceBorderHighlight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.LuxitrViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    viewModel: LuxitrViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var quickInputText by remember { mutableStateOf("") }

    val categoryList = listOf(
        Pair("Find My Perfume", "✨"),
        Pair("Best for Office", "💼"),
        Pair("Best for Date", "💖"),
        Pair("Best for Party", "👥"),
        Pair("Fresh Perfumes", "🍃"),
        Pair("Sweet Perfumes", "🌸"),
        Pair("Night Perfumes", "🌙"),
        Pair("Explore All Perfumes", "▦")
    )

    Column(
        modifier = modifier
            .testTag("home_screen")
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Luxury Top Bar
        LuxitrTopBar(
            title = "LUXITR",
            onMenuClick = {
                viewModel.navigateTo(ScreenDestination.Collection)
            },
            onWebActionClick = {
                viewModel.navigateTo(
                    ScreenDestination.InAppWeb(
                        url = "https://luxitr.com",
                        perfumeName = "LUXITR Fragrances"
                    )
                )
            },
            actionIcon = {
                IconButton(
                    onClick = {
                        viewModel.navigateTo(ScreenDestination.Favorites)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Saved Favorites",
                        tint = GoldPrimary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header Typography Banner
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Welcome to",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "LUXYS",
                        color = GoldPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 3.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Your Personal Fragrance Intelligence.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // AI Bot Introduction Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    SurfaceCardElevated,
                                    SurfaceCard,
                                    ObsidianBlack
                                )
                            )
                        )
                        .border(1.dp, GoldPrimary.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                        .clickable {
                            viewModel.navigateTo(ScreenDestination.Chat)
                        }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        // AI Avatar badge
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(GoldLight, GoldPrimary, GoldDark)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "LUXYS AI",
                                tint = ObsidianBlack,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hi! I'm LUXYS ✨",
                                color = GoldLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your personal perfume assistant. Tell me what you're looking for, and I'll help you find the perfect fragrance from LUXITR.",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap here to chat with LUXYS ->",
                                color = GoldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 8 Category Tiles (2 columns)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (i in categoryList.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val first = categoryList[i]
                            val isFirstHighlighted = i == 0
                            HomeCategoryTile(
                                title = first.first,
                                icon = first.second,
                                isHighlighted = isFirstHighlighted,
                                onClick = {
                                    viewModel.handleQuickCategory(
                                        if (first.second.isNotEmpty()) "${first.second} ${first.first}" else first.first
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                testTag = "cat_btn_${i}"
                            )

                            if (i + 1 < categoryList.size) {
                                val second = categoryList[i + 1]
                                HomeCategoryTile(
                                    title = second.first,
                                    icon = second.second,
                                    isHighlighted = false,
                                    onClick = {
                                        viewModel.handleQuickCategory(
                                            if (second.second.isNotEmpty()) "${second.second} ${second.first}" else second.first
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    testTag = "cat_btn_${i + 1}"
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Featured Scent Spotlight
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LUXITR Signature Icons",
                        color = GoldLight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All ->",
                        color = GoldPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            viewModel.navigateTo(ScreenDestination.Collection)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(viewModel.repository.catalog.take(4)) { perfume ->
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceCard)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.navigateTo(ScreenDestination.Detail(perfume.id))
                                }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                PerfumeBottleArtwork(
                                    perfume = perfume,
                                    sizeDp = 90.dp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = perfume.name,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = perfume.price,
                                    color = GoldPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LuxuryBuyNowPillButton(
                                    onClick = {
                                        viewModel.openBuyNow(context, perfume)
                                    },
                                    testTag = "home_buy_${perfume.id}"
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Chat Launcher Bar (As shown in screenshot screen 3)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = quickInputText,
                    onValueChange = { quickInputText = it },
                    placeholder = {
                        Text(
                            text = "Type a message...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .testTag("home_chat_input")
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (quickInputText.isNotBlank()) {
                                val text = quickInputText
                                quickInputText = ""
                                viewModel.navigateTo(ScreenDestination.Chat)
                                viewModel.sendMessageToLuxys(text)
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .testTag("home_send_btn")
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(GoldLight, GoldPrimary, GoldDark)
                            )
                        )
                        .clickable {
                            val text = quickInputText.ifBlank { "What perfume is best for me?" }
                            quickInputText = ""
                            viewModel.navigateTo(ScreenDestination.Chat)
                            viewModel.sendMessageToLuxys(text)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = ObsidianBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
