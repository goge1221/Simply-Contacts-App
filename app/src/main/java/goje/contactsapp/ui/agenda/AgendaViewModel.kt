package goje.contactsapp.ui.agenda

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import goje.contactsapp.entity.Contact
import goje.contactsapp.entity.ContactElement
import goje.contactsapp.entity.ContactPreferences
import goje.contactsapp.entity.StartingCharacter

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private var _contactsList = MutableLiveData<List<ContactElement>>()
    val contactsList: LiveData<List<ContactElement>> = _contactsList

    init {
        getContacts()
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

                    val displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

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
                                phoneNumber, displayName, contactId
                            )
                        )
                    }
                    phoneCursor.close()
                }
            }
        }
        cursor.close()

        _contactsList.value = contactsInfoList
        ContactPreferences.updateContactsList(getApplication<Application?>().applicationContext, contactsInfoList)
        Log.i("CONTACTS_CONTACT",
            ContactPreferences.retrieveMostContactedPersons(getApplication<Application?>().applicationContext)
                .toString()
        )
    }

    @SuppressLint("Range")
    fun deleteContact(context: Context, phoneNumber: String, contactName: String): Boolean {
        val contactUri: Uri =
            Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
            )
        val cur: Cursor = context.contentResolver.query(contactUri, null, null, null, null)!!
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                            .equals(contactName, ignoreCase = true)
                    ) {
                        val lookupKey =
                            cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                        val uri: Uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lookupKey
                        )
                        context.contentResolver.delete(uri, null, null)
                        return true
                    }
                } while (cur.moveToNext())
            }
        } catch (e: Exception) {
            println(e.stackTrace)
        } finally {
            cur.close()
        }
        return false
    }

    fun getContactById(contactId: String): Contact {
        getContacts()
        for (contact in contactsList.value!!)
            if (contact is Contact)
                if (contact.contactId == contactId)
                    return contact
        return Contact("", "", "")
    }

    fun retrieveContacts() {
        getContacts()
        addStartingLetters()
    }

    private fun addStartingLetters() {
        val modifiedList = mutableListOf<ContactElement>()
        var currentStartingLetter: Char? = null

        _contactsList.value?.let { contactsList ->
            for (contact in contactsList) {
                if (contact is Contact) {
                    val firstChar = contact.name.first()

                    if (firstChar.isLetter()) {
                        if (firstChar != currentStartingLetter) {
                            currentStartingLetter = firstChar
                            modifiedList.add(StartingCharacter("999", firstChar))
                        }
                    } else {
                        if (currentStartingLetter != '#') {
                            currentStartingLetter = '#'
                            modifiedList.add(StartingCharacter("999", '#'))
                        }
                    }
                    modifiedList.add(contact)
                }
            }
        }

        _contactsList.value = modifiedList
    }

    fun addStartingLettersWithReceivedList(contactsList: ArrayList<ContactElement>): List<ContactElement> {
        val modifiedList = mutableListOf<ContactElement>()
        var currentStartingLetter: Char? = null

        for (contact in contactsList) {
            if (contact is Contact) {
                val firstChar = contact.name.first()

                if (firstChar.isLetter()) {
                    if (firstChar != currentStartingLetter) {
                        currentStartingLetter = firstChar
                        modifiedList.add(StartingCharacter("999", firstChar))
                    }
                } else {
                    if (currentStartingLetter != '#') {
                        currentStartingLetter = '#'
                        modifiedList.add(StartingCharacter("999", '#'))
                    }
                }
                modifiedList.add(contact)
            }
        }

        return modifiedList
    }


}