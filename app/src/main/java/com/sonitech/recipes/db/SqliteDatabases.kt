package com.sonitech.recipes.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SqliteDatabases(context: Context) : SQLiteOpenHelper(context, "Shopping_Cart_DB", null, 1) {

    // Create cart table
    private val SQL_CREATE_CART_INGREDIENTS = "CREATE TABLE ${ShoppingCart.TABLE_NAME} (" +
            "${ShoppingCart.COLUMN_CART_INGREDIENT_ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "${ShoppingCart.COLUMN_RECIPE_NAME} TEXT," +
            "${ShoppingCart.COLUMN_CART_INGREDIENT} TEXT," +
            "${ShoppingCart.COLUMN_CART_INGREDIENT_QTY} TEXT," +
            "${ShoppingCart.COLUMN_CART_INGREDIENT_IMAGE} TEXT," +
            "${ShoppingCart.COLUMN_CART_INGREDIENT_PRICE} TEXT)"

    private val SQL_CREATE_MEAL_PLAIN = "CREATE TABLE ${MealPlan.TABLE_NAME} (" +
            "${MealPlan.COLUMN_MEAL_PLAN_ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "${MealPlan.COLUMN_DATE_TIME} TEXT," +
            "${MealPlan.COLUMN_BREAKFAST} TEXT," +
            "${MealPlan.COLUMN_LUNCH} TEXT," +
            "${MealPlan.COLUMN_DINNER} TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_CART_INGREDIENTS)
        db?.execSQL(SQL_CREATE_MEAL_PLAIN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addShoppingCartIngredient(
        recipeName: String,
        name: String,
        qty: String,
        image: String,
        price: String
    ): Boolean {
        val sqliteDatabases = writableDatabase

        // Create map where set values in db
        val values = ContentValues().apply {
            put(ShoppingCart.COLUMN_RECIPE_NAME, recipeName)
            put(ShoppingCart.COLUMN_CART_INGREDIENT, name)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_IMAGE, image)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_PRICE, price)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_QTY, qty)
        }

        // Insert new row in table
        return sqliteDatabases!!.insert(ShoppingCart.TABLE_NAME, null, values).toInt() != -1
    }

    fun addMealPlanning(
        dateTime: String,
        breakfast: String,
        lunch: String,
        dinner: String
    ): Boolean {
        val sqliteDatabases = writableDatabase

        // Create map where set values in db
        val values = ContentValues().apply {
            put(MealPlan.COLUMN_DATE_TIME, dateTime)
            put(MealPlan.COLUMN_BREAKFAST, breakfast)
            put(MealPlan.COLUMN_LUNCH, lunch)
            put(MealPlan.COLUMN_DINNER, dinner)
        }

        // Insert new row in table
        return sqliteDatabases!!.insert(MealPlan.TABLE_NAME, null, values).toInt() != -1
    }

    fun getShoppingCartIngredients(): Cursor {
        val sqliteDatabases = readableDatabase
        return sqliteDatabases!!.rawQuery("SELECT * FROM " + ShoppingCart.TABLE_NAME, null)
    }

    fun getMealPlan(): Cursor {
        val sqliteDatabases = readableDatabase
        return sqliteDatabases!!.rawQuery("SELECT * FROM " + MealPlan.TABLE_NAME, null)
    }

    fun updateShoppingCartIngredient(
        id: String,
        name: String,
        qty: String,
        image: String,
        price: String
    ): Boolean {
        val sqliteDatabases = writableDatabase

        // Create map where set values in db
        val values = ContentValues().apply {
            put(ShoppingCart.COLUMN_CART_INGREDIENT, name)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_IMAGE, image)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_PRICE, price)
            put(ShoppingCart.COLUMN_CART_INGREDIENT_QTY, qty)
        }

        return sqliteDatabases!!.update(
            ShoppingCart.TABLE_NAME,
            values,
            ShoppingCart.COLUMN_CART_INGREDIENT + "=?",
            arrayOf(name)
        ) > 0
    }

    fun updateMealPan(
        dateTime: String,
        breakfast: String,
        lunch: String,
        dinner: String
    ): Boolean {
        val sqliteDatabases = writableDatabase

        // Create map where set values in db
        val values = ContentValues().apply {
            put(MealPlan.COLUMN_DATE_TIME, dateTime)
            put(MealPlan.COLUMN_BREAKFAST, breakfast)
            put(MealPlan.COLUMN_LUNCH, lunch)
            put(MealPlan.COLUMN_DINNER, dinner)
        }

        return sqliteDatabases!!.update(
            MealPlan.TABLE_NAME,
            values,
            MealPlan.COLUMN_DATE_TIME + "=?",
            arrayOf(dateTime)
        ) > 0
    }

    fun deleteShoppingCartIngredient(name: String): Boolean {
        val sqliteDatabases = writableDatabase
        return sqliteDatabases!!.delete(
            ShoppingCart.TABLE_NAME,
            ShoppingCart.COLUMN_CART_INGREDIENT + "=?",
            arrayOf(name)
        ) > 0
    }

    fun deleteMealPlan(dateTime: String): Boolean {
        val sqliteDatabases = writableDatabase
        return sqliteDatabases!!.delete(
            MealPlan.TABLE_NAME,
            MealPlan.COLUMN_DATE_TIME + "=?",
            arrayOf(dateTime)
        ) > 0
    }
}