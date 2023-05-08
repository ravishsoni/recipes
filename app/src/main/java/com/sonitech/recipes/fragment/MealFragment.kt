package com.sonitech.recipes.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonitech.recipes.R
import com.sonitech.recipes.adapter.DateTimeAdapter
import com.sonitech.recipes.databinding.FragmentMealBinding
import com.sonitech.recipes.db.MealPlan
import com.sonitech.recipes.db.SqliteDatabases
import com.sonitech.recipes.interfaces.OnClickListeners
import com.sonitech.recipes.model.DateTimeModel
import com.sonitech.recipes.model.MealPlanModel
import java.text.SimpleDateFormat
import java.util.*

class MealFragment : Fragment(), OnClickListeners, View.OnClickListener {
    private var binding: FragmentMealBinding? = null
    private var dateTimeList: ArrayList<DateTimeModel> = ArrayList()
    private var sqliteHelper: SqliteDatabases? = null
    private var mealPlanList: ArrayList<MealPlanModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_meal, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)

        binding!!.tvSave.setOnClickListener(this)
        binding!!.tvUpdate.setOnClickListener(this)
        binding!!.tvDelete.setOnClickListener(this)

        sqliteHelper = SqliteDatabases(requireActivity())

        checkMealPlan()

        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val sdf = SimpleDateFormat("EEE\ndd", Locale.ROOT)
        val monthName = android.text.format.DateFormat.format("MMMM", Date()) as String

        for (i in 0..6) {
            val date: Date = cal.time
            dateTimeList.add(
                DateTimeModel(
                    sdf.format(cal.time),
                    android.text.format.DateFormat.format("EEEE, MMMM dd", date).toString()
                )
            )
            cal.add(Calendar.DAY_OF_WEEK, 1)
        }

        binding!!.tvMonth.text = monthName
        binding!!.rvDateTime.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvDateTime.hasFixedSize()
        binding!!.rvDateTime.isLayoutFrozen = true
        binding!!.rvDateTime.adapter = DateTimeAdapter(requireActivity(), dateTimeList, this)
        return binding!!.root
    }

    override fun <T> onClick(item: T, holder: RecyclerView.ViewHolder, type: String) {

        val dateTime: DateTimeModel = item as DateTimeModel
        if (type == "DateTime") {
            binding!!.clDateTime.visibility = View.VISIBLE
            binding!!.tvDateTime.text = dateTime.dayDateTime
            binding!!.etBreakfast.text!!.clear()
            binding!!.etLunch.text!!.clear()
            binding!!.etDinner.text!!.clear()

            binding!!.tvUpdate.visibility = View.GONE
            binding!!.tvDelete.visibility = View.GONE
            binding!!.tvSave.visibility = View.VISIBLE

            if (mealPlanList.isNotEmpty()) {
                val index = 0
                for (i in index until mealPlanList.size) {
                    if (dateTime.dayDateTime == mealPlanList[i].dateTime) {
                        binding!!.etBreakfast.setText(mealPlanList[i].breakfast)
                        binding!!.etLunch.setText(mealPlanList[i].lunch)
                        binding!!.etDinner.setText(mealPlanList[i].dinner)

                        binding!!.tvUpdate.visibility = View.VISIBLE
                        binding!!.tvDelete.visibility = View.VISIBLE
                        binding!!.tvSave.visibility = View.GONE
                    }
                    Log.d("Date", mealPlanList[i].dateTime)
                }
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tv_save ->
                saveMealPlan()

            R.id.tv_update ->
                updateMealPlan()

            R.id.tv_delete ->
                deleteMealPlan()
        }
    }

    private fun saveMealPlan() {
        sqliteHelper!!.writableDatabase
        val isAdded = sqliteHelper!!.addMealPlanning(
            binding!!.tvDateTime.text.toString().trim(),
            binding!!.etBreakfast.text.toString().trim(),
            binding!!.etLunch.text.toString().trim(),
            binding!!.etDinner.text.toString().trim(),
        )

        binding!!.etBreakfast.text!!.clear()
        binding!!.etLunch.text!!.clear()
        binding!!.etDinner.text!!.clear()

        if (isAdded)
            Toast.makeText(requireActivity(), "Data saved successfully", Toast.LENGTH_SHORT).show()

        checkMealPlan()
    }

    private fun updateMealPlan() {
        sqliteHelper!!.writableDatabase
        val isUpdated = sqliteHelper!!.updateMealPan(
            binding!!.tvDateTime.text.toString().trim(),
            binding!!.etBreakfast.text.toString().trim(),
            binding!!.etLunch.text.toString().trim(),
            binding!!.etDinner.text.toString().trim(),
        )

        binding!!.etBreakfast.text!!.clear()
        binding!!.etLunch.text!!.clear()
        binding!!.etDinner.text!!.clear()

        if (isUpdated)
            Toast.makeText(requireActivity(), "Data updated successfully", Toast.LENGTH_SHORT)
                .show()

        checkMealPlan()
    }

    private fun deleteMealPlan() {
        sqliteHelper!!.writableDatabase
        val isDeleted = sqliteHelper!!.deleteMealPlan(
            binding!!.tvDateTime.text.toString().trim()
        )

        binding!!.etBreakfast.text!!.clear()
        binding!!.etLunch.text!!.clear()
        binding!!.etDinner.text!!.clear()

        if (isDeleted)
            Toast.makeText(requireActivity(), "Data deleted successfully", Toast.LENGTH_SHORT)
                .show()

        checkMealPlan()
    }

    private fun checkMealPlan(): ArrayList<MealPlanModel> {
        val db = sqliteHelper!!.readableDatabase

        val projection =
            arrayOf(
                MealPlan.COLUMN_MEAL_PLAN_ID,
                MealPlan.COLUMN_DATE_TIME,
                MealPlan.COLUMN_BREAKFAST,
                MealPlan.COLUMN_LUNCH,
                MealPlan.COLUMN_DINNER
            )

        try {
            val mCursor = db.query(
                MealPlan.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            // Clear data
            mealPlanList.clear()

            if (mCursor != null) {
                mCursor.moveToFirst()
                for (i in 0 until mCursor.count) {
                    val mealPlan =
                        MealPlanModel(
                            mCursor.getString(1),
                            mCursor.getString(2),
                            mCursor.getString(3),
                            mCursor.getString(4)
                        )
                    mealPlanList.add(mealPlan)
                    mCursor.moveToNext()
                }
                mCursor.close()

                return mealPlanList
            }
            return mealPlanList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mealPlanList
    }
}