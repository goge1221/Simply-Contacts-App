package com.example.agendaapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.agendaapp.databinding.FragmentAgendaBinding


class AgendaFragment : Fragment() {

    private lateinit var binding: FragmentAgendaBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
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
        checkPermissionToReadContacts()
        Log.e("CONTATS", getContacts().toString())
    }

    private fun checkPermissionToReadContacts() {
        if (!userHasPermission())
            requestPermission()
    }

    private fun userHasPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            binding.root.context,
            Manifest.permission.READ_CONTACTS
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            Manifest.permission.READ_CONTACTS
        )
    }

    @SuppressLint("Range")
    private fun getContacts(): ArrayList<Contact> {
        val contentResolver: ContentResolver = binding.root.context.contentResolver
        var contactId: String?
        val contactsInfoList = ArrayList<Contact>()
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val hasPhoneNumber: Int =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt()
                if (hasPhoneNumber > 0) {
                    contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val phoneCursor: Cursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String?>(contactId),
                        null
                    )!!
                    if (phoneCursor.moveToNext()) {
                        val phoneNumber: String =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactsInfoList.add(
                            Contact(
                                phoneNumber,
                                contactId
                            )
                        )
                    }
                    phoneCursor.close()
                }
            }
        }
        cursor.close()

        return contactsInfoList
    }
}