package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.LuxuryFullWidthBuyButton
import com.example.ui.components.PerfumeBottleArtwork
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.LuxitrViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    viewModel: LuxitrViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentStep by viewModel.currentQuizStep.collectAsStateWithLifecycle()
    val selectedVibe by viewModel.selectedVibe.collectAsStateWithLifecycle()
    val selectedOccasion by viewModel.selectedOccasion.collectAsStateWithLifecycle()
    val selectedSeason by viewModel.selectedSeason.collectAsStateWithLifecycle()
    val selectedIntensity by viewModel.selectedIntensity.collectAsStateWithLifecycle()
    val quizResult by viewModel.quizResult.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .testTag("quiz_screen")
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Quiz Header
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
                    onClick = { viewModel.prevQuizStep() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldPrimary
                    )
                }

                Text(
                    text = "LUXITR",
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.width(40.dp))
            }
        }

        if (currentStep <= 4) {
            // Title & Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Find My Perfume",
                    color = GoldLight,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tell us what you like, and we'll find your perfect match.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Stepper: 1 - 2 - 3 - 4
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (step in 1..4) {
                        val isCurrent = step == currentStep
                        val isPassed = step < currentStep
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCurrent) GoldPrimary else if (isPassed) GoldDark else SurfaceCard
                                )
                                .border(
                                    1.dp,
                                    if (isCurrent) GoldBright else SurfaceBorder,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$step",
                                color = if (isCurrent) ObsidianBlack else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (step < 4) {
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(1.5.dp)
                                    .background(if (step < currentStep) GoldPrimary else SurfaceBorder)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Step Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                when (currentStep) {
                    1 -> {
                        item {
                            Text(
                                text = "What's your vibe?",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val vibes = listOf(
                                Pair("Fresh", "🍃"),
                                Pair("Sweet", "🌸"),
                                Pair("Bold", "🔥"),
                                Pair("Elegant", "⚜️"),
                                Pair("Romantic", "💖"),
                                Pair("Mysterious", "🌙"),
                                Pair("Aquatic", "🌊"),
                                Pair("Woody", "🌲")
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                vibes.forEach { (vibe, icon) ->
                                    val isSelected = selectedVibe == vibe
                                    QuizSelectableChip(
                                        label = vibe,
                                        icon = icon,
                                        isSelected = isSelected,
                                        onClick = { viewModel.selectQuizVibe(vibe) }
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        item {
                            Text(
                                text = "Where will you wear it?",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val occasions = listOf(
                                Pair("Office / Work", "💼"),
                                Pair("Date Night", "🍷"),
                                Pair("Parties & Clubs", "🎉"),
                                Pair("Signature Daily", "👑"),
                                Pair("Gym & Sports", "⚡"),
                                Pair("Weddings & Galas", "✨")
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                occasions.forEach { (occ, icon) ->
                                    val isSelected = selectedOccasion == occ
                                    QuizSelectableChip(
                                        label = occ,
                                        icon = icon,
                                        isSelected = isSelected,
                                        onClick = { viewModel.selectQuizOccasion(occ) }
                                    )
                                }
                            }
                        }
                    }
                    3 -> {
                        item {
                            Text(
                                text = "Preferred Season & Weather?",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val seasons = listOf(
                                Pair("Summer / Hot", "☀️"),
                                Pair("Winter / Cool", "❄️"),
                                Pair("Monsoon / Rainy", "🌧️"),
                                Pair("All-Season Universal", "🌍")
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                seasons.forEach { (s, icon) ->
                                    val isSelected = selectedSeason == s
                                    QuizSelectableChip(
                                        label = s,
                                        icon = icon,
                                        isSelected = isSelected,
                                        onClick = { viewModel.selectQuizSeason(s) }
                                    )
                                }
                            }
                        }
                    }
                    4 -> {
                        item {
                            Text(
                                text = "Desired Projection & Intensity?",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val intensities = listOf(
                                Pair("Subtle & Intimate", "🕊️"),
                                Pair("Balanced & Noticeable", "⚖️"),
                                Pair("Heavy Beast-Mode", "🦁")
                            )

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                intensities.forEach { (inte, icon) ->
                                    val isSelected = selectedIntensity == inte
                                    QuizSelectableChip(
                                        label = inte,
                                        icon = icon,
                                        isSelected = isSelected,
                                        onClick = { viewModel.selectQuizIntensity(inte) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Next Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                LuxuryFullWidthBuyButton(
                    onClick = { viewModel.nextQuizStep() },
                    label = if (currentStep == 4) "REVEAL MY SOUL SCENT ->" else "Next ->",
                    testTag = "quiz_next_step_btn"
                )
            }
        } else {
            // Step 5: Quiz Results
            quizResult?.let { result ->
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(GoldPrimary.copy(alpha = 0.15f))
                                .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "✨ ${result.matchPercentage}% SOUL SCENT MATCH",
                                color = GoldBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Match Perfume Artwork
                        PerfumeBottleArtwork(
                            perfume = result.primaryMatch,
                            sizeDp = 180.dp,
                            showAtmosphericAura = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = result.primaryMatch.name,
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif
                        )

                        Text(
                            text = result.primaryMatch.subtitle,
                            color = GoldLight,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // AI Verdict
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceCardElevated)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "LUXYS AI Verdict",
                                        color = GoldLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = result.luxysVerdict,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Secondary runner-up match
                        Text(
                            text = "Also consider: ${result.secondaryMatch.name} (${result.secondaryMatch.price})",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Buy Now CTA for Result
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Column {
                        LuxuryFullWidthBuyButton(
                            onClick = {
                                viewModel.openBuyNow(context, result.primaryMatch)
                            },
                            label = "BUY ${result.primaryMatch.name.uppercase()} ON LUXITR.COM ->",
                            testTag = "quiz_result_buy_btn"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Retake Fragrance Quiz",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.startQuiz() }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizSelectableChip(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) GoldPrimary else SurfaceCard)
            .border(
                1.dp,
                if (isSelected) GoldBright else SurfaceBorder,
                RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = if (isSelected) ObsidianBlack else TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
