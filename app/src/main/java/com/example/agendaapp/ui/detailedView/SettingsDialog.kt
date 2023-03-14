package com.example.agendaapp.ui.detailedView

import android.R
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.agendaapp.databinding.SettingsDialogBinding


class SettingsDialog(
    context: Context, themeResId: Int,
    private val container: ViewGroup?,
) : Dialog(context, themeResId), AdapterView.OnItemSelectedListener {

    private lateinit var binding: SettingsDialogBinding
    private var userLanguagePreference = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsDialogBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        setContentView(binding.root)
        setOnClickListeners()
        setUpSpinner()
    }

    private fun setUpSpinner() {
        val spinner: Spinner = binding.languageSpinner
        spinner.onItemSelectedListener = this

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", MODE_PRIVATE)
        val userChoice = sharedPreferences.getString("user_language", "English")

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            context,
            com.example.agendaapp.R.array.languages,
            R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setSelection(adapter.getPosition(userChoice))
        }
    }

    private fun setOnClickListeners() {
        binding.doneButton.setOnClickListener {
            saveUserChoice()
            dismiss()
        }
    }

    private fun saveUserChoice(){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_language", userLanguagePreference)
        editor.apply()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        userLanguagePreference = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

}
