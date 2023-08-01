package goje.contactsapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager


object PermissionChecker {

    fun userHasSpecifiedPermissionn(context: Context?, permission: String) = context?.let {
        ActivityCompat.checkSelfPermission(
            it,
            permission
        )
    } == PackageManager.PERMISSION_GRANTED


    fun userHasSpecifiedPermission(context: Context?, permission: String): Boolean? {

        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context!!)

        var permissionAskedAndAccepted = true

        if (permission == android.Manifest.permission.CALL_PHONE){
            permissionAskedAndAccepted = sharedPreferences.getBoolean("permission_to_call_granted", false);
        }

        else if(permission == android.Manifest.permission.READ_CALL_LOG){
            permissionAskedAndAccepted = sharedPreferences.getBoolean("permission_to_read_call_log_granted", false);
        }

        val hasPermission = context.let {
            ActivityCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED
        }


        return hasPermission && permissionAskedAndAccepted;
    }

}