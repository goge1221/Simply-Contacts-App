package goje.contactsapp.ui.detailedView

import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import goje.contactsapp.databinding.FragmentEditContactBinding
import goje.contactsapp.entity.Contact


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
            updateNumber()
            updateName()
        }
    }

    private fun updateName() {

        val id = contact.contactId
        val ops = ArrayList<ContentProviderOperation>()

        // Name
        val builder: ContentProviderOperation.Builder =
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
        builder.withSelection(
            ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?",
            arrayOf(
                id,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
        )
        builder.withValue(
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            binding.callerName.text.toString().substringBefore(" ")
        )

        if (checkIfPersonHasSecondName()) {
            val secondName = binding.callerName.text.toString().substringAfter(" ")
            Log.e(
                "EEE", "First string: " + binding.callerName.text
                    .toString().substringBefore(" ")
                        + " Second: " + binding.callerName.text.toString()
                    .substringAfter(" ")
            )

            builder.withValue(
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                secondName
            )
        } else{
            builder.withValue(
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ""
            )
        }

        ops.add(builder.build())

        // Update
        try {
            requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkIfPersonHasSecondName(): Boolean {
        val fullName = binding.callerName.text.toString()
        return fullName.contains(" ")
    }

    private fun updateNumber() {

        val rawContactId = getRawContactIdsForContact(contact.contactId.toLong())[0].toString()

        val where = (ContactsContract.Data.RAW_CONTACT_ID + " = ? AND "
                + ContactsContract.Data.MIMETYPE + " = ?")

        val numberParams = arrayOf(
            rawContactId,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )

        val ops: ArrayList<ContentProviderOperation> = ArrayList()

        ops.add(
            ContentProviderOperation.newUpdate(
                ContactsContract.Data.CONTENT_URI
            )
                .withSelection(where, numberParams)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    binding.callerNumber.text.toString()
                )
                .build()
        )

        requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
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