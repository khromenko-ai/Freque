package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.scale
import kotlin.math.*

@Composable
fun NodeVisualization(nodeId: String, accentColor: Color) {
    when (nodeId) {
        "intro" -> IntroViz(accentColor)
        "time_reality" -> TimeViz(accentColor)
        "spectral" -> SpectralViz(accentColor)
        "observer" -> ObserverViz(accentColor)
        "hierarchy" -> HierarchyViz(accentColor)
        "holography" -> HolographyViz(accentColor)
        "ring" -> GeometryViz(accentColor)
        "crit_1_lagrangian" -> LagrangianPacketViz(accentColor)
        "crit_2_gabor" -> GaborUncertaintyViz(accentColor)
        "crit_3_unitarity" -> UnitarityRingViz(accentColor)
        "crit_4_entropy" -> HolographicBekensteinViz(accentColor)
        "zero_symmetry" -> ZeroSymmetryViz(accentColor)
        "scale_space" -> ScaleSpaceViz(accentColor)
        else -> IntroViz(accentColor)
    }
}

@Composable
private fun IntroViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("intro")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing)),
        label = "rotation"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "scale"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2
        val cy = size.height / 2
        val radius = size.minDimension * 0.3f * scale
        
        translate(cx, cy) {
            for (i in 0 until 16) {
                val angle = (rotation + i * (360f / 16)) * (PI / 180f)
                val x = cos(angle).toFloat() * radius
                val y = sin(angle).toFloat() * radius * 0.5f
                
                drawCircle(
                    color = color.copy(alpha = 0.6f),
                    radius = 8f,
                    center = Offset(x, y)
                )
                
                drawLine(
                    color = color.copy(alpha = 0.2f),
                    start = Offset.Zero,
                    end = Offset(x, y),
                    strokeWidth = 2f
                )
                
                // Connection between points
                val nextAngle = (rotation + (i+1) * (360f / 16)) * (PI / 180f)
                drawLine(
                    color = color.copy(alpha = 0.4f),
                    start = Offset(x, y),
                    end = Offset(cos(nextAngle).toFloat() * radius, sin(nextAngle).toFloat() * radius * 0.5f),
                    strokeWidth = 1f
                )
            }
        }
    }
}

// "Статичная многомерная структура преобразуется в последовательность событий через 'вейвлет' наблюдателя"
@Composable
private fun TimeViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("time")
    val sweep by infiniteTransition.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val sweepY = size.height * sweep
        
        // Static multidimensional structure
        for (i in 0..20) {
            for (j in 0..10) {
                val x = size.width * (i / 20f)
                val y = size.height * (j / 10f)
                
                val distToSweep = abs(y - sweepY)
                val isObserved = distToSweep < 80f
                
                val pointAlpha = if (isObserved) 1f else 0.15f
                val pointRadius = if (isObserved) 6f else 2f
                val pointColor = if (isObserved) Color.White else color
                
                drawCircle(
                    color = pointColor.copy(alpha = pointAlpha),
                    radius = pointRadius,
                    center = Offset(x, y)
                )
            }
        }
        
        // Observer's wavelet transforming structure into "Now"
        drawRect(
            color = color.copy(alpha = 0.3f),
            topLeft = Offset(0f, sweepY - 40f),
            size = Size(size.width, 80f)
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, sweepY),
            end = Offset(size.width, sweepY),
            strokeWidth = 4f
        )
    }
}

@Composable
private fun SpectralViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("spectral")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val midY = size.height / 2f
        val w = size.width
        
        val freqs = listOf(0.02f to 0.4f, 0.05f to 0.2f, 0.1f to 0.1f) 
        
        // Sum wave
        val sumPath = Path()
        var first = true
        for (x in 0 until w.toInt() step 4) {
            var yOffset = 0f
            for ((f, a) in freqs) {
                yOffset += sin(x * f - phase * (f*10)) * (size.height * a)
            }
            if (first) {
                sumPath.moveTo(x.toFloat(), midY + yOffset)
                first = false
            } else {
                sumPath.lineTo(x.toFloat(), midY + yOffset)
            }
        }
        drawPath(
            path = sumPath,
            color = color,
            style = Stroke(width = 6f)
        )
        
        // Components
        for ((idx, fPair) in freqs.withIndex()) {
            val f = fPair.first
            val a = fPair.second
            val compPath = Path()
            first = true
            val compMidY = size.height * (0.8f + idx * 0.05f) // Draw at bottom
            for (x in 0 until w.toInt() step 4) {
                val y = compMidY + sin(x * f - phase * (f*10)) * (size.height * a * 0.2f)
                if (first) {
                    compPath.moveTo(x.toFloat(), y)
                    first = false
                } else {
                    compPath.lineTo(x.toFloat(), y)
                }
            }
            drawPath(
                path = compPath,
                color = color.copy(alpha = 0.4f),
                style = Stroke(width = 2f)
            )
        }
    }
}

@Composable
private fun ObserverViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("observer")
    val timePos by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "pos"
    )
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing)),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val midY = h / 2f
        val windowCenterX = w * timePos
        
        val path = Path()
        var first = true
        for (x in 0 until w.toInt() step 2) {
            val fx = x.toFloat()
            val distance = fx - windowCenterX
            val envelope = exp(-(distance * distance) / (2f * 4000f)) // Window Memory
            
            val y = midY + sin(distance * 0.04f - phase) * envelope * (h * 0.25f)
            
            if (first) {
                path.moveTo(fx, y)
                first = false
            } else {
                path.lineTo(fx, y)
            }
        }
        
        drawLine(
            color = color.copy(alpha = 0.2f),
            start = Offset(0f, midY),
            end = Offset(w, midY),
            strokeWidth = 2f
        )
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
        
        // The Observer Event
        drawCircle(
            color = Color.White,
            radius = 16f,
            center = Offset(windowCenterX, midY + sin(-phase) * (h * 0.25f))
        )
        drawCircle(
            color = color.copy(alpha = 0.4f),
            radius = 64f, // Integration window
            center = Offset(windowCenterX, midY)
        )
    }
}

@Composable
private fun HierarchyViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("hierarchy")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        
        val maxRadius = size.minDimension * 0.45f
        
        // Superwavelet
        drawCircle(
            color = color.copy(alpha = 0.1f + 0.1f * sin(time * PI.toFloat() * 2f)),
            radius = maxRadius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = color.copy(alpha = 0.5f),
            radius = maxRadius,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
        
        // Inner wavelets
        val nodes = 6
        for (i in 0 until nodes) {
            val angle = time * PI.toFloat() * 2f + i * (2f * PI.toFloat() / nodes)
            val r = maxRadius * 0.5f
            val x = cx + cos(angle) * r
            val y = cy + sin(angle) * r
            
            drawCircle(
                color = color.copy(alpha = 0.3f),
                radius = r,
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = 8f,
                center = Offset(x, y)
            )
            drawLine(
                color = color.copy(alpha = 0.4f),
                start = Offset(cx, cy),
                end = Offset(x, y),
                strokeWidth = 2f
            )
        }
    }
}

@Composable
private fun HolographyViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("holography")
    val zoom by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "zoom"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        
        for (i in 0..4) {
            // Self-similar expansion
            val scale = (zoom + i) / 5f 
            val radius = size.minDimension * 0.45f * scale
            val alpha = (1f - scale).coerceIn(0f, 1f)
            
            drawCircle(
                color = color.copy(alpha = alpha * 0.8f),
                radius = radius,
                center = Offset(cx, cy),
                style = Stroke(width = 4f * (1f - scale))
            )

            // Micro-structures
            val detailNodes = 8
            if (radius > 20f) {
                for (d in 0 until detailNodes) {
                    val angle = d * (2 * PI / detailNodes) + zoom * PI
                    val x = cx + cos(angle).toFloat() * radius
                    val y = cy + sin(angle).toFloat() * radius
                    drawCircle(
                        color = Color.White.copy(alpha = alpha * 0.5f),
                        radius = 4f * scale,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

@Composable
private fun GeometryViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("geometry")
    val expansion by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "expansion"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        
        // Expanding "Ring of States"
        val radius = size.minDimension * 0.4f * expansion
        val alpha = if (expansion > 1f) (1.5f - expansion) / 0.5f else 1f
        
        drawCircle(
            color = color.copy(alpha = alpha * 0.3f),
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = 8f)
        )
        
        // The observer on the rim
        // If ring expands faster than observer goes around, it seems flat
        val observerAngle = (expansion * 2f * PI.toFloat()).coerceAtMost(PI.toFloat() / 4f + PI.toFloat()*2)
        val obsX = cx + cos(observerAngle) * radius
        val obsY = cy + sin(observerAngle) * radius
        
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = 16f,
            center = Offset(obsX, obsY)
        )
        
        // Linear perception tangent
        val tanLength = size.width * 0.6f
        val dx = -sin(observerAngle)
        val dy = cos(observerAngle)
        drawLine(
            color = color.copy(alpha = alpha * 0.8f),
            start = Offset(obsX - dx * tanLength, obsY - dy * tanLength),
            end = Offset(obsX + dx * tanLength, obsY + dy * tanLength),
            strokeWidth = 3f
        )
    }
}

@Composable
fun LagrangianPacketViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("lagrangian")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val midY = h / 2f
        
        // 1. Draw individual underlying continuous Fourier waves (harmonics)
        val harmonicFreqs = listOf(0.015f, 0.03f, 0.045f, 0.06f)
        val harmonicColors = listOf(Color(0xFFF43F5E), Color(0xFFFF9800), Color(0xFF00BCD4), Color(0xFFE040FB))
        
        harmonicFreqs.forEachIndexed { idx, freq ->
            val p = Path()
            var isFirst = true
            for (x in 0 until w.toInt() step 6) {
                // Harmonic waves are global, spanning from -infinity to +infinity
                val y = midY + sin(x * freq - phase * (idx + 1)) * (h * 0.08f)
                if (isFirst) {
                    p.moveTo(x.toFloat(), y)
                    isFirst = false
                } else {
                    p.lineTo(x.toFloat(), y)
                }
            }
            drawPath(
                path = p,
                color = harmonicColors[idx].copy(alpha = 0.25f),
                style = Stroke(width = 2f)
            )
        }
        
        // 2. Draw the synthesized localized wave packet (constructive interference)
        // This packet moves in space-time as phases align
        val packetPath = Path()
        var isFirstPacket = true
        // Animate the center of the constructive wave packet
        val packetCenter = w / 2f + sin(phase) * (w * 0.3f)
        
        for (x in 0 until w.toInt() step 3) {
            val fx = x.toFloat()
            val dx = fx - packetCenter
            // Gaussian envelope limiting the packet
            val envelope = exp(-(dx * dx) / (2f * 2500f))
            
            // Sum of harmonics inside the envelope
            var waveSum = 0f
            harmonicFreqs.forEachIndexed { idx, freq ->
                waveSum += sin(fx * freq - phase * (idx + 1))
            }
            // Normalize sum
            waveSum = (waveSum / harmonicFreqs.size) * (h * 0.35f) * envelope
            
            val y = midY + waveSum
            if (isFirstPacket) {
                packetPath.moveTo(fx, y)
                isFirstPacket = false
            } else {
                packetPath.lineTo(fx, y)
            }
        }
        
        // Draw Gaussian envelope boundaries to illustrate the localized region
        val envelopePathUpper = Path()
        val envelopePathLower = Path()
        var isFirstEnv = true
        for (x in 0 until w.toInt() step 10) {
            val fx = x.toFloat()
            val dx = fx - packetCenter
            val envVal = exp(-(dx * dx) / (2f * 2500f)) * (h * 0.35f)
            if (isFirstEnv) {
                envelopePathUpper.moveTo(fx, midY - envVal)
                envelopePathLower.moveTo(fx, midY + envVal)
                isFirstEnv = false
            } else {
                envelopePathUpper.lineTo(fx, midY - envVal)
                envelopePathLower.lineTo(fx, midY + envVal)
            }
        }
        
        drawPath(
            path = envelopePathUpper,
            color = Color.White.copy(alpha = 0.15f),
            style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
        )
        drawPath(
            path = envelopePathLower,
            color = Color.White.copy(alpha = 0.15f),
            style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
        )
        
        // Draw the main active coherent particle trajectory (the wave packet)
        drawPath(
            path = packetPath,
            color = color,
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )
        
        // Draw localized observer focus point (the physical particle equivalent)
        drawCircle(
            color = Color.White,
            radius = 12f,
            center = Offset(packetCenter, midY + sin(packetCenter * 0.03f - phase) * exp(0f) * (h * 0.15f))
        )
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = 28f,
            center = Offset(packetCenter, midY + sin(packetCenter * 0.03f - phase) * exp(0f) * (h * 0.15f))
        )
    }
}

@Composable
fun GaborUncertaintyViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("gabor")
    
    // Animate the size/width of the present window fluctuating
    val scaleFactor by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing)),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        // Split screen: Column-like or side-by-side
        // Left part: Space-Time Window (t). Right part: Frequency Spectrum (omega).
        val dividerX = w * 0.5f
        
        // Standard divider line
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(dividerX, 0f),
            end = Offset(dividerX, h),
            strokeWidth = 2f
        )
        
        // Draw titles placeholders as colored graphics
        // ---- SPACE-TIME WINDOW (Left side) ----
        val leftMidY = h * 0.5f
        val leftCenterX = dividerX * 0.5f
        // Variance sigma^2 expands and contracts
        val sigma = 500f + scaleFactor * 9000f
        
        // Draw Gabor wavelet window (Envelope)
        val envelopePath = Path()
        val wavePath = Path()
        var isFirst = true
        
        for (x in 10 until (dividerX - 10).toInt() step 3) {
            val fx = x.toFloat()
            val dx = fx - leftCenterX
            val envelope = exp(-(dx * dx) / (2f * sigma))
            val waveVal = sin(dx * 0.13f - phase) * envelope * (h * 0.33f)
            
            if (isFirst) {
                envelopePath.moveTo(fx, leftMidY - envelope * (h * 0.33f))
                wavePath.moveTo(fx, leftMidY + waveVal)
                isFirst = false
            } else {
                envelopePath.lineTo(fx, leftMidY - envelope * (h * 0.33f))
                wavePath.lineTo(fx, leftMidY + waveVal)
            }
        }
        
        // Draw the mirror of envelope
        isFirst = true
        val envelopePathLower = Path()
        for (x in 10 until (dividerX - 10).toInt() step 3) {
            val fx = x.toFloat()
            val dx = fx - leftCenterX
            val envelope = exp(-(dx * dx) / (2f * sigma))
            if (isFirst) {
                envelopePathLower.moveTo(fx, leftMidY + envelope * (h * 0.33f))
                isFirst = false
            } else {
                envelopePathLower.lineTo(fx, leftMidY + envelope * (h * 0.33f))
            }
        }
        
        // Draw the envelope filled with glow
        drawPath(
            path = envelopePath,
            color = Color.White.copy(alpha = 0.08f),
            style = Stroke(width = 2f)
        )
        drawPath(
            path = envelopePathLower,
            color = Color.White.copy(alpha = 0.08f),
            style = Stroke(width = 2f)
        )
        
        // Draw carrier wave
        drawPath(
            path = wavePath,
            color = Color(0xFFFF9800),
            style = Stroke(width = 3.5f, cap = StrokeCap.Round)
        )
        
        // Highlight active window center
        drawCircle(
            color = Color.White,
            radius = 6f,
            center = Offset(leftCenterX, leftMidY)
        )
        
        // ---- FREQUENCY SPECTRUM (Right side) ----
        // According to Fourier theory: narrower space window <=> wider spectral window
        val rightMidY = h * 0.5f
        val rightCenterX = w * 0.75f
        
        // Spectral sigma is inverse to spatial sigma
        // Gabor limit: Sigma_space * Sigma_freq = constant
        val freqSigma = 32000f / sigma
        
        val freqPath = Path()
        isFirst = true
        for (x in (dividerX + 10).toInt() until (w - 10).toInt() step 3) {
            val fx = x.toFloat()
            val dx = fx - rightCenterX
            // Dual peaks at carrier frequency of 0.13
            val carrierOffset = 40f
            
            // Peak 1 and Peak 2 representing positive/negative carrier frequency components
            val specGlow = (exp(-((dx - carrierOffset) * (dx - carrierOffset)) / (2f * freqSigma)) + 
                            exp(-((dx + carrierOffset) * (dx + carrierOffset)) / (2f * freqSigma))) * 0.5f
                            
            // Height scales up when spectrum narrows, preserving integration area (L1 norm conservation)
            val peakHeight = specGlow * (h * 0.33f) * (1.1f - scaleFactor * 0.5f)
            
            val y = rightMidY - peakHeight + sin(fx * 0.2f - phase * 3) * specGlow * 10f
            if (isFirst) {
                freqPath.moveTo(fx, y)
                isFirst = false
            } else {
                freqPath.lineTo(fx, y)
            }
        }
        
        // Draw base spectral axis line
        drawLine(
            color = Color.White.copy(alpha = 0.2f),
            start = Offset(dividerX + 20f, rightMidY),
            end = Offset(w - 20f, rightMidY),
            strokeWidth = 2f
        )
        
        // Draw the spectrum wave
        drawPath(
            path = freqPath,
            color = color,
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
        
        // Draw standard limit boundaries indicating optimal integration (uncertainty boundary)
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = max(10f, freqSigma * 0.3f),
            center = Offset(rightCenterX + 40f, rightMidY)
        )
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = max(10f, freqSigma * 0.3f),
            center = Offset(rightCenterX - 40f, rightMidY)
        )
    }
}

@Composable
fun UnitarityRingViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("unitarity")
    
    // Animate radius inflation representing expansion
    val radiusMult by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "radius"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "rotation"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height * 0.42f
        val baseRadius = size.minDimension * 0.27f
        val currentRadius = baseRadius * radiusMult
        
        // 1. Draw central core
        drawCircle(
            color = Color.White.copy(alpha = 0.15f),
            radius = 16f,
            center = Offset(cx, cy)
        )
        
        // 2. Draw expansion limits (dashed orbits)
        drawCircle(
            color = Color.White.copy(alpha = 0.1f),
            radius = baseRadius * 0.45f,
            style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f,5f), 0f)),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.1f),
            radius = baseRadius * 1.05f,
            style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f,5f), 0f)),
            center = Offset(cx, cy)
        )

        // 3. Draw the active elastic physical ring
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = currentRadius,
            style = Stroke(width = 6f),
            center = Offset(cx, cy)
        )
        
        // 4. Draw quantum probability amplitudes (state nodules) on the ring
        val count = 10
        val sumSqAmplitudes = 1f // Conserved total probability
        
        for (i in 0 until count) {
            val angleDeg = (rotation + (i * (360f / count)))
            val angleRad = angleDeg * (PI / 180f)
            
            // Individual state coordinate
            val sx = cx + cos(angleRad).toFloat() * currentRadius
            val sy = cy + sin(angleRad).toFloat() * currentRadius
            
            // Fluctuating amplitude
            val ampScale = 0.5f + 0.3f * sin(angleDeg * 0.1f + rotation * 0.05f).toFloat()
            val dotRadius = 10f * ampScale
            
            // Connect to core
            drawLine(
                color = color.copy(alpha = 0.15f),
                start = Offset(cx, cy),
                end = Offset(sx, sy),
                strokeWidth = 2f
            )
            
            // Draw state node
            drawCircle(
                color = color.copy(alpha = 0.4f),
                radius = dotRadius + 6f,
                center = Offset(sx, sy)
            )
            drawCircle(
                color = Color.White,
                radius = dotRadius,
                center = Offset(sx, sy)
            )
        }
        
        // 5. Draw live unitarity probability bar at the bottom
        val barY = size.height * 0.88f
        val barW = size.width * 0.72f
        val barStartX = (size.width - barW) * 0.5f
        
        // Draw background container
        drawRect(
            color = Color.White.copy(alpha = 0.1f),
            topLeft = Offset(barStartX, barY - 12f),
            size = Size(barW, 24f)
        )
        
        // Draw the active green probability bar (filled exactly 100% to represent conservation)
        drawRect(
            color = Color(0xFF10B981),
            topLeft = Offset(barStartX + 2f, barY - 10f),
            size = Size(barW - 4f, 20f)
        )
        
        // Overlay a small graphical text indicator or tick mark at center indicating total probability
        drawLine(
            color = Color.White,
            start = Offset(size.width / 2f, barY - 16f),
            end = Offset(size.width / 2f, barY + 16f),
            strokeWidth = 4f
        )
    }
}

@Composable
fun HolographicBekensteinViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("bekenstein")
    
    // Zoom scale to show recursiveness
    val zoom by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4500, easing = LinearEasing)),
        label = "zoom"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(25000, easing = LinearEasing)),
        label = "rotation"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val boundaryRadius = size.minDimension * 0.42f
        
        // 1. Draw outer horizon boundary (Bekenstein Limit Boundary)
        drawCircle(
            color = color,
            radius = boundaryRadius,
            style = Stroke(width = 4f),
            center = Offset(cx, cy)
        )
        
        // Concentric outer halo representation of entropy
        drawCircle(
            color = color.copy(alpha = 0.15f),
            radius = boundaryRadius + 12f,
            style = Stroke(width = 2f),
            center = Offset(cx, cy)
        )
        
        // 2. Draw nested self-similar scale pixels (recursively shrinking cubes or meshes)
        val layersCount = 4
        for (layer in 0 until layersCount) {
            val s = (zoom + layer) / layersCount.toFloat() // Scale of current nested layer
            val scaleRadius = boundaryRadius * s
            val alpha = (1f - s).coerceIn(0f, 1f)
            
            if (scaleRadius > 5f) {
                // Draw nested self-similar circles
                drawCircle(
                    color = color.copy(alpha = alpha * 0.25f),
                    radius = scaleRadius,
                    style = Stroke(width = 2f),
                    center = Offset(cx, cy)
                )
                
                // Draw fractional grid crossings (pixels representing holographic points)
                val segmentCount = 6
                val angleOffset = rotation * (if (layer % 2 == 0) 1f else -1f)
                for (i in 0 until segmentCount) {
                    val angle = (angleOffset + i * (360f / segmentCount)) * (PI / 180f)
                    val px = cx + cos(angle).toFloat() * scaleRadius
                    val py = cy + sin(angle).toFloat() * scaleRadius
                    
                    drawLine(
                        color = Color.White.copy(alpha = alpha * 0.3f),
                        start = Offset(cx, cy),
                        end = Offset(px, py),
                        strokeWidth = 1.5f
                    )
                    
                    drawCircle(
                        color = color.copy(alpha = alpha * 0.6f),
                        radius = 5f * s,
                        center = Offset(px, py)
                    )
                }
            }
        }
        
        // 3. Draw high-frequency "Low-Pass Spectral Filter / Boundary Censorship"
        // Sweeping radial scan showing limits of entropy
        val scanAngle = (rotation * 2.2f) * (PI / 180f)
        val sx = cx + cos(scanAngle).toFloat() * boundaryRadius
        val sy = cy + sin(scanAngle).toFloat() * boundaryRadius
        
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(cx, cy),
            end = Offset(sx, sy),
            strokeWidth = 3f
        )
        
        // Glow points along scanning line
        for (k in 1..4) {
            val f = k / 4f
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(cx + cos(scanAngle).toFloat() * boundaryRadius * f, cy + sin(scanAngle).toFloat() * boundaryRadius * f)
            )
        }
    }
}

@Composable
fun ZeroSymmetryViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("zeroSymmetry")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing)),
        label = "phase"
    )
    val symmetryFactor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "symmetryFactor"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width
        val h = size.height
        
        // Background grid of pure possibilities
        for (i in 0..10) {
            val gx = (w / 10f) * i
            drawLine(
                color = color.copy(alpha = 0.05f),
                start = Offset(gx, 0f),
                end = Offset(gx, h),
                strokeWidth = 1f
            )
        }

        // Draw the main positive wave (representing f(x))
        val pathF = Path()
        // Draw the negative wave (representing -f(x))
        val pathMinusF = Path()

        val steps = 120
        for (i in 0..steps) {
            val x = (w / steps) * i
            val relX = (i / steps.toFloat()) * 4f * PI.toFloat()
            val amp = h * 0.16f
            val yOffsetF = sin(relX - phase) * amp * (1f - 0.9f * symmetryFactor)
            val yOffsetMinusF = -sin(relX - phase) * amp * (1f - 0.1f * symmetryFactor)

            if (i == 0) {
                pathF.moveTo(x, cy + yOffsetF)
                pathMinusF.moveTo(x, cy + yOffsetMinusF)
            } else {
                pathF.lineTo(x, cy + yOffsetF)
                pathMinusF.lineTo(x, cy + yOffsetMinusF)
            }
        }

        // Draw f(x) (Cyan representation of positive modes)
        drawPath(
            path = pathF,
            color = Color(0xFF00BCD4).copy(alpha = 0.7f),
            style = Stroke(width = 3f)
        )

        // Draw -f(x) (Orange representation of negative waves)
        drawPath(
            path = pathMinusF,
            color = Color(0xFFFF9800).copy(alpha = 0.7f),
            style = Stroke(width = 3f)
        )

        // Draw the composite SUM wave S(x) = f(x) + (-f(x))
        val pathSum = Path()
        for (i in 0..steps) {
            val x = (w / steps) * i
            val relX = (i / steps.toFloat()) * 4f * PI.toFloat()
            val amp = h * 0.16f
            val yOffsetF = sin(relX - phase) * amp * (1f - 0.9f * symmetryFactor)
            val yOffsetMinusF = -sin(relX - phase) * amp * (1f - 0.1f * symmetryFactor)
            val sumY = yOffsetF + yOffsetMinusF

            if (i == 0) {
                pathSum.moveTo(x, cy + sumY)
            } else {
                pathSum.lineTo(x, cy + sumY)
            }
        }

        // Draw Sum S(x) in bright Magenta color
        drawPath(
            path = pathSum,
            color = Color(0xFFE040FB),
            style = Stroke(width = 4.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
        )

        // Draw discrete wavelet template as points "1 0 0 1 0 1..." at the bottom
        val binaryArray = intArrayOf(1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1)
        val binY = h * 0.85f
        val binW = w / (binaryArray.size + 1)
        for (i in binaryArray.indices) {
            val bx = binW * (i + 1)
            val bit = binaryArray[i]
            val bitColor = if (bit == 1) Color(0xFF10B981) else Color.White.copy(alpha = 0.2f)
            val bitRadius = if (bit == 1) 8f else 4f
            
            drawCircle(
                color = bitColor,
                radius = bitRadius,
                center = Offset(bx, binY)
            )
            
            if (bit == 1) {
                drawCircle(
                    color = bitColor.copy(alpha = 0.3f),
                    radius = bitRadius + 6f * (1f - symmetryFactor),
                    center = Offset(bx, binY),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

@Composable
fun ScaleSpaceViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("scaleSpace")
    
    // Pulse animation indicating the observer's attention coordinate or scan of scales
    val attentionYFactor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "attentionYFactor"
    )
    
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "rotationAngle"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        
        // Background grid of scales
        for (i in 1..4) {
            val scaleRadius = (w * 0.12f) * i
            drawCircle(
                color = color.copy(alpha = 0.03f),
                radius = scaleRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 1f)
            )
        }

        // Draw scale axis (glowing coordinate line representing Radius coordinate 'r')
        drawLine(
            color = Color(0xFF00BCD4).copy(alpha = 0.25f),
            start = Offset(cx, h * 0.12f),
            end = Offset(cx, h * 0.88f),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        
        // Let's draw 4 concentric rings stacked/projected with simulated 3D elliptic perspective
        // Ring levels: 1 = small, 2 = medium, 3 = large, 4 = super large
        val levels = 4
        val levelYPositions = floatArrayOf(
            h * 0.22f, // Coarse Scale
            h * 0.42f, // Intermediate Scale
            h * 0.62f, // Finer Scale
            h * 0.82f  // Ultimate detailed scale
        )
        
        val levelRadiiX = floatArrayOf(
            w * 0.14f,
            w * 0.24f,
            w * 0.35f,
            w * 0.46f
        )
        
        val levelRadiiY = floatArrayOf(
            h * 0.04f,
            h * 0.06f,
            h * 0.08f,
            h * 0.10f
        )
        
        val ringColors = arrayOf(
            Color(0xFFE040FB), // Magenta (Coarsest scale)
            Color(0xFF818CF8), // Indigo
            Color(0xFF22D3EE), // Cyan
            Color(0xFF10B981)  // Green (Fine scale)
        )

        // Draw connections (projection rays pointing from outer ring nodes to inner ring nodes)
        val nodeCount = 8
        for (n in 0 until nodeCount) {
            val angle = (2 * PI / nodeCount) * n + rotationAngle
            val cosA = cos(angle).toFloat()
            val sinA = sin(angle).toFloat()
            
            // Reconstruct path from top ring to bottom ring for this node
            val projectionPath = Path()
            for (lvl in 0 until levels) {
                val rx = levelRadiiX[lvl]
                val ry = levelRadiiY[lvl]
                val ly = levelYPositions[lvl]
                val px = cx + cosA * rx
                val py = ly + sinA * ry
                
                if (lvl == 0) {
                    projectionPath.moveTo(px, py)
                } else {
                    projectionPath.lineTo(px, py)
                }
            }
            
            // Draw projection guidelines
            drawPath(
                path = projectionPath,
                color = Color.White.copy(alpha = 0.08f),
                style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
            )
        }

        // Draw the rings
        for (lvl in 0 until levels) {
            val rx = levelRadiiX[lvl]
            val ry = levelRadiiY[lvl]
            val ly = levelYPositions[lvl]
            val rColor = ringColors[lvl]
            
            // Determine active highlight based on observer attention pulse
            val relativeDistance = abs(attentionYFactor - (lvl / (levels - 1).toFloat()))
            val highlightIntensity = (1f - relativeDistance * 1.8f).coerceIn(0f, 1f)
            
            val alpha = 0.15f + highlightIntensity * 0.70f
            val strokeW = 1.5f + highlightIntensity * 3.5f
            
            // Draw ellipse
            val ellipsePath = Path()
            val steps = 80
            for (i in 0..steps) {
                val theta = (2 * PI / steps) * i
                val ex = cx + cos(theta).toFloat() * rx
                val ey = ly + sin(theta).toFloat() * ry
                if (i == 0) {
                    ellipsePath.moveTo(ex, ey)
                } else {
                    ellipsePath.lineTo(ex, ey)
                }
            }
            
            // Draw the ring border
            drawPath(
                path = ellipsePath,
                color = rColor.copy(alpha = alpha),
                style = Stroke(width = strokeW)
            )

            // Draw a subtle fill inside highlighted ring
            if (highlightIntensity > 0.05f) {
                drawPath(
                    path = ellipsePath,
                    color = rColor.copy(alpha = 0.03f * highlightIntensity)
                )
            }

            // Draw quantized nodes along this ring
            for (n in 0 until nodeCount) {
                val angle = (2 * PI / nodeCount) * n + rotationAngle
                val cosA = cos(angle).toFloat()
                val sinA = sin(angle).toFloat()
                val nx = cx + cosA * rx
                val ny = ly + sinA * ry
                
                // Active node pulses
                val isNodeActive = (n % 3 == lvl % 3)
                val nodeColor = if (isNodeActive) rColor else Color.White.copy(alpha = 0.35f)
                val nodeRad = if (isNodeActive) (5f + highlightIntensity * 4f) else 3f
                
                drawCircle(
                    color = nodeColor,
                    radius = nodeRad,
                    center = Offset(nx, ny)
                )
                
                if (isNodeActive && highlightIntensity > 0.1f) {
                    drawCircle(
                        color = rColor.copy(alpha = 0.4f * highlightIntensity),
                        radius = nodeRad + 8f * highlightIntensity,
                        center = Offset(nx, ny),
                        style = Stroke(width = 1.5f)
                    )
                }
            }
        }

        // Draw Observer Focus Pointer sliding along the Scale axis (Y axis)
        val activeY = h * 0.22f + attentionYFactor * (h * 0.60f)
        
        // Draw horizontal scanning plane representing "The Slice of Observer present state read"
        drawLine(
            color = Color(0xFFFF9800).copy(alpha = 0.45f),
            start = Offset(cx - w * 0.38f, activeY),
            end = Offset(cx + w * 0.38f, activeY),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        )
        
        // Glowing marker on the axis
        drawCircle(
            color = Color(0xFFFF9800),
            radius = 10f,
            center = Offset(cx, activeY)
        )
        drawCircle(
            color = Color(0xFFFF9800).copy(alpha = 0.35f),
            radius = 22f,
            center = Offset(cx, activeY),
            style = Stroke(width = 3f)
        )
    }
}

@Composable
fun CosmicVacuumIntroViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("cosmic_vacuum")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "phase"
    )
    val noiseY by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Reverse),
        label = "noise"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cy = h / 2f

        // Draw multiple faint background sine lines representing latent cosmic frequencies
        for (i in 0 until 5) {
            val path = Path()
            val amp = (h * 0.18f) / (i + 1)
            val freq = 0.015f * (i + 1)
            val phaseOffset = i * (PI.toFloat() / 3f)

            var first = true
            for (x in 0 until w.toInt() step 4) {
                val waveY = cy + sin(x * freq + phase + phaseOffset) * amp * noiseY
                if (first) {
                    path.moveTo(x.toFloat(), waveY)
                    first = false
                } else {
                    path.lineTo(x.toFloat(), waveY)
                }
            }
            drawPath(
                path = path,
                color = color.copy(alpha = 0.1f + 0.05f * i),
                style = Stroke(width = 2f - 0.2f * i)
            )
        }

        // Draw quantum vacuum virtual packets that rise and disappear
        val count = 12
        for (i in 0 until count) {
            val t = (phase / (2f * PI.toFloat()) + i / count.toFloat()) % 1.0f
            // x coordinate is fixed per index
            val x = w * (0.08f + 0.84f * (i / count.toFloat()))
            // y coordinate goes from bottom to top or oscillates
            val alpha = sin(t * PI.toFloat())
            val sizeRadius = 5f + 10f * t
            val yOffset = cos(t * PI.toFloat() * 2f + i) * (h * 0.25f)
            
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.35f),
                radius = sizeRadius,
                center = Offset(x, cy + yOffset)
            )

            drawLine(
                color = color.copy(alpha = alpha * 0.2f),
                start = Offset(x, cy),
                end = Offset(x, cy + yOffset),
                strokeWidth = 1.5f
            )
        }

        // Center horizon line
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(0f, cy),
            end = Offset(w, cy),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
    }
}

@Composable
fun HolographicHorizonViz(color: Color) {
    val infiniteTransition = rememberInfiniteTransition("holographic_horizon")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(25000, easing = LinearEasing)),
        label = "rotation"
    )
    val breathe by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "breathe"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxDim = size.minDimension * 0.45f

        // Draw concentric 3D perspective grid lines
        for (i in 1..6) {
            val r = maxDim * (i / 6f) * breathe
            // Draw circle with perspective transformation
            val path = Path()
            val steps = 60
            for (s in 0..steps) {
                val angle = (s * 360f / steps) * (PI / 180f)
                val rx = r
                val ry = r * 0.45f // isometric perspective flattening
                val px = cx + cos(angle).toFloat() * rx
                val py = cy + sin(angle).toFloat() * ry
                if (s == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            drawPath(
                path = path,
                color = color.copy(alpha = 0.1f * i),
                style = Stroke(width = 1.5f)
            )
        }

        // Draw radial projection coordinates with rotating angle
        for (i in 0 until 12) {
            val angle = (rotation + i * (360f / 12)) * (PI / 180f)
            val rx = maxDim * breathe
            val ry = maxDim * 0.45f * breathe
            val px = cx + cos(angle).toFloat() * rx
            val py = cy + sin(angle).toFloat() * ry

            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(cx, cy),
                end = Offset(px, py),
                strokeWidth = 1f
            )

            // Draw information boundary nodes (quanta) at the horizon
            drawCircle(
                color = Color.White.copy(alpha = 0.65f),
                radius = 5f,
                center = Offset(px, py)
            )
            drawCircle(
                color = color.copy(alpha = 0.3f),
                radius = 12f,
                center = Offset(px, py)
            )
        }

        // Central holographic source glowing sphere
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = 32f * breathe,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color.White,
            radius = 10f,
            center = Offset(cx, cy)
        )
    }
}

@Composable
fun PixelFractalViz(color: Color, zoomFactor: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val baseSize = kotlin.math.min(w, h) * 0.8f

        fun drawFractal(x: Float, y: Float, sz: Float, depth: Int, maxDepth: Int) {
            if (depth > maxDepth || sz < 1f) return
            
            drawRoundRect(
                color = color.copy(alpha = 0.3f / (depth + 1)),
                topLeft = Offset(x - sz / 2, y - sz / 2),
                size = Size(sz, sz),
                cornerRadius = CornerRadius(sz * 0.1f, sz * 0.1f),
                style = Stroke(width = kotlin.math.max(1f, sz * 0.02f))
            )
            
            val subSz = sz / 3f
            for (i in -1..1) {
                for (j in -1..1) {
                    val px = x + i * subSz
                    val py = y + j * subSz
                    if (i == 0 && j == 0) {
                        drawFractal(px, py, subSz, depth + 1, maxDepth)
                    } else {
                        val pulse = (kotlin.math.sin(time * 2 * Math.PI + depth + i + j).toFloat() + 1f) / 2f
                        drawCircle(
                            color = color.copy(alpha = 0.5f * pulse),
                            center = Offset(px, py),
                            radius = subSz * 0.3f
                        )
                    }
                }
            }
        }

        withTransform({
            translate(left = cx, top = cy)
            scale(scaleX = zoomFactor, scaleY = zoomFactor)
            translate(left = -cx, top = -cy)
        }) {
            val maxDepth = (kotlin.math.log(zoomFactor.toDouble() + 1.0, 3.0) * 2 + 2).toInt()
            drawFractal(cx, cy, baseSize, 0, maxDepth)
        }
    }
}



