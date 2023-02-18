package com.example.agendaapp.ui.agenda

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agendaapp.R
import com.example.agendaapp.databinding.FragmentAgendaBinding
import com.example.agendaapp.entity.Contact
import com.example.agendaapp.recyclerViews.agendaRecyclerView.AgendaAdapter
import com.example.agendaapp.ui.detailedView.DetailedContactFragment
import com.example.agendaapp.utils.Constants
import com.example.agendaapp.utils.PermissionChecker
import com.google.android.material.bottomnavigation.BottomNavigationView


class AgendaFragment : Fragment(), OnContactClickedListener {

    private var _binding: FragmentAgendaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var agendaViewModel: AgendaViewModel
    private lateinit var agendaObserver: AgendaObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.READ_CONTACTS)) {
            initializeViewModel()
            changeLayoutToPermissionsGranted()
        } else {
            requestPermissionToReadContacts()
            setGrantPermissionsButtonListener()
        }

    }

    private fun addObserverToContactsList(){
        // Create the observer which updates the UI.
        val nameObserver = Observer<List<Contact>> { updatedContactsList ->
            // Update the UI, in this case, a TextView.
            agendaObserver.updateContactsList(updatedContactsList)
            Log.e("updated_list", updatedContactsList.toString())
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        agendaViewModel.contactsList.observe(viewLifecycleOwner, nameObserver)
    }

    private fun initializeViewModel() {
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        addObserverToContactsList()
    }

    private fun initializePermissionsNotGrantedLayout() {
        binding.linearLayout.visibility = View.VISIBLE
    }

    private fun changeLayoutToPermissionsGranted() {
        binding.linearLayout.visibility = View.GONE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        val agendaAdapter = AgendaAdapter(agendaViewModel.contactsList.value!!, this)
        agendaObserver = agendaAdapter
        binding.agendaRecyclerView.adapter = agendaAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setGrantPermissionsButtonListener() {
        binding.grantPermissionsButton.setOnClickListener {
            requestPermissionToReadContacts()
        }
    }

    private fun requestPermissionToReadContacts() {
        if (!PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.READ_CONTACTS)) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                Constants.PERMISSION_TO_READ_CONTACTS
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.PERMISSION_TO_READ_CONTACTS && grantResults.isNotEmpty()) {
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

    override fun selectContact(contact: Contact) {
        hideToolAndNavBar()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main2, DetailedContactFragment(contact), "DETAILED_CONTACT_FRAGMENT")
            .addToBackStack(tag)
            .commit()
    }

    private fun hideToolAndNavBar(){
        val toolBar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.GONE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.GONE
    }


}