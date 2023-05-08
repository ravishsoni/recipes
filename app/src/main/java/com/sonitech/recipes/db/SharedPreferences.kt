package com.sonitech.recipes.db

import android.content.Context

object SharedPreferences {
    fun setValues(context: Context, key: String, value: String) {
        context.getSharedPreferences("Recipe_Shared", Context.MODE_PRIVATE).edit()
            .putString(key, value).apply()
    }

    fun getValues(context: Context, key: String): String {
        return context.getSharedPreferences("Recipe_Shared", Context.MODE_PRIVATE)?.getString(key, "")!!
    }

    fun clearValues(context: Context){
        context.getSharedPreferences("Recipe_Shared",Context.MODE_PRIVATE)!!.edit().clear().apply()
    }
}