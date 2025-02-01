package com.example.fakestoreapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fakestoreapp.storage.entities.CategoryEntity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "product",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["category"],
        childColumns = ["category"],
        onDelete = ForeignKey.Companion.CASCADE
    )],
)
data class Product(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("price")
    val price: String? = null,
    @SerializedName("category")
    @ColumnInfo(index = true)
    val category: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String? = null,
)

fun Product.toJson() = Gson().toJson(this)
fun String.toProduct() = Gson().fromJson(this, Product::class.java)
