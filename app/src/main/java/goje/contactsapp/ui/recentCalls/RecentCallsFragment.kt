package goje.contactsapp.ui.recentCalls

import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentRecentCallsBinding
import goje.contactsapp.entity.RecentCall
import goje.contactsapp.recyclerViews.agendaRecyclerView.IRecentCallClickListener
import goje.contactsapp.recyclerViews.recentCallsRecyclerView.RecentCallsAdapter
import goje.contactsapp.ui.agenda.AgendaViewModel
import goje.contactsapp.ui.detailedView.DetailedRecentContactFragment
import goje.contactsapp.utils.Constants
import goje.contactsapp.utils.Constants.PERMISSION_TO_READ_CALL_LOG
import goje.contactsapp.utils.Constants.REQUEST_CODE_SET_DEFAULT_DIALER
import goje.contactsapp.utils.PermissionChecker


class RecentCallsFragment : Fragment(), IRecentCallClickListener {

    private var _binding: FragmentRecentCallsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var notificationsViewModel: RecentCallsViewModel
    private lateinit var agendaViewModel: AgendaViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PermissionChecker.userHasSpecifiedPermission(
                context,
                android.Manifest.permission.READ_CALL_LOG
            )
        ) {
            initializeViewModel()
            changeLayoutToPermissionsGranted()
        } else {
            requestPermissionToReadCallLog()
            setGrantPermissionsButtonListener()
        }
    }

    private fun appIsDefaultHandler(): Boolean {
        val telecomManager =
            requireActivity().getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
        return requireActivity().packageName == telecomManager.defaultDialerPackage
    }

    private fun makeAppDefaultCallerApp() {
        Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
            TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
            requireActivity().packageName
        ).apply {
            if (resolveActivity(requireActivity().packageManager) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val rm: RoleManager? = requireActivity().getSystemService(RoleManager::class.java)
                    if (rm?.isRoleAvailable(RoleManager.ROLE_DIALER) == true) {
                        @Suppress("DEPRECATION")
                        requireActivity().startActivityForResult(
                            rm.createRequestRoleIntent(RoleManager.ROLE_DIALER),
                            REQUEST_CODE_SET_DEFAULT_DIALER
                        )
                    }
                } else {
                    @Suppress("DEPRECATION")
                    requireActivity().startActivityForResult(this, REQUEST_CODE_SET_DEFAULT_DIALER)
                }
            }
        }
    }

    private fun initializePermissionsNotGrantedLayout() {
        binding.linearLayout.visibility = View.VISIBLE
        if (Constants.fontSize > 1.2)
            binding.image.visibility = View.GONE
    }

    private fun setGrantPermissionsButtonListener() {
        binding.grantPermissionsButton.setOnClickListener {
            if (appIsDefaultHandler())
                requestPermissionToReadCallLog()
            else {
                makeAppDefaultCallerApp()
                if (appIsDefaultHandler())
                    requestPermissionToReadCallLog()
                else
                    initializePermissionsNotGrantedLayout()
            }
        }
    }

    private fun changeLayoutToPermissionsGranted() {
        binding.linearLayout.visibility = View.GONE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        binding.agendaRecyclerView.adapter =
            RecentCallsAdapter(notificationsViewModel.recentCallsList.value!!, this)
    }

    private fun initializeViewModel() {
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        agendaViewModel.retrieveContacts()
        notificationsViewModel = ViewModelProvider(this)[RecentCallsViewModel::class.java]
        agendaViewModel.contactsList.value?.let { notificationsViewModel.retrieveReentCalls(it) }
    }

    private fun requestPermissionToReadCallLog() {

        if (!appIsDefaultHandler()) {
            initializePermissionsNotGrantedLayout()
            makeAppDefaultCallerApp()
        }

        if (appIsDefaultHandler()) {

            if (!PermissionChecker.userHasSpecifiedPermission(
                    context,
                    android.Manifest.permission.READ_CALL_LOG
                )
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CALL_LOG),
                    PERMISSION_TO_READ_CALL_LOG
                )
            }
            else{
                initializeViewModel()
                changeLayoutToPermissionsGranted()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_TO_READ_CALL_LOG && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Permission to read contacts was granted
                    initializeViewModel()
                    changeLayoutToPermissionsGranted()
                    return
                }
            }
        }
        initializePermissionsNotGrantedLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun openRecentCall(call: RecentCall) {
        hideToolAndNavBar()
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_fragment_activity_main2,
                DetailedRecentContactFragment(call),
                "DETAILED_CONTACT_FRAGMENT"
            )
            .addToBackStack(tag)
            .commit()
    }

    private fun hideToolAndNavBar() {
        val toolBar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.GONE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.GONE
    }
}