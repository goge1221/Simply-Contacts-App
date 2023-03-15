package goje.contactsapp.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionChecker {

    fun userHasSpecifiedPermission(context: Context?, permission: String) = context?.let {
        ActivityCompat.checkSelfPermission(
            it,
            permission
        )
    } == PackageManager.PERMISSION_GRANTED

}