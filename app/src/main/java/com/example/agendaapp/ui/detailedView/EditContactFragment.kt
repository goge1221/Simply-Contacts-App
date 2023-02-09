package com.example.agendaapp.ui.detailedView

import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
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


    private fun initializeViewWithInformation(){
        binding.callerName.setText(contact.name, TextView.BufferType.EDITABLE)
        binding.callerNumber.setText(contact.phoneNumber, TextView.BufferType.EDITABLE)
    }

    private fun addOnUpdateClickListener(){
        binding.updateContactButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
            updateNameAndNumber()
        }
    }

    private val DATA_COLS = arrayOf(
        ContactsContract.Data.MIMETYPE,
        ContactsContract.Data.DATA1,  //phone number
        ContactsContract.Data.CONTACT_ID
    )


    private fun updateNameAndNumber(): Boolean {
        val newNumber = binding.callerNumber.text.toString()
        val contactId = getContactId(contact.phoneNumber)

        //selection for name
        var where = String.format(
            "%s = '%s' AND %s = ?",
            DATA_COLS[0],  //mimetype
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
            DATA_COLS[2] /*contactId*/
        )
        val args = arrayOf(contactId)
        val operations: ArrayList<ContentProviderOperation> = ArrayList()
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, args)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, binding.callerName.text.toString())
                .build()
        )

        //change selection for number
        where = String.format(
            "%s = '%s' AND %s = ?",
            DATA_COLS[0],  //mimetype
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            DATA_COLS[1] /*number*/
        )

        //change args for number
        args[0] = contact.phoneNumber
        operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(where, args)
                .withValue(DATA_COLS[1], newNumber)
                .build()
        )
        try {
            val results: Array<ContentProviderResult> =
                context?.contentResolver!!.applyBatch(ContactsContract.AUTHORITY, operations)
            for (result in results) {
                Log.d("Update Result", result.toString())
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //TODO FIX WHAT HAPPENS IF YOU CLICK

    @SuppressLint("Range")
    private fun getContactId(number: String): String {
        val cursor: Cursor? = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
            arrayOf(number),
            null
        )
        if (cursor == null || cursor.count == 0) return ""
        cursor.moveToFirst()
        val id: String =
            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
        cursor.close()
        return id
    }

}