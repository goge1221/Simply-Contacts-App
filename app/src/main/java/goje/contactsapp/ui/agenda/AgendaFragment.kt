package goje.contactsapp.ui.agenda

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import goje.contactsapp.entity.Contact
import goje.contactsapp.recyclerViews.agendaRecyclerView.AgendaAdapter
import goje.contactsapp.ui.detailedView.AddNewContactFragment
import goje.contactsapp.ui.detailedView.DetailedContactFragment
import goje.contactsapp.utils.Constants
import goje.contactsapp.utils.PermissionChecker
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentAgendaBinding


class AgendaFragment : Fragment(), OnContactClickedListener, IContactDelete, IContactGetById {

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
        agendaViewModel.retrieveContacts()
    }

    private fun addNewContactListener(){
        binding.addContactButton.setOnClickListener {
            hideToolAndNavBar()
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main2, AddNewContactFragment(), "ADD_NEW_CONTACT_FRAGMENT")
                .addToBackStack(tag)
                .commit()
        }
    }

    private fun initializePermissionsNotGrantedLayout() {
        binding.linearLayout.visibility = View.VISIBLE
    }

    private fun changeLayoutToPermissionsGranted() {
        binding.linearLayout.visibility = View.GONE
        binding.addContactButton.visibility = View.VISIBLE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        val agendaAdapter = AgendaAdapter(agendaViewModel.contactsList.value!!, this)
        agendaObserver = agendaAdapter
        binding.agendaRecyclerView.adapter = agendaAdapter
        addNewContactListener()
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
            .replace(R.id.nav_host_fragment_activity_main2, DetailedContactFragment(contact, this, this), "DETAILED_CONTACT_FRAGMENT")
            .addToBackStack(tag)
            .commit()
    }

    private fun hideToolAndNavBar(){
        val toolBar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.GONE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.GONE
    }

    override fun deleteContact(contact: Contact) {
        val successfullyDeleted = agendaViewModel.deleteContact(requireContext(), contact.phoneNumber, contact.name)
        if (successfullyDeleted)
            Toast.makeText(context, contact.name + " deleted.", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "An error occurred during deletion.", Toast.LENGTH_SHORT).show()
    }

    override fun getContactById(contactId: String): Contact {
        return agendaViewModel.getContactById(contactId)
    }


}