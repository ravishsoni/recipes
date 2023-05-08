package com.sonitech.recipes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bully.app.pedia.diffuitls.RecipeDiffUtil
import com.bumptech.glide.Glide
import com.sonitech.recipes.R
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.RecipesModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.reflect.KFunction1

class RecipesAdapter(
    val context: Context,
    val recipes: ArrayList<RecipesModel>,
    val onClickListener: OnClickListeners
) :
    RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder =
        RecipesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_recipes, parent, false)
        )

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val recipe = recipes[position]
        val recipeName = recipe.Name
        val recipePic = recipe.Image
        val recipeType = recipe.CuisineType

        holder.tvRecipe.text = recipeName
        holder.tvRecipeType.text = recipeType
        Glide.with(context).load(recipePic).placeholder(R.drawable.ic_menu)
            .error(R.drawable.ic_menu).into(holder.ivRecipe)

        // Check fav recipe
        if (recipe.Fav == 1) {
            holder.ivFav.setImageResource(R.drawable.ic_favorite)
            holder.ivFav.setColorFilter(ContextCompat.getColor(context, R.color.purple_500), android.graphics.PorterDuff.Mode.MULTIPLY)
        } else {
            holder.ivFav.setImageResource(R.drawable.ic_favorite_not)
            holder.ivFav.setColorFilter(ContextCompat.getColor(context, R.color.purple_500), android.graphics.PorterDuff.Mode.MULTIPLY)
        }


        holder.cvRecipe.setOnClickListener {
            onClickListener.onClick(recipe, holder, "RecipeDetails")
        }

        holder.ivFav.setOnClickListener {
            onClickListener.onClick(recipe, holder, "FavRecipe")
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipe(filteredRecipeList: ArrayList<RecipesModel?>?) {
        val diffResult =
            DiffUtil.calculateDiff(RecipeDiffUtil(recipes, filteredRecipeList))
        recipes.clear()
        recipes.addAll(filteredRecipeList as ArrayList<RecipesModel>)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivRecipe: CircleImageView = itemView.findViewById(R.id.iv_recipe)
        val tvRecipe: TextView = itemView.findViewById(R.id.tv_recipe)
        val cvRecipe: CardView = itemView.findViewById(R.id.cv_recipe)
        val tvRecipeType: TextView = itemView.findViewById(R.id.tv_recipe_type)
        val ivFav: ImageView = itemView.findViewById(R.id.iv_fav)
    }
}