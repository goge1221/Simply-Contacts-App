package com.example.agendaapp.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

class PermissionHandler(
    private val requestPermissionLauncher: ActivityResultLauncher<String>,
    private val context: Context
) {

    fun askPermissions(){
        checkPermissionToReadContacts()
        checkPermissionToMakeCall()
    }

    private fun checkPermissionToMakeCall(){
        if (!userHasPermissionToCall())
            requestPermissionToCall()
    }

    private fun requestPermissionToCall(){
        requestPermissionLauncher.launch(
            Manifest.permission.CALL_PHONE
        )
    }

    private fun userHasPermissionToCall(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkPermissionToReadContacts() {
        if (!userHasPermission())
            requestPermission()

    }

    private fun userHasPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            Manifest.permission.READ_CONTACTS
        )
    }
}