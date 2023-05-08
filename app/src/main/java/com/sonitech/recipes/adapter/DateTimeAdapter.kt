package com.sonitech.recipes.adapter

import android.content.Context
import android.graphics.Color
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
import com.sonitech.recipes.model.DateTimeModel
import com.sonitech.recipes.model.IngredientModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DateTimeAdapter(
    val context: Context,
    val dateTimeList: ArrayList<DateTimeModel>,
    val onClickListeners: OnClickListeners
) :
    RecyclerView.Adapter<DateTimeAdapter.DateTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateTimeViewHolder =
        DateTimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_datetime, parent, false)
        )

    override fun onBindViewHolder(holder: DateTimeViewHolder, position: Int) {

        val dateTime = dateTimeList[position]

        if (dateTime.dateTime == SimpleDateFormat("EEE\ndd", Locale.ROOT).format(Date()))
            holder.tvDateTime.setTextColor(Color.parseColor("#FF6200EE"));

        holder.tvDateTime.text = dateTime.dateTime

        holder.tvDateTime.setOnClickListener {
            onClickListeners.onClick(dateTime, holder, "DateTime")
        }
    }

    override fun getItemCount(): Int = dateTimeList.size

    inner class DateTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDateTime: TextView = itemView.findViewById(R.id.tv_datetime)
    }
}