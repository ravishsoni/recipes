package com.sonitech.recipes.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonitech.recipes.R
import com.sonitech.recipes.activity.RecipeDetailsActivity
import com.sonitech.recipes.adapter.RecipesAdapter
import com.sonitech.recipes.databinding.FragmentFavoriteBinding
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.RecipesModel

class FavoriteFragment : Fragment(), OnClickListeners {
    private var binding: FragmentFavoriteBinding? = null
    private var fireCloudStore: FirebaseFirestore? = null
    private var recipesAdapter: RecipesAdapter? = null
    private var recipesList: ArrayList<RecipesModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_favorite, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)

        // Init firebase cloud store
        fireCloudStore = FirebaseFirestore.getInstance()

        // Get recipes firebase data
        getRecipes()

        // Init recyclerview
        binding!!.rvFavorites.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding!!.rvFavorites.hasFixedSize()
        recipesAdapter = RecipesAdapter(requireActivity(), recipesList, this)
        binding!!.rvFavorites.adapter = recipesAdapter
        recipesAdapter!!.notifyDataSetChanged()

        return binding!!.root
    }

    private fun getRecipes() {
        // Get data ref
        val docRef = fireCloudStore!!.collection("Recipes")
        docRef.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                val documentList = querySnapshot.documents
                recipesList.clear()
                for (document in documentList) {
                    val recipe: RecipesModel = document.toObject(RecipesModel::class.java)!!
                    if (recipe.Fav == 1) {
                        recipe.id = document.id
                        recipesList.add(recipe)
                        Log.d("Fav Recipe", recipe.Name)
                    }
                }
                recipesAdapter!!.notifyDataSetChanged()
            }

            if (recipesList.isNotEmpty()) {
                binding!!.rvFavorites.visibility = View.VISIBLE
                binding!!.tvDataNotFound.visibility = View.GONE
            } else {
                binding!!.rvFavorites.visibility = View.GONE
                binding!!.tvDataNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun <T> onClick(item: T, holder: RecyclerView.ViewHolder, type: String) {

        val recipe = item as RecipesModel

        if (type == "RecipeDetails") {
            val recipeBundle = Bundle()
            recipeBundle.putSerializable("BUNDLE_RECIPE", recipe)
            startActivity(
                Intent(
                    context,
                    RecipeDetailsActivity::class.java
                ).putExtras(recipeBundle)
            )
        } else if (type == "FavRecipe") {
            updateFavRecipe(recipe, holder as RecipesAdapter.RecipesViewHolder)
        }
    }

    private fun updateFavRecipe(recipe: RecipesModel, holder: RecipesAdapter.RecipesViewHolder) {

        if (recipe.Fav == 1) {
            recipe.Fav = 0

            holder.ivFav.setImageResource(R.drawable.ic_favorite_not)
            holder.ivFav.setColorFilter(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.purple_500
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }

        fireCloudStore!!.collection("Recipes").document(recipe.id).update("Fav", recipe.Fav)
            .addOnSuccessListener { documentReference ->
                recipesList.remove(recipe)

                // Refresh data
                getRecipes()

                Toast.makeText(requireActivity(), "Fav Recipe Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(), exception.message, Toast.LENGTH_SHORT).show()
            }
    }
}