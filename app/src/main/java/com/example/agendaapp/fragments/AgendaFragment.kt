package com.example.agendaapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.agendaapp.R
import com.example.agendaapp.contactRecyclerView.ContactsAdapter
import com.example.agendaapp.databinding.FragmentAgendaBinding
import com.example.agendaapp.interfaces.OnContactClickedListener
import com.example.agendaapp.objects.Contact
import com.example.agendaapp.utils.PermissionHandler


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
        initializeRecyclerView()
        PermissionHandler(requestPermissionLauncher, binding?.root?.context!!).askPermissions()
    }

    private fun initializeRecyclerView() {
        binding?.apply {
            recyclerView.adapter =
                ContactsAdapter(
                    sharedViewModel.contactsList.value as ArrayList<Contact>,
                    this@AgendaFragment
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun contactClicked(contact: Contact) {
        sharedViewModel.setSingleContact(contact)
        findNavController().navigate(R.id.action_agendaFragment_to_detailCallFragment)
    }

}