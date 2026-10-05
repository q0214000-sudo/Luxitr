package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.BottleVisualShape
import com.example.data.model.Perfume
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun PerfumeBottleArtwork(
    perfume: Perfume,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 140.dp,
    showAtmosphericAura: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val accentColor = Color(perfume.accentColorHex)

    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = if (showAtmosphericAura) glowAlpha * 0.4f else 0.1f),
                        Color(0xFF141318),
                        Color(0xFF09080C)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            val w = size.width
            val h = size.height

            // Atmospheric backdrop particles/rays
            if (showAtmosphericAura) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = glowAlpha * 0.7f), Color.Transparent),
                        center = Offset(w * 0.5f, h * 0.6f),
                        radius = w * 0.55f
                    )
                )
            }

            when (perfume.bottleShape) {
                BottleVisualShape.CYLINDER_AQUA -> drawAquaCylinderBottle(w, h, accentColor)
                BottleVisualShape.CRYSTAL_FIRE -> drawFireCrystalBottle(w, h, accentColor)
                BottleVisualShape.ROYAL_FLACON -> drawRoyalFlaconBottle(w, h, accentColor)
                BottleVisualShape.RUBY_RECTANGLE -> drawRubyRectangleBottle(w, h, accentColor)
                BottleVisualShape.MIDNIGHT_SQUARE -> drawMidnightSquareBottle(w, h, accentColor)
                BottleVisualShape.AMBER_GEM -> drawAmberGemBottle(w, h, accentColor)
                BottleVisualShape.ELIXIR_FLASK -> drawElixirFlaskBottle(w, h, accentColor)
                BottleVisualShape.CHERRY_DECANTER -> drawCherryDecanterBottle(w, h, accentColor)
            }
        }
    }
}

// 1. Hawas ICE - Cyan/Blue Icy Cylinder Bottle
private fun DrawScope.drawAquaCylinderBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Cap
    drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFFE0E0E0), Color(0xFF9E9E9E), Color(0xFFFAFAFA))),
        topLeft = Offset(cx - w * 0.12f, h * 0.12f),
        size = Size(w * 0.24f, h * 0.14f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Silver Neck
    drawRect(
        color = Color(0xFFB0BEC5),
        topLeft = Offset(cx - w * 0.08f, h * 0.26f),
        size = Size(w * 0.16f, h * 0.05f)
    )
    // Bottle Body
    val bodyPath = Path().apply {
        moveTo(cx - w * 0.28f, h * 0.35f)
        cubicTo(cx - w * 0.32f, h * 0.36f, cx - w * 0.32f, h * 0.75f, cx - w * 0.26f, h * 0.85f)
        lineTo(cx + w * 0.26f, h * 0.85f)
        cubicTo(cx + w * 0.32f, h * 0.75f, cx + w * 0.32f, h * 0.36f, cx + w * 0.28f, h * 0.35f)
        close()
    }
    drawPath(
        path = bodyPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE1F5FE),
                accent,
                Color(0xFF0277BD)
            )
        )
    )
    // Glass highlight
    drawRoundRect(
        color = Color.White.copy(alpha = 0.45f),
        topLeft = Offset(cx - w * 0.22f, h * 0.42f),
        size = Size(w * 0.08f, h * 0.35f),
        cornerRadius = CornerRadius(10f, 10f)
    )
    // Arabic calligraphy accent line
    drawLine(
        color = Color.White.copy(alpha = 0.8f),
        start = Offset(cx - w * 0.14f, h * 0.58f),
        end = Offset(cx + w * 0.14f, h * 0.58f),
        strokeWidth = 2.5f
    )
}

// 2. God of Fire - Fiery Crystal Flacon
private fun DrawScope.drawFireCrystalBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Golden Crown Cap
    drawRoundRect(
        brush = Brush.linearGradient(listOf(GoldLight, GoldPrimary, GoldBright)),
        topLeft = Offset(cx - w * 0.14f, h * 0.10f),
        size = Size(w * 0.28f, h * 0.15f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Bottle Body - Dark Obsidian with Flaming Magma Heart
    val body = Path().apply {
        moveTo(cx - w * 0.22f, h * 0.30f)
        lineTo(cx - w * 0.30f, h * 0.55f)
        lineTo(cx - w * 0.24f, h * 0.86f)
        lineTo(cx + w * 0.24f, h * 0.86f)
        lineTo(cx + w * 0.30f, h * 0.55f)
        lineTo(cx + w * 0.22f, h * 0.30f)
        close()
    }
    drawPath(
        path = body,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E1010),
                accent,
                Color(0xFFBF360C),
                Color(0xFF0D0202)
            )
        )
    )
    // Golden Emblem Snake/Dragon in Center
    drawCircle(
        brush = Brush.radialGradient(listOf(GoldLight, GoldPrimary)),
        radius = w * 0.09f,
        center = Offset(cx, h * 0.56f)
    )
    drawPath(
        path = body,
        color = GoldPrimary.copy(alpha = 0.5f),
        style = Stroke(width = 2f)
    )
}

// 3. Creed Aventus - Regal Clear / Golden Shoulder Flacon
private fun DrawScope.drawRoyalFlaconBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Crown Stopper
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF2C2C2C), Color(0xFF111111))),
        topLeft = Offset(cx - w * 0.15f, h * 0.12f),
        size = Size(w * 0.30f, h * 0.13f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Silver Neck Ring
    drawRect(
        color = Color(0xFFCFD8DC),
        topLeft = Offset(cx - w * 0.10f, h * 0.25f),
        size = Size(w * 0.20f, h * 0.05f)
    )
    // Clear Top Shoulder
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.25f))),
        topLeft = Offset(cx - w * 0.27f, h * 0.30f),
        size = Size(w * 0.54f, h * 0.22f),
        cornerRadius = CornerRadius(8f, 8f)
    )
    // Black Leather Band / Gold Plate
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF000000))),
        topLeft = Offset(cx - w * 0.28f, h * 0.52f),
        size = Size(w * 0.56f, h * 0.34f),
        cornerRadius = CornerRadius(8f, 8f)
    )
    // Golden Knight Emblem
    drawRoundRect(
        brush = Brush.linearGradient(listOf(GoldLight, GoldPrimary)),
        topLeft = Offset(cx - w * 0.16f, h * 0.58f),
        size = Size(w * 0.32f, h * 0.18f),
        cornerRadius = CornerRadius(4f, 4f)
    )
}

// 4. Dunhill Desire - Ruby Red Curved Flask
private fun DrawScope.drawRubyRectangleBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Offset Chrome Cap on Left
    drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFFEEEEEE), Color(0xFF9E9E9E))),
        topLeft = Offset(cx - w * 0.24f, h * 0.14f),
        size = Size(w * 0.18f, h * 0.14f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Connecting Hinge Arm
    drawLine(
        color = Color(0xFFBDBDBD),
        start = Offset(cx - w * 0.06f, h * 0.22f),
        end = Offset(cx + w * 0.16f, h * 0.22f),
        strokeWidth = 4f
    )
    // Ruby Red Bottle
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF5252),
                accent,
                Color(0xFF880E4F),
                Color(0xFF4A0012)
            )
        ),
        topLeft = Offset(cx - w * 0.27f, h * 0.28f),
        size = Size(w * 0.54f, h * 0.58f),
        cornerRadius = CornerRadius(14f, 14f)
    )
    // Glass Bevel highlights
    drawRoundRect(
        color = Color.White.copy(alpha = 0.3f),
        topLeft = Offset(cx - w * 0.22f, h * 0.33f),
        size = Size(w * 0.06f, h * 0.48f),
        cornerRadius = CornerRadius(6f, 6f)
    )
}

// 5. Bleu De Chanel - Midnight Deep Blue Square Decanter
private fun DrawScope.drawMidnightSquareBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Magnetic Black Cap
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF212121), Color(0xFF000000))),
        topLeft = Offset(cx - w * 0.16f, h * 0.13f),
        size = Size(w * 0.32f, h * 0.16f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Deep Midnight Glass Body
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E3A8A),
                Color(0xFF0F172A),
                Color(0xFF020617)
            )
        ),
        topLeft = Offset(cx - w * 0.28f, h * 0.29f),
        size = Size(w * 0.56f, h * 0.56f),
        cornerRadius = CornerRadius(8f, 8f)
    )
    // Golden Minimalist Type Label
    drawRect(
        color = GoldPrimary.copy(alpha = 0.85f),
        topLeft = Offset(cx - w * 0.16f, h * 0.52f),
        size = Size(w * 0.32f, 2f)
    )
    drawRect(
        color = Color.White.copy(alpha = 0.75f),
        topLeft = Offset(cx - w * 0.10f, h * 0.56f),
        size = Size(w * 0.20f, 1.5f)
    )
    // Beveled edges
    drawRoundRect(
        color = Color(0xFF60A5FA).copy(alpha = 0.25f),
        topLeft = Offset(cx - w * 0.25f, h * 0.32f),
        size = Size(w * 0.50f, h * 0.50f),
        cornerRadius = CornerRadius(6f, 6f),
        style = Stroke(width = 1.5f)
    )
}

// 6. Baccarat Rouge 540 - Amber & Crimson Crystal Gem Decanter
private fun DrawScope.drawAmberGemBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Golden Stopper
    drawRoundRect(
        brush = Brush.linearGradient(listOf(GoldBright, GoldPrimary, GoldDark)),
        topLeft = Offset(cx - w * 0.16f, h * 0.12f),
        size = Size(w * 0.32f, h * 0.16f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Heavy Crystal Glass Bottle
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD54F).copy(alpha = 0.6f),
                accent,
                Color(0xFFD84315),
                Color(0xFF3E2723)
            )
        ),
        topLeft = Offset(cx - w * 0.26f, h * 0.28f),
        size = Size(w * 0.52f, h * 0.58f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Red Label with Gold Border
    drawRoundRect(
        color = Color(0xFFB71C1C),
        topLeft = Offset(cx - w * 0.18f, h * 0.44f),
        size = Size(w * 0.36f, h * 0.26f),
        cornerRadius = CornerRadius(2f, 2f)
    )
    drawRoundRect(
        color = GoldBright,
        topLeft = Offset(cx - w * 0.18f, h * 0.44f),
        size = Size(w * 0.36f, h * 0.26f),
        cornerRadius = CornerRadius(2f, 2f),
        style = Stroke(width = 1.5f)
    )
}

// 7. Sauvage Elixir - Dark Midnight Blue Lacquered Flask
private fun DrawScope.drawElixirFlaskBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Ribbed Midnight Cap
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF282828), Color(0xFF0A0A0A))),
        topLeft = Offset(cx - w * 0.15f, h * 0.12f),
        size = Size(w * 0.30f, h * 0.18f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Midnight Blue Chubby Flask
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A237E),
                Color(0xFF0D1333),
                Color(0xFF050714)
            )
        ),
        topLeft = Offset(cx - w * 0.28f, h * 0.30f),
        size = Size(w * 0.56f, h * 0.54f),
        cornerRadius = CornerRadius(16f, 16f)
    )
    // Silver Inset Plate
    drawRoundRect(
        color = Color(0xFFCFD8DC),
        topLeft = Offset(cx - w * 0.18f, h * 0.46f),
        size = Size(w * 0.36f, h * 0.22f),
        cornerRadius = CornerRadius(4f, 4f),
        style = Stroke(width = 1.5f)
    )
}

// 8. Lost Cherry - Rich Cherry Glass Decanter
private fun DrawScope.drawCherryDecanterBottle(w: Float, h: Float, accent: Color) {
    val cx = w * 0.5f
    // Translucent Cherry Cap
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFFD81B60), Color(0xFF880E4F))),
        topLeft = Offset(cx - w * 0.15f, h * 0.12f),
        size = Size(w * 0.30f, h * 0.16f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Architectural Cherry Glass Flacon
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF4081),
                accent,
                Color(0xFF4A0020)
            )
        ),
        topLeft = Offset(cx - w * 0.26f, h * 0.28f),
        size = Size(w * 0.52f, h * 0.58f),
        cornerRadius = CornerRadius(4f, 4f)
    )
    // Pinkish-White Label
    drawRoundRect(
        color = Color(0xFFFCE4EC),
        topLeft = Offset(cx - w * 0.16f, h * 0.46f),
        size = Size(w * 0.32f, h * 0.22f),
        cornerRadius = CornerRadius(2f, 2f)
    )
}
