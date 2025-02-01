package com.example.fakestoreapp.storage

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class FSCache @Inject constructor(context: Context) {
    val name = "fscache"
    val sharedPreferences : SharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE)
}