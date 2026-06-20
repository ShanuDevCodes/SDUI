package `in`.shanudevcodes.sdui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * SduiBannerCard — a fully custom server-driven component.
 *
 * Unique features impossible to replicate with built-in primitives:
 *   - Animated pulsing live indicator dot
 *   - Dual-layer gradient (image scrim + colour tint overlay)
 *   - Eyebrow + title + subtitle typography trio with independent styling
 *   - Pill badge with custom accent colour
 *   - Optional CTA button wired to SDUI actions
 *   - Custom corner radius + drop shadow as a single atomic unit
 *
 * Props (all optional with sensible defaults):
 *   imageUrl       — background image URL
 *   eyebrow        — small ALL-CAPS label above the title (e.g. "FEATURED")
 *   title          — main headline
 *   subtitle       — secondary body text
 *   badge          — pill badge text (e.g. "-40%", "NEW", "LIVE")
 *   badgeColor     — hex background for the badge pill
 *   ctaLabel       — CTA button label; hidden if empty
 *   gradientStart  — hex for the top gradient stop (default: transparent)
 *   gradientEnd    — hex for the bottom gradient stop (default: near-black)
 *   accentColor    — hex used for eyebrow text + CTA button
 *   radius         — corner radius dp (default 24)
 *   height         — card height dp (default 220)
 *   live           — bool; show animated pulse dot when true
 *
 * Actions:
 *   onClick        — fired when the CTA button is tapped
 */
@Composable
fun SduiBannerCard(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val imageUrl     = component.stringProp("imageUrl")
    val eyebrow      = component.stringProp("eyebrow")
    val title        = component.stringProp("title", "Untitled")
    val subtitle     = component.stringProp("subtitle")
    val badge        = component.stringProp("badge")
    val badgeColor   = parseColor(component.stringProp("badgeColor", "#FF6650A4"))
    val ctaLabel     = component.stringProp("ctaLabel")
    val gradientStart = parseColor(component.stringProp("gradientStart", "#00000000"))
    val gradientEnd   = parseColor(component.stringProp("gradientEnd", "#FF0F0D13"))
    val accentColor   = parseColor(component.stringProp("accentColor", "#FFD0BCFF"))
    val radius       = component.intProp("radius", 24)
    val cardHeight   = component.intProp("height", 220)
    val live         = component.booleanProp("live", false)
    val onClickAction = component.action("onClick")
    val onAction     = LocalSduiActionHandler.current

    // Pulse animation for the live indicator
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulseAlpha"
    )

    val shape = RoundedCornerShape(radius.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight.dp)
            .shadow(elevation = 12.dp, shape = shape, clip = false)
            .clip(shape)
    ) {
        // Layer 1: background image
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        // Layer 2: gradient scrim
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(gradientStart, gradientEnd))
                )
        )

        // Layer 3: content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row: badge + live dot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (badge.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(badgeColor)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Spacer(Modifier.width(1.dp))
                }

                if (live) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5252).copy(alpha = pulse))
                        )
                        Text(
                            text = "LIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF5252)
                        )
                    }
                }
            }

            // Bottom: text + CTA
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (eyebrow.isNotEmpty()) {
                    Text(
                        text = eyebrow.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.5.sp
                    )
                }
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.75f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (ctaLabel.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onClickAction?.let { onAction(it) } },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        shape = CircleShape,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = ctaLabel,
                            color = Color(0xFF1C1B1F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

private fun parseColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    return try {
        when (clean.length) {
            6 -> Color(clean.toLong(16) or 0xFF000000)
            8 -> Color(clean.toLong(16))
            else -> Color.Unspecified
        }
    } catch (e: Exception) { Color.Unspecified }
}
