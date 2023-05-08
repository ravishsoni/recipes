package com.sonitech.recipes.model

import com.google.firebase.firestore.Exclude

data class IngredientModel(
    val Image: String,
    val Name: String,
    var isAdded: Int,
    var Price: String
): java.io.Serializable {
    constructor(): this("", "",0, "")
}