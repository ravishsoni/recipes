package com.sonitech.recipes.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class RecipesModel(
    @Exclude
    var id: String,
    val Description: String,
    val Image: String,
    val Name: String,
    val Ingredients: List<IngredientModel>,
    val CuisineType: String,
    var Fav: Int
) : Serializable {
    constructor(): this("", "", "", "", ArrayList(), "",0)
}

