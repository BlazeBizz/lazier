package com.velox.lazier.comopse_ulits

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput


enum class ButtonStateChange { Pressed, Idle }

fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    var buttonStateChange by remember { mutableStateOf(ButtonStateChange.Idle) }
    val scale by animateFloatAsState(if (buttonStateChange == ButtonStateChange.Pressed) 0.85f else 1f)
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                onClick.invoke()
            })
        .pointerInput(buttonStateChange) {
            awaitPointerEventScope {
                buttonStateChange = if (buttonStateChange == ButtonStateChange.Pressed) {
                    waitForUpOrCancellation()
                    ButtonStateChange.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonStateChange.Pressed
                }
            }
        }
}

fun Modifier.pressClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonStateChange.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonStateChange.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {  }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonStateChange.Pressed) {
                    waitForUpOrCancellation()
                    ButtonStateChange.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonStateChange.Pressed
                }
            }
        }
}

fun Modifier.shakeClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonStateChange.Idle) }

    val tx by animateFloatAsState(
        targetValue = if (buttonState == ButtonStateChange.Pressed) 0f else -50f,
        animationSpec = repeatable(
            iterations = 2,
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    this
        .graphicsLayer {
            translationX = tx
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonStateChange.Pressed) {
                    waitForUpOrCancellation()
                    ButtonStateChange.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonStateChange.Pressed
                }
            }
        }
}