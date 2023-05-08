package com.sonitech.recipes.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonitech.recipes.R
import com.sonitech.recipes.adapter.IngredientsAdapter
import com.sonitech.recipes.databinding.FragmentCartBinding
import com.sonitech.recipes.db.ShoppingCart
import com.sonitech.recipes.db.SqliteDatabases
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.IngredientModel

class CartFragment : Fragment(), OnClickListeners {
    private var binding: FragmentCartBinding? = null
    private var fireCloudStore: FirebaseFirestore? = null
    private var ingredientsAdapter: IngredientsAdapter? = null
    private var shoppingCartList: ArrayList<IngredientModel> = ArrayList()
    private var sqliteDatbases: SqliteDatabases? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_cart, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        // Init firebase cloud store
        sqliteDatbases = SqliteDatabases(requireActivity())

        // Get recipes firebase data
        getShoppingCartList()

        // Init recyclerview
        binding!!.rvShoppingCart.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding!!.rvShoppingCart.hasFixedSize()
        ingredientsAdapter = IngredientsAdapter(requireActivity(), getShoppingCartList(), this, ArrayList(), "Cart")
        binding!!.rvShoppingCart.adapter = ingredientsAdapter
        ingredientsAdapter!!.notifyDataSetChanged()

        return binding!!.root
    }

    private fun getShoppingCartList(): List<IngredientModel> {
        val db = sqliteDatbases!!.readableDatabase

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
                        IngredientModel(mCursor.getString(4), mCursor.getString(2), mCursor.getInt(3), mCursor.getString(5))
                    shoppingCartList.add(ingredient)
                    mCursor.moveToNext()
                }
                Log.d("Ingredient", shoppingCartList.get(0).Name + "\n" + shoppingCartList.get(0).Image + "\n" + shoppingCartList.get(0).isAdded + "\n" + shoppingCartList.get(0).Price)
                mCursor.close()
                return shoppingCartList
            }
            return shoppingCartList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        if (shoppingCartList.isNotEmpty()) {
            binding!!.rvShoppingCart.visibility = View.VISIBLE
            binding!!.tvDataNotFound.visibility = View.GONE
        } else {
            binding!!.rvShoppingCart.visibility = View.GONE
            binding!!.tvDataNotFound.visibility = View.VISIBLE
        }
        return shoppingCartList
    }

    override fun <T> onClick(item: T, holder: RecyclerView.ViewHolder, type: String) {

        val ingredient = item as IngredientModel
        val ingredientsViewHolder = holder as IngredientsAdapter.IngredientsViewHolder

        if (type == getString(R.string.added_in_cart)) {
            deleteShoppingCart(ingredient, ingredientsViewHolder, type)
        }
    }

    private fun deleteShoppingCart(
        ingredient: IngredientModel,
        holder: IngredientsAdapter.IngredientsViewHolder,
        type: String
    ) {
        sqliteDatbases!!.deleteShoppingCartIngredient(ingredient.Name)
        ingredient.isAdded = 0
        holder.tvAddToCart.text = getString(R.string.add_to_cart)

        // Refresh data
        getShoppingCartList()
        ingredientsAdapter!!.notifyDataSetChanged()
    }
}