package com.sonitech.recipes.interfaces

import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface OnClickListeners {
    fun <T> onClick(item: T, holder: ViewHolder, type: String)
}