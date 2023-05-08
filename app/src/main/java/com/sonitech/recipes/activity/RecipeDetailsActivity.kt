package com.sonitech.recipes.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.sonitech.recipes.R
import com.sonitech.recipes.adapter.IngredientsAdapter
import com.sonitech.recipes.databinding.ActivityRecipeDetailsBinding
import com.sonitech.recipes.db.ShoppingCart
import com.sonitech.recipes.db.SqliteDatabases
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.IngredientModel
import com.sonitech.recipes.model.RecipesModel

class RecipeDetailsActivity : AppCompatActivity(), OnClickListeners {
    private var binding: ActivityRecipeDetailsBinding? = null
    private var fireCloudStore: FirebaseFirestore? = null
    private var recipesModel: RecipesModel? = null
    private var ingredientsAdapter: IngredientsAdapter? = null
    private var ingredientList: ArrayList<IngredientModel> = ArrayList<IngredientModel>()
    private var sqliteHelper: SqliteDatabases? = null
    private var shoppingCartList: ArrayList<IngredientModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_recipe_details)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details)

        // Set status color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        // Init database
        sqliteHelper = SqliteDatabases(this)

        // Init firebase cloud store
        fireCloudStore = FirebaseFirestore.getInstance()

        // Get recipe details
        if (intent.hasExtra("BUNDLE_RECIPE"))
            recipesModel = intent.extras!!.getSerializable("BUNDLE_RECIPE") as RecipesModel

        // Set recipe details
        setRecipeDetails()
    }

    private fun setRecipeDetails() {
        val recipeName = recipesModel!!.Name
        val recipeImage = recipesModel!!.Image
        val recipeType = recipesModel!!.CuisineType
        val recipeDescription = recipesModel!!.Description
        val recipeIngredient = recipesModel!!.Ingredients

        binding!!.tvRecipeName.text = recipeName
        binding!!.tvRecipeType.text = recipeType
        binding!!.tvRecipeDescription.text = recipeDescription
        Glide.with(this).load(recipeImage).placeholder(R.drawable.ic_menu).error(R.drawable.ic_menu)
            .into(binding!!.ivRecipe)

        // Get ingredient data from local db
        sqliteHelper!!.readableDatabase
        val cursor = sqliteHelper!!.getShoppingCartIngredients()

        // Clear data
        ingredientList.clear()

        // Init ingredient recyclerview
        binding!!.rvIngredients.layoutManager = GridLayoutManager(this, 2)
        binding!!.rvIngredients.hasFixedSize()

        if (recipeIngredient.isNotEmpty()) {
            binding!!.tvRecipeIngredientsLabels.visibility = View.VISIBLE
            ingredientsAdapter = IngredientsAdapter(this, recipeIngredient, this, checkShoppingCart(), "Details")
            binding!!.rvIngredients.adapter = ingredientsAdapter
            ingredientsAdapter!!.notifyDataSetChanged()
        } else {
            binding!!.tvRecipeIngredientsLabels.visibility = View.GONE
        }
    }

    override fun <T> onClick(item: T, holder: RecyclerView.ViewHolder, type: String) {

        val ingredient = item as IngredientModel
        val ingredientsViewHolder = holder as IngredientsAdapter.IngredientsViewHolder

        if (type == getString(R.string.add_to_cart)) {
            updateShoppingCart(ingredient, ingredientsViewHolder, type)
        } else if (type == getString(R.string.added_in_cart)) {
            deleteShoppingCart(ingredient, ingredientsViewHolder, type)
        }
    }

    private fun updateShoppingCart(
        ingredient: IngredientModel,
        holder: IngredientsAdapter.IngredientsViewHolder,
        type: String
    ) {

        Log.d("Type", ingredient.isAdded.toString())
        if (ingredient.isAdded == 0) {
            ingredient.isAdded = 1

            holder.tvAddToCart.text = getString(R.string.added_in_cart)
        } else {
            ingredient.isAdded = 0

            holder.tvAddToCart.text = getString(R.string.add_to_cart)
        }

        // Save ingredients list in local db
        sqliteHelper!!.writableDatabase
        sqliteHelper!!.addShoppingCartIngredient(
            recipesModel!!.Name,
            ingredient.Name,
            ingredient.isAdded.toString(),
            ingredient.Image,
            ingredient.Price
        )
    }

    private fun deleteShoppingCart(
        ingredient: IngredientModel,
        holder: IngredientsAdapter.IngredientsViewHolder,
        type: String
    ) {
        sqliteHelper!!.deleteShoppingCartIngredient(ingredient.Name)
        ingredient.isAdded = 0
        holder.tvAddToCart.text = getString(R.string.add_to_cart)
    }

    private fun checkShoppingCart(): ArrayList<IngredientModel> {
        val db = sqliteHelper!!.readableDatabase

        val projection =
            arrayOf(
                ShoppingCart.COLUMN_CART_INGREDIENT_ID,
                ShoppingCart.COLUMN_RECIPE_NAME,
                ShoppingCart.COLUMN_CART_INGREDIENT,
                ShoppingCart.COLUMN_CART_INGREDIENT_QTY,
                ShoppingCart.COLUMN_CART_INGREDIENT_IMAGE,
                ShoppingCart.COLUMN_CART_INGREDIENT_PRICE,
            )

        try {
            val mCursor = db.query(
                ShoppingCart.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            // Clear data
            shoppingCartList.clear()

            if (mCursor != null) {
                mCursor.moveToFirst()
                for (i in 0 until mCursor.count) {
                    val ingredient =
                        IngredientModel(
                            mCursor.getString(4),
                            mCursor.getString(2),
                            mCursor.getInt(3),
                            mCursor.getString(5)
                        )
                    shoppingCartList.add(ingredient)
                    mCursor.moveToNext()
                }
                Log.d(
                    "Ingredient",
                    shoppingCartList.get(0).Name + "\n" + shoppingCartList.get(0).Image + "\n" + shoppingCartList.get(
                        0
                    ).isAdded + "\n" + shoppingCartList.get(0).Price
                )
                mCursor.close()

                if (mCursor.getString(1) == recipesModel!!.Name)
                    return shoppingCartList
                else return ArrayList()
            }
            return shoppingCartList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return shoppingCartList
    }
}