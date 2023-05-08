package bully.app.pedia.diffuitls

import androidx.recyclerview.widget.DiffUtil
import com.sonitech.recipes.model.RecipesModel

class RecipeDiffUtil(
    oldAddedRecipeList: List<RecipesModel?>?,
    newAddedRecipeList: ArrayList<RecipesModel?>?
) : DiffUtil.Callback() {

    var oldAddedRecipeList: List<RecipesModel?>? = ArrayList<RecipesModel>()
    var newAddedRecipeList: List<RecipesModel?>? = ArrayList<RecipesModel>()

    init {
        this.oldAddedRecipeList = oldAddedRecipeList
        this.newAddedRecipeList = newAddedRecipeList
    }

    override fun getOldListSize(): Int {
        return if (oldAddedRecipeList != null) oldAddedRecipeList!!.size else 0
    }

    override fun getNewListSize(): Int {
        return if (newAddedRecipeList != null) newAddedRecipeList!!.size else 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newAddedRecipeList!![newItemPosition] === oldAddedRecipeList!![oldItemPosition]
    }
}