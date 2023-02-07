package com.example.agendaapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agendaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_TO_READ_CONTACTS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (hasPermissionToReadContacts()) {
         //   val navView: BottomNavigationView = binding.navView
         //   val navController = findNavController(R.id.nav_host_fragment_activity_main2)
          //  navView.setupWithNavController(navController)
        } else requestPermissionToReadContacts()
    }

    private fun hasPermissionToReadContacts() = ActivityCompat.checkSelfPermission(
        this@MainActivity,
        android.Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED


    private fun requestPermissionToReadContacts() {
        if (!hasPermissionToReadContacts()){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISSION_TO_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_TO_READ_CONTACTS && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.i("PERMISSION_READ_STORAGE", "${permissions[i]} granted.")
                }
            }
        }
    }


}