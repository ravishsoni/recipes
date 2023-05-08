package com.sonitech.recipes.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonitech.recipes.R
import com.sonitech.recipes.activity.RecipeDetailsActivity
import com.sonitech.recipes.adapter.RecipesAdapter
import com.sonitech.recipes.databinding.FragmentRecipeBinding
import com.sonitech.recipes.db.SqliteDatabases
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.RecipesModel
import java.util.*
import kotlin.collections.ArrayList

class RecipeFragment : Fragment(), View.OnClickListener, TextWatcher, OnClickListeners {
    private var binding: FragmentRecipeBinding? = null
    private var fireCloudStore: FirebaseFirestore? = null
    private var recipesAdapter: RecipesAdapter? = null
    private var recipesList: ArrayList<RecipesModel> = ArrayList()
    private var filteredRecipesList: ArrayList<RecipesModel?>? =
        java.util.ArrayList<RecipesModel?>()
    private var sqliteDatabases: SqliteDatabases? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_recipe, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container, false)
        val view = binding!!.root

        // Init database
        sqliteDatabases = SqliteDatabases(requireActivity())

        // Init firebase cloud store
        fireCloudStore = FirebaseFirestore.getInstance()

        binding!!.ivClear.setOnClickListener(this)
        binding!!.edtSearch.addTextChangedListener(this)

        // Get recipes firebase data
        getRecipes()

        // Init recyclerview
        binding!!.rvRecipes.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding!!.rvRecipes.hasFixedSize()
        recipesAdapter = RecipesAdapter(requireActivity(), recipesList, this)
        binding!!.rvRecipes.adapter = recipesAdapter
        recipesAdapter!!.notifyDataSetChanged()
        return view
    }

    private fun getRecipes() {
        // Get data ref
        val docRef = fireCloudStore!!.collection("Recipes")
        docRef.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot!!.isEmpty) {
                recipesList.clear()
                val documentList = querySnapshot.documents
                for (document in documentList) {
                    val recipe: RecipesModel = document.toObject(RecipesModel::class.java)!!
                    recipe.id = document.id
                    recipesList.add(recipe)
                    Log.d("Recipe", recipe.toString())
                }
                binding!!.rvRecipes.visibility = View.VISIBLE
                binding!!.tvDataNotFound.visibility = View.GONE
                recipesAdapter!!.notifyDataSetChanged()
            } else {
                binding!!.rvRecipes.visibility = View.GONE
                binding!!.tvDataNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.iv_clear ->
                clearSearchRecipeData()
        }
    }

    private fun clearSearchRecipeData() {
        val strSearchRecipe: String = binding!!.edtSearch.text.toString()
        if (!TextUtils.isEmpty(strSearchRecipe)) {
            clearRecipeDetails()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val strSearchRecipe: String = binding!!.edtSearch.text.toString()
        if (!TextUtils.isEmpty(strSearchRecipe)) {
            binding!!.ivClear.visibility = View.VISIBLE
        } else {
            binding!!.ivClear.visibility = View.GONE
            clearRecipeDetails()
        }

        // For filtering pedigrees data

        // For filtering pedigrees data
        this.filteredRecipesList!!.clear()
        val filteredRecipeList: MutableList<RecipesModel?>? = ArrayList<RecipesModel?>()
        for (recipe in recipesList) {
            if (recipe.Name.lowercase(Locale.getDefault())
                    .contains(strSearchRecipe) || recipe.CuisineType.lowercase(Locale.getDefault())
                    .contains(strSearchRecipe)
            ) {
                filteredRecipeList!!.add(recipe)
                this.filteredRecipesList = filteredRecipeList as ArrayList<RecipesModel?>?
            } else {
                for (ingredient in recipe.Ingredients) {
                    if (ingredient.Name.lowercase().contains(strSearchRecipe)) {
                        filteredRecipeList!!.add(recipe)
                        this.filteredRecipesList = filteredRecipeList as ArrayList<RecipesModel?>?
                    }
                }
            }
        }

        recipesAdapter!!.updateRecipe(this.filteredRecipesList)
        recipesAdapter!!.notifyDataSetChanged()
    }

    private fun clearRecipeDetails() {

        // Clear search text and list
        binding!!.edtSearch.text!!.clear()
        this.filteredRecipesList!!.clear()
        recipesAdapter!!.updateRecipe(this.filteredRecipesList)
        recipesAdapter!!.notifyDataSetChanged()

        // For refreshing data
        getRecipes()
    }

    override fun <T> onClick(item: T, holder: RecyclerView.ViewHolder, type: String) {

        val recipe = item as RecipesModel

        if (type == "RecipeDetails") {
            val recipeBundle = Bundle()
            recipeBundle.putSerializable("BUNDLE_RECIPE", recipe)
            startActivity(Intent(context, RecipeDetailsActivity::class.java).putExtras(recipeBundle))
        } else if (type == "FavRecipe") {
            updateFavRecipe(recipe, holder as RecipesAdapter.RecipesViewHolder)
        }
    }

    private fun updateFavRecipe(recipe: RecipesModel, holder: RecipesAdapter.RecipesViewHolder) {

        if (recipe.Fav == 0) {
            recipe.Fav = 1

            holder.ivFav.setImageResource(R.drawable.ic_favorite)
            holder.ivFav.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.purple_500), android.graphics.PorterDuff.Mode.MULTIPLY)
        } else {
            recipe.Fav = 0

            holder.ivFav.setImageResource(R.drawable.ic_favorite_not)
            holder.ivFav.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.purple_500), android.graphics.PorterDuff.Mode.MULTIPLY)
        }

        fireCloudStore!!.collection("Recipes").document(recipe.id).update("Fav", recipe.Fav)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireActivity(), "Fav Recipe Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(), exception.message, Toast.LENGTH_SHORT).show()
            }
    }
}