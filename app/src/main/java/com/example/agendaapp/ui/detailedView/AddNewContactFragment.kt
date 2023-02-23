package com.example.agendaapp.ui.detailedView

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.Contacts.People
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaapp.R
import com.example.agendaapp.databinding.FragmentAddNewContactBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddNewContactFragment : Fragment() {

    private lateinit var binding: FragmentAddNewContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNewContactClickListener()
    }

    private fun addNewContactClickListener() {
        binding.updateContactButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            showNavAndToolBar()
            insertContact()
        }
    }

    private fun insertContact() {
        val values = ContentValues()
        values.put(CommonDataKinds.Phone.NUMBER, binding.callerNumber.text.toString())
        values.put(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_CUSTOM)
        values.put(CommonDataKinds.Phone.DISPLAY_NAME, binding.callerName.text.toString())
        val dataUri: Uri? = requireContext().contentResolver.insert(People.CONTENT_URI, values)
        val updateUri = Uri.withAppendedPath(dataUri, People.Phones.CONTENT_DIRECTORY)
        values.clear()
        ContactsContract.RawContacts.ACCOUNT_TYPE
        values.put(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
        values.put(CommonDataKinds.Phone.NUMBER, binding.callerNumber.text.toString())
        requireContext().contentResolver.insert(updateUri, values)
    }


    private fun showNavAndToolBar() {
        val toolBar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.VISIBLE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.VISIBLE
    }

}