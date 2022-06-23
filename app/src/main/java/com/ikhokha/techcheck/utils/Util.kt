package com.ikhokha.techcheck.utils

import android.view.animation.Animation
import android.view.animation.RotateAnimation

fun String.onlyLetters() = all { it.isLetter() }

infix fun <T> Boolean.then(param: T): T? = if (this) param else null

val <T> T.exhaustive: T
    get() = this

fun getRotateAnimation() =
    RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
        duration = 1000
        startOffset = 500
        repeatMode = Animation.RESTART
        repeatCount = Animation.INFINITE
    }