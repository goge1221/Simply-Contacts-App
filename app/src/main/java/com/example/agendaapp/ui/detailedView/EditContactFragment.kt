package com.example.agendaapp.ui.detailedView

import android.annotation.SuppressLint
import android.content.*
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
            myTest()
        }
    }

    private fun myTest(){
        val phoneId = getContactId(contact.name, contact.phoneNumber) // The ID of the phone number you want to update
        Toast.makeText(context, phoneId.toString(), Toast.LENGTH_SHORT).show()

        val newPhoneNumber =  "222"// The new phone number for the contact
        val name = "new name meeen" // The new name for the contact

        val values = ContentValues()
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber)

        val updateUri = ContentUris.withAppendedId(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            phoneId
        )
      //  val rows = context?.contentResolver?.update(updateUri, values, null, null)
    //    Toast.makeText(context, "Rows affected: " + rows, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    private fun getContactId(name: String, phoneNumber: String): Long {
        var ret: Long = -1
        val selection = (
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ? AND " +
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?"
                )
        val selectionArgs = arrayOf("%$name%", phoneNumber)
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val c = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, selection, selectionArgs, null
        )
        if (c!!.moveToFirst()) {
            ret = c.getLong(0)
        }
        c.close()

        return ret
    }

}