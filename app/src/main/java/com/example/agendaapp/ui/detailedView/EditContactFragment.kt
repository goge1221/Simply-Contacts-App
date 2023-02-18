package com.example.agendaapp.ui.detailedView

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.agendaapp.databinding.FragmentEditContactBinding
import com.example.agendaapp.entity.Contact


class EditContactFragment(private val contact: Contact) : Fragment() {

    private var _binding: FragmentEditContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditContactBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewWithInformation()
        addOnUpdateClickListener()
    }


    private fun initializeViewWithInformation() {
        binding.callerName.setText(contact.name, TextView.BufferType.EDITABLE)
        binding.callerNumber.setText(contact.phoneNumber, TextView.BufferType.EDITABLE)
    }

    private fun addOnUpdateClickListener() {
        binding.updateContactButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            Toast.makeText(context, updateContactName().toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateContactName(): Boolean {
        val rawContactIds = getRawContactIdsForContact(contact.contactId.toLong())

        var rowsUpdated = 0
        for (rawContactId in rawContactIds) {
            val updateValues = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, binding.callerName.text.toString())
            }

            // Update the contact name
            val nameUpdateUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, rawContactId)
            rowsUpdated += requireContext().contentResolver.update(
                nameUpdateUri,
                updateValues,
                "${ContactsContract.Data.MIMETYPE} = ?",
                arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            )
        }

        // Return true if at least one row was updated
        return rowsUpdated > 0
    }


    private fun updateContact(): Boolean {

        val name = binding.callerName.text.toString()
        val phoneNumber = binding.callerNumber.text.toString()

        val rawContactIds = getRawContactIdsForContact(contact.contactId.toLong())

        var rowsUpdated = 0
        for (rawContactId in rawContactIds) {
            val updateValues = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            }

            // Update the contact name
            val nameUpdateUri =
                ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, rawContactId)
            rowsUpdated += requireContext().contentResolver.update(
                nameUpdateUri,
                updateValues,
                null,
                null
            )

            // Update the phone number
            val phoneUpdateValues = ContentValues().apply {
                put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
            }

            val phoneUpdateUri =
                ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, rawContactId)
            rowsUpdated += requireContext().contentResolver.update(
                phoneUpdateUri,
                phoneUpdateValues,
                "${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Phone.TYPE} = ?",
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
                )
            )
        }

        // Return true if at least one row was updated
        return rowsUpdated > 0
    }

    @SuppressLint("Range")
    private fun getRawContactIdsForContact(contactId: Long): List<Long> {
        val rawContactIds = mutableListOf<Long>()

        val uri = ContactsContract.RawContacts.CONTENT_URI
        val projection = arrayOf(ContactsContract.RawContacts._ID)
        val selection = "${ContactsContract.RawContacts.CONTACT_ID} = ?"
        val selectionArgs = arrayOf(contactId.toString())

        val cursor =
            requireContext().contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            while (it.moveToNext()) {
                val rawContactId = it.getLong(it.getColumnIndex(ContactsContract.RawContacts._ID))
                rawContactIds.add(rawContactId)
            }
        }

        return rawContactIds
    }


}