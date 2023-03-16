package goje.contactsapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        if (scale > 1.0){
           Constants.USER_ENABLED_BIG_FONT_SIZE = true
        }
    }

    private fun whenBackButtonClickedReturnToAgendaFragment() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main2)
                if (currentFragment?.childFragmentManager?.fragments?.get(0) is RecentCallsFragment){ return;}
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