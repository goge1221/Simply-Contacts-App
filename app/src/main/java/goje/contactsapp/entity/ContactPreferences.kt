package goje.contactsapp.entity

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import goje.contactsapp.utils.Constants
import java.lang.reflect.Type


class ContactPreferences {

    fun updateContactsList(context: Context, contactLists: ArrayList<Contact>) {

        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val updatedContactsList = checkIfContactsListChanged(
            contactLists,
            retrieveAllContactsFromSharedPreferences(context)
        )

        val preferencesEditor = sharedPreferences.edit()

        var string = Gson().toJson(updatedContactsList)

        if (Constants.FIRST_TIME_SAVING_CONTACTS_TO_SHARED_PREFERENCES) {
            string = Gson().toJson(getInitialPairs(contactLists))
            Constants.FIRST_TIME_SAVING_CONTACTS_TO_SHARED_PREFERENCES = false
        }
        preferencesEditor.putString("contacts", string)

        preferencesEditor.apply()
    }


    fun retrieveMostContactedPersons(context: Context): ArrayList<Pair<Contact, Int>> {
        return retrieveAllContactsFromSharedPreferences(context)
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
                    updatedPreferencesList.add(Pair(contact, contactInPreference.second))
                    break
                } else {
                    //Initialize with 0
                    updatedPreferencesList.add(Pair(contact, 0))
                    break
                }
            }
        }
        return updatedPreferencesList
    }

    private fun retrieveAllContactsFromSharedPreferences(context: Context): ArrayList<Pair<Contact, Int>> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val contactsString = sharedPreferences.getString("contacts", "")

        val type: Type = object : TypeToken<ArrayList<Pair<Contact, Int>?>?>() {}.type

        return Gson().fromJson(contactsString, type) ?: ArrayList()
    }


}