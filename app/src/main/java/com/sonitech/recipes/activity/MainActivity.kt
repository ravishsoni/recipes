package com.sonitech.recipes.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sonitech.recipes.R
import com.sonitech.recipes.databinding.ActivityMainBinding
import com.sonitech.recipes.fragment.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set status color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        binding!!.ivRecipe.setOnClickListener(this)
        binding!!.ivFavorite.setOnClickListener(this)
        binding!!.ivCart.setOnClickListener(this)
        binding!!.ivMeal.setOnClickListener(this)
        binding!!.ivAccount.setOnClickListener(this)

        //Launch fragment
        pushFragment(RecipeFragment(), false, true, false, RecipeFragment::class.java.simpleName, null)
    }

    fun pushFragment(
        fragment: Fragment,
        isAdd: Boolean,
        isReplace: Boolean,
        isAddToBackStack: Boolean,
        tag: String,
        bundle: Bundle?
    ) {
        val fragmentTrascation = supportFragmentManager.beginTransaction()

        // Checkingn bundle
        if (bundle != null) fragment.arguments = bundle

        // Add fragment on screen
        if (isAdd) fragmentTrascation.add(R.id.fragment, fragment, tag)

        // Replace fragment on screen
        if (isReplace) fragmentTrascation.replace(R.id.fragment, fragment, tag)

        // Add back stach
        if (isAddToBackStack) fragmentTrascation.addToBackStack(tag)

        fragmentTrascation.commit()
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.iv_recipe ->
                setRecipeTab()

            R.id.iv_favorite ->
                setFavoriteTab()

            R.id.iv_cart ->
                setCartTab()

            R.id.iv_meal ->
                setMealTab()

            R.id.iv_account ->
                setAccountTab()
        }
    }

    private fun setRecipeTab() {
        binding!!.ivRecipe.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivFavorite.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivCart.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivMeal.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivAccount.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)

        pushFragment(RecipeFragment(), false, true, false, RecipeFragment::class.java.simpleName, null)
    }

    private fun setFavoriteTab() {
        binding!!.ivRecipe.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivFavorite.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivCart.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivMeal.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivAccount.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)

        pushFragment(FavoriteFragment(), false, true, false, FavoriteFragment::class.java.simpleName, null)
    }

    private fun setCartTab() {
        binding!!.ivRecipe.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivFavorite.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivCart.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivMeal.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivAccount.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)

        pushFragment(CartFragment(), false, true, false, CartFragment::class.java.simpleName, null)
    }

    private fun setMealTab() {
        binding!!.ivRecipe.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivFavorite.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivCart.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivMeal.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivAccount.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)

        pushFragment(MealFragment(), false, true, false, MealFragment::class.java.simpleName, null)
    }

    private fun setAccountTab() {
        binding!!.ivRecipe.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivFavorite.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivCart.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivMeal.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding!!.ivAccount.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

        pushFragment(AccountFragment(), false, true, false, MealFragment::class.java.simpleName, null)
    }
}