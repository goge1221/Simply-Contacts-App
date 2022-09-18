package com.example.agendaapp.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.agendaapp.R
import com.example.agendaapp.contactRecyclerView.ContactsAdapter
import com.example.agendaapp.databinding.FragmentAgendaBinding
import com.example.agendaapp.interfaces.OnContactClickedListener
import com.example.agendaapp.objects.Contact


class AgendaFragment : Fragment(), OnContactClickedListener {

    private var binding: FragmentAgendaBinding? = null

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: AgendaViewModel by activityViewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentInflater = FragmentAgendaBinding.inflate(inflater, container, false)
        binding = fragmentInflater
        return fragmentInflater.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        checkPermissionToReadContacts()
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        binding?.apply {
            recyclerView.adapter =
                ContactsAdapter(sharedViewModel.contactsList.value as ArrayList<Contact>, this@AgendaFragment)
        }
    }

    private fun checkPermissionToReadContacts() {
        if (!userHasPermission())
            requestPermission()
    }

    private fun userHasPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            binding?.root?.context!!,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            Manifest.permission.READ_CONTACTS
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun contactClicked(contact: Contact) {
        val bundle = Bundle().apply {
            putSerializable("CONTACT", contact)
        }


        findNavController().navigate(R.id.action_agendaFragment_to_detailCallFragment, bundle)
      //  findNavController().navigate(R.id.action_agendaFragment_to_detailCallFragment)
    }

}