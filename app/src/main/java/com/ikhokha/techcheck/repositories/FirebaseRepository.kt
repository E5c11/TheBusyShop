package com.ikhokha.techcheck.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.ikhokha.techcheck.data.entities.Product
import com.ikhokha.techcheck.utils.observeValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

const val STORAGE_BASE_URL = "gs://the-busy-shop.appspot.com/"

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    fbDatabase: FirebaseDatabase,
    private val fbStorage: FirebaseStorage
) {

    private val database = fbDatabase.reference
    private val storage = fbStorage.reference

    suspend fun signInUser(email: String, password: String): FirebaseUser =
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user ?:
        throw FirebaseAuthException("", "")

    fun getProducts(): Flow<List<Product>> = database.observeValue()
            .map { data ->
                val list = mutableListOf<Product>()
                Log.d("myT", "getProducts: ${data}")
                for (snap in data!!.children) {
                    val result = snap.getValue<Product>()
                    result?.apply {
                        imageUrl = getProductImageUrl(image)
                        Log.d("myT", "getProducts: ${imageUrl}")
                        id = snap.key
                    }?.let { list.add(it) }
                }
                list
            }.catch { it.stackTrace }

    private suspend fun getProductImageUrl(imageName: String?) =
        fbStorage.getReferenceFromUrl("${STORAGE_BASE_URL}$imageName")

}