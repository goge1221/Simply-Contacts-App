package goje.contactsapp

import android.app.role.RoleManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.databinding.ActivityMainBinding
import goje.contactsapp.ui.detailedView.AddNewContactFragment
import goje.contactsapp.ui.detailedView.DetailedContactFragment
import goje.contactsapp.ui.detailedView.DetailedRecentContactFragment
import goje.contactsapp.ui.recentCalls.RecentCallsFragment
import goje.contactsapp.utils.Constants
import goje.contactsapp.utils.Constants.REQUEST_CODE_SET_DEFAULT_DIALER


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navView.setupWithNavController(navController)

        whenBackButtonClickedReturnToAgendaFragment()

        val c: Configuration = resources.configuration
        val scale: Float = c.fontScale
        Constants.fontSize = scale
        if (scale > 1.6) {
            Constants.USER_ENABLED_BIG_FONT_SIZE = true
        }
        launchSetDefaultDialerIntent(this)
        checkIfDefaultHandler()
    }

    private fun launchSetDefaultDialerIntent(activity: AppCompatActivity) {
        Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
            TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
            activity.packageName
        ).apply {
            if (resolveActivity(activity.packageManager) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val rm: RoleManager? = activity.getSystemService(RoleManager::class.java)
                    if (rm?.isRoleAvailable(RoleManager.ROLE_DIALER) == true) {
                        @Suppress("DEPRECATION")
                        activity.startActivityForResult(
                            rm.createRequestRoleIntent(RoleManager.ROLE_DIALER),
                            REQUEST_CODE_SET_DEFAULT_DIALER
                        )
                    }
                } else {
                    @Suppress("DEPRECATION")
                    activity.startActivityForResult(this, REQUEST_CODE_SET_DEFAULT_DIALER)
                }
            }
        }
    }

    private fun checkIfDefaultHandler() {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val isAlreadyDefaultDialer = packageName == telecomManager.defaultDialerPackage
        Constants.DEFAULT_PHONE_HANDLER = isAlreadyDefaultDialer
    }

    private fun whenBackButtonClickedReturnToAgendaFragment() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main2)
                if (currentFragment?.childFragmentManager?.fragments?.get(0) is RecentCallsFragment) {
                    return;
                }
                if (currentFragment?.childFragmentManager?.fragments?.get(0) is DetailedContactFragment
                    || currentFragment?.childFragmentManager?.fragments?.get(0) is AddNewContactFragment
                    || currentFragment?.childFragmentManager?.fragments?.get(0) is DetailedRecentContactFragment
                ) {
                    binding.navView.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }
                supportFragmentManager.popBackStack()
            }
        })
    }


}