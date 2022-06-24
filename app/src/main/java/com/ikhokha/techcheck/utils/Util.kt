package com.ikhokha.techcheck.utils

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

fun DatabaseReference.observeValue(): Flow<DataSnapshot?> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                offer(snapshot)
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }