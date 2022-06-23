package com.ikhokha.techcheck.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginFirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth) {

    suspend fun signInUser(email: String, password: String): FirebaseUser =
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user ?:
        throw FirebaseAuthException("", "")

}