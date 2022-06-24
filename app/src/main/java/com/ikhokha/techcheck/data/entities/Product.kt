package com.ikhokha.techcheck.data.entities

import android.os.Parcelable
import androidx.room.Entity
import com.google.firebase.storage.StorageReference
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "product_table")
data class Product (
    var id          : String? = null,
    val description : String? = null,
    val image       : String? = null,
    val price       : Double?    = null,
    var imageUrl    : @RawValue StorageReference? = null

): Parcelable
