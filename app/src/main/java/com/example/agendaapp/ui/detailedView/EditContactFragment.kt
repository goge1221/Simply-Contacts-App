package com.example.agendaapp.ui.detailedView

import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            updateContact()
        }
    }

    private fun updateContact() {
        val id = contact.contactId
        val number = "000 000 000"
        val ops = ArrayList<ContentProviderOperation>()

        // Name
        var builder: ContentProviderOperation.Builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
        builder.withSelection(
            ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?",
            arrayOf(
                id,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
        )
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, binding.callerName.text.toString())
        ops.add(builder.build())

        // Number
        builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
        builder.withSelection(
            ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?",
            arrayOf(
                id,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME.toString()
            )
        )
        builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
        ops.add(builder.build())


        // Update
        try {
            requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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