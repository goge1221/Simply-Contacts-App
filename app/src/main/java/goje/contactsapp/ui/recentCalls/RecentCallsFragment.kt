package goje.contactsapp.ui.recentCalls

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentRecentCallsBinding
import goje.contactsapp.entity.RecentCall
import goje.contactsapp.recyclerViews.agendaRecyclerView.IRecentCallClickListener
import goje.contactsapp.recyclerViews.recentCallsRecyclerView.RecentCallsAdapter
import goje.contactsapp.ui.detailedView.DetailedRecentContactFragment
import goje.contactsapp.utils.Constants.PERMISSION_TO_READ_CALL_LOG
import goje.contactsapp.utils.PermissionChecker


class RecentCallsFragment : Fragment(), IRecentCallClickListener {

    private var _binding: FragmentRecentCallsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var notificationsViewModel: RecentCallsViewModel

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

    private fun initializePermissionsNotGrantedLayout() {
        binding.linearLayout.visibility = View.VISIBLE
    }

    private fun setGrantPermissionsButtonListener() {
        binding.grantPermissionsButton.setOnClickListener {
            requestPermissionToReadCallLog()
        }
    }

    private fun changeLayoutToPermissionsGranted() {
        binding.linearLayout.visibility = View.GONE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        binding.agendaRecyclerView.adapter =
            RecentCallsAdapter(notificationsViewModel.recentCallsList.value!!, this)
    }

    private fun initializeViewModel() {
        notificationsViewModel = ViewModelProvider(this)[RecentCallsViewModel::class.java]
        notificationsViewModel.retrieveReentCalls()
    }

    private fun requestPermissionToReadCallLog() {
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