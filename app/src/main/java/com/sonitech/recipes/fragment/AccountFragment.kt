package com.sonitech.recipes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sonitech.recipes.R
import com.sonitech.recipes.databinding.FragmentAccountBinding
import com.sonitech.recipes.db.SharedPreferences

class AccountFragment : Fragment(), View.OnClickListener {
   private var binding: FragmentAccountBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_account, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        // Get user data
        getUserData()

        binding!!.tvSave.setOnClickListener(this)
        return binding!!.root
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tv_save -> {
                saveUserData()
            }
        }
    }

    private fun saveUserData() {
        SharedPreferences.setValues(requireActivity(), "Username", binding!!.etUserName.text.toString().trim())
        SharedPreferences.setValues(requireActivity(), "Cuisinetype", binding!!.etCuisineType.text.toString().trim())
        SharedPreferences.setValues(requireActivity(), "DietaryRestrictions", binding!!.etDietaryRestrictions.text.toString().trim())
        Toast.makeText(requireActivity(), "User details saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getUserData() {
        binding!!.etUserName.setText(SharedPreferences.getValues(requireActivity(), "Username"))
        binding!!.etCuisineType.setText(SharedPreferences.getValues(requireActivity(), "Cuisinetype"))
        binding!!.etDietaryRestrictions.setText(SharedPreferences.getValues(requireActivity(), "DietaryRestrictions"))
    }
}