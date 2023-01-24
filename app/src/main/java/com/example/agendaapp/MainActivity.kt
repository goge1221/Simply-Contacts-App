package com.example.agendaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.agendaapp.databinding.ActivityMainBinding
import com.example.agendaapp.fragments.BlankFragment

class MainActivity : AppCompatActivity() {
    
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initializeFragment()
    }

    private fun initializeFragment(){
        supportFragmentManager.beginTransaction()
            .add(BlankFragment(), "blank")
            .commit()
    }

}