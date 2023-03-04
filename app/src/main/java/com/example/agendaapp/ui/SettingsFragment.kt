package com.example.agendaapp.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.agendaapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}