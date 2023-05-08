package com.sonitech.recipes.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sonitech.recipes.R
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.IngredientModel
import de.hdodenhof.circleimageview.CircleImageView

class IngredientsAdapter(
    val context: Context,
    val ingredients: List<IngredientModel>,
    val onClickListeners: OnClickListeners,
    var shoppingCartList: ArrayList<IngredientModel>,
    val type: String
) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder =
        IngredientsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_ingredients, parent, false)
        )

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {

        val ingredient = ingredients[position]
        val ingredientsName = ingredient.Name
        val ingredientsPic = ingredient.Image
        val ingredientsPrice = ingredient.Price

        val shoppingCart = IngredientModel()
        if (shoppingCartList.isNotEmpty()) {
            val index = 0
            for (i in index until shoppingCartList.size) {
                if (!TextUtils.isEmpty(shoppingCartList[i].Name) && shoppingCartList[i].Name == ingredientsName) {
                    holder.tvAddToCart.text = context.getString(R.string.added_in_cart)
                }
            }
        }

        if (type == "Cart")
            holder.tvAddToCart.text = context.getString(R.string.added_in_cart)

        holder.tvIngredient.text = ingredientsName
        holder.tvIngredientPrice.text = "₹ $ingredientsPrice"
        Glide.with(context).load(ingredientsPic).placeholder(R.drawable.ic_menu)
            .error(R.drawable.ic_menu).into(holder.ivIngredient)

        holder.tvAddToCart.setOnClickListener {
            onClickListeners.onClick(
                ingredient,
                holder,
                holder.tvAddToCart.text.toString().trim()
            )
        }
    }

    override fun getItemCount(): Int = ingredients.size

    inner class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIngredient: CircleImageView = itemView.findViewById(R.id.iv_ingredient)
        val tvIngredient: TextView = itemView.findViewById(R.id.tv_ingredient)
        val cvIngredient: CardView = itemView.findViewById(R.id.cv_ingredient)
        val tvAddToCart: TextView = itemView.findViewById(R.id.tv_add_to_cart)
        val tvIngredientPrice: TextView = itemView.findViewById(R.id.tv_ingredient_price)
    }
}