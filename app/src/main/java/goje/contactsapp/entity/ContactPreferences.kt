package goje.contactsapp.entity

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import goje.contactsapp.utils.Constants
import java.lang.reflect.Type


object ContactPreferences {

    fun increaseCallNumberOfContact(context: Context, contactId: String) {
        val contactsList = retrieveAllContactsFromSharedPreferences(context)

        // Find the index of the pair with the matching contactId
        val indexToUpdate = contactsList.indexOfFirst { it.first.contactId == contactId }

        // If the contactId is found in the list, increase the Int value by 1
        if (indexToUpdate != -1) {
            val (contact, count) = contactsList[indexToUpdate]
            val updatedPair = Pair(contact, count + 1)

            contactsList[indexToUpdate] = updatedPair

            val sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
            val contactsJson = Gson().toJson(contactsList)

            sharedPreferences.edit().putString(Constants.CONTACTS, contactsJson).apply()

        }
    }

    fun updateContactsList(context: Context, contactLists: ArrayList<Contact>) {

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val updatedContactsList = checkIfContactsListChanged(
            contactLists,
            retrieveAllContactsFromSharedPreferences(context)
        )

        val preferencesEditor = sharedPreferences.edit()

        var string = Gson().toJson(updatedContactsList)

        if (sharedPreferences.getBoolean("OPEN_APP_FIRST_TIME", true)) {
            string = Gson().toJson(getInitialPairs(contactLists))
            sharedPreferences.edit().putBoolean("OPEN_APP_FIRST_TIME", false).apply()
        }
        preferencesEditor.putString(Constants.CONTACTS, string)

        preferencesEditor.apply()
    }


    fun retrieveMostContactedPersons(context: Context): ArrayList<Contact> {

        val contactsList = retrieveAllContactsFromSharedPreferences(context)

        val contactsListWithoutZeroCalls = contactsList.filter { it.second != 0 }.sortedByDescending { it.second }
        val topContacts = contactsListWithoutZeroCalls.take(5.coerceAtMost(contactsListWithoutZeroCalls.size))

        return topContacts.map { it.first } as ArrayList<Contact>
    }


    private fun getInitialPairs(contactsList: ArrayList<Contact>): ArrayList<Pair<Contact, Int>> {

        val initializedContactLists = ArrayList<Pair<Contact, Int>>()

        for (contact in contactsList)
            initializedContactLists.add(Pair(contact, 0))

        return initializedContactLists
    }

    private fun checkIfContactsListChanged(
        contactsList: ArrayList<Contact>,
        contactsFromPreferences: ArrayList<Pair<Contact, Int>>
    ): ArrayList<Pair<Contact, Int>> {

        val updatedPreferencesList = ArrayList<Pair<Contact, Int>>()

        for (contact in contactsList) {

            for (contactInPreference in contactsFromPreferences) {
                if (contact == contactInPreference.first) {
                    //Add the value from contact in prefference
                    updatedPreferencesList.add(contactInPreference)
                    break
                }
            }
        }

        for (contact in contactsList) {
            val found = updatedPreferencesList.any { it.first == contact }
            if (!found) {
                updatedPreferencesList.add(Pair(contact, 0))
            }
        }

        return updatedPreferencesList
    }

    private fun retrieveAllContactsFromSharedPreferences(context: Context): ArrayList<Pair<Contact, Int>> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val contactsString = sharedPreferences.getString(Constants.CONTACTS, "")

        val type: Type = object : TypeToken<ArrayList<Pair<Contact, Int>?>?>() {}.type

        return Gson().fromJson(contactsString, type) ?: ArrayList()
    }


}