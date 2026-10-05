package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FragranceScoreMeter
import com.example.ui.components.LuxuryFullWidthBuyButton
import com.example.ui.components.PerfumeBottleArtwork
import com.example.ui.theme.GoldBright
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

@Composable
fun DetailScreen(
    perfumeId: String,
    viewModel: LuxitrViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val perfume = viewModel.repository.getPerfumeById(perfumeId)
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val isFav = favoriteIds.contains(perfumeId)

    if (perfume == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(ObsidianBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("Fragrance not found", color = TextPrimary)
        }
        return
    }

    Column(
        modifier = modifier
            .testTag("detail_screen_${perfume.id}")
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Detail Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldPrimary
                    )
                }

                // Brand Emblem
                Text(
                    text = "LUXITR",
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Serif
                )

                Row {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(perfume.id) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) Color(0xFFFF5252) else GoldPrimary
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Check out ${perfume.name} on LUXITR")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Discover ${perfume.name} (${perfume.subtitle}) on LUXITR: ${perfume.searchUrl}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Fragrance"))
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = GoldPrimary
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Hero Flacon Artwork
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PerfumeBottleArtwork(
                        perfume = perfume,
                        sizeDp = 220.dp,
                        showAtmosphericAura = true
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Fragrance Title & Subtitle
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = perfume.name,
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = perfume.price,
                                color = GoldPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = perfume.originalPrice,
                                color = TextMuted,
                                fontSize = 14.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }

                    Text(
                        text = perfume.subtitle,
                        color = GoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            // 3 Attribute Badges (Best For, Weather, Personality)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Best For
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Best For", color = TextMuted, fontSize = 9.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(perfume.bestFor, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }

                    // Weather
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Weather", color = TextMuted, fontSize = 9.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(perfume.weather, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }

                    // Personality
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Personality", color = TextMuted, fontSize = 9.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(perfume.personality, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Fragrance Profile Meters (Projection, Sweetness, Freshness, Warmth)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCardElevated)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        FragranceScoreMeter(
                            label = "Projection",
                            value = when (perfume.projectionScore) {
                                5 -> "Beast"
                                4 -> "Heavy"
                                3 -> "Moderate"
                                else -> "Intimate"
                            },
                            score = perfume.projectionScore
                        )
                        FragranceScoreMeter(
                            label = "Sweetness",
                            value = when (perfume.sweetnessScore) {
                                5, 4 -> "High"
                                3 -> "Medium"
                                else -> "Low"
                            },
                            score = perfume.sweetnessScore
                        )
                        FragranceScoreMeter(
                            label = "Freshness",
                            value = when (perfume.freshnessScore) {
                                5, 4 -> "High"
                                3 -> "Medium"
                                else -> "Low"
                            },
                            score = perfume.freshnessScore
                        )
                        FragranceScoreMeter(
                            label = "Warmth",
                            value = when (perfume.warmthScore) {
                                5, 4 -> "High"
                                3 -> "Medium"
                                else -> "Low"
                            },
                            score = perfume.warmthScore
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // About This Fragrance Storytelling
            item {
                Text(
                    text = "About This Fragrance",
                    color = GoldLight,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = perfume.description,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Olfactory Notes Pyramid
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Olfactory Notes",
                            color = GoldLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row {
                            Text("Top Notes: ", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(perfume.topNotes.joinToString(" • "), color = TextPrimary, fontSize = 12.sp)
                        }

                        Row {
                            Text("Heart Notes: ", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(perfume.heartNotes.joinToString(" • "), color = TextPrimary, fontSize = 12.sp)
                        }

                        Row {
                            Text("Base Notes: ", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(perfume.baseNotes.joinToString(" • "), color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // LUXYS Persuasion Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF241D12),
                                    SurfaceCard,
                                    ObsidianBlack
                                )
                            )
                        )
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "LUXYS Insight",
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "LUXYS Fragrance Intelligence Verdict",
                                color = GoldBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = perfume.luxysPitch,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bottom Sticky Golden CTA BUY NOW Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            LuxuryFullWidthBuyButton(
                onClick = {
                    viewModel.openBuyNow(context, perfume)
                },
                label = "BUY NOW ->",
                testTag = "detail_buy_now_btn"
            )
        }
    }
}
