package com.example.agendaapp.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.os.Parcel
import android.provider.ContactsContract
import android.support.v4.os.IResultReceiver._Parcel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agendaapp.objects.Contact

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private var _contactsList = MutableLiveData<ArrayList<Contact>>()
    val contactsList: LiveData<ArrayList<Contact>> = _contactsList


    private var _singleContact = MutableLiveData<Contact>()
    val singleContact: LiveData<Contact> = _singleContact

    init {
        getContacts()
    }


    fun setSingleContact(contact: Contact){
        _singleContact.value = contact
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
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

                    val displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

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
                                displayName
                            )
                        )
                    }
                    phoneCursor.close()
                }
            }
        }
        cursor.close()

        _contactsList.value = contactsInfoList
    }
}