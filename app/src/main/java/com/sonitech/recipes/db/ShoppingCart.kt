package com.sonitech.recipes.db

import android.provider.BaseColumns
import com.google.firebase.firestore.Exclude

object ShoppingCart : BaseColumns {
    val TABLE_NAME = "tb_shopping_cart"
    val COLUMN_CART_INGREDIENT_ID = "cart_ingredient_id"
    val COLUMN_RECIPE_NAME = "recipe_name"
    val COLUMN_CART_INGREDIENT = "cart_ingredient"
    val COLUMN_CART_INGREDIENT_QTY = "cart_ingredient_qty"
    val COLUMN_CART_INGREDIENT_IMAGE = "cart_ingredient_image"
    val COLUMN_CART_INGREDIENT_PRICE = "cart_ingredient_price"
}