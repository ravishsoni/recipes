package com.sonitech.recipes.db

import android.provider.BaseColumns

object MealPlan: BaseColumns {
    val TABLE_NAME = "tb_meal_plan"
    val COLUMN_MEAL_PLAN_ID = "meal_plan_id"
    val COLUMN_DATE_TIME = "date_time"
    val COLUMN_BREAKFAST = "breakfast"
    val COLUMN_LUNCH = "lunch"
    val COLUMN_DINNER = "dinner"
}