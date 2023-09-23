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


        val oldContactList = retrieveAllContactsFromSharedPreferences(context)

        val updatedContactsList = checkIfContactsListChanged(
            contactLists,
            retrieveAllContactsFromSharedPreferences(context)
        )

        val preferencesEditor = sharedPreferences.edit()


        var string = Gson().toJson(updatedContactsList)

        if (Constants.FIRST_TIME_SAVING_CONTACTS_TO_SHARED_PREFERENCES){
            string = Gson().toJson(contactLists)
            Constants.FIRST_TIME_SAVING_CONTACTS_TO_SHARED_PREFERENCES = false
        }
        preferencesEditor.putString("contacts", string)

        preferencesEditor.apply()
    }


    fun retrieveMostContactedPersons(context: Context): ArrayList<Contact> {
        return retrieveAllContactsFromSharedPreferences(context)
    }


    private fun checkIfContactsListChanged(
        contactsList: ArrayList<Contact>,
        contactsFromPreferences: ArrayList<Contact>
    ): ArrayList<Contact> {

        //TODO change later to an array list of pairs
        val updatedPreferencesList = ArrayList<Contact>()

        for (contact in contactsList) {

            for (contactInPreference in contactsFromPreferences) {
                if (contact == contactInPreference) {
                    //Add the value from contact in prefference
                    updatedPreferencesList.add(contact)
                    break
                } else {
                    //Initialize with 0
                    updatedPreferencesList.add(contact)
                    break
                }
            }
        }
        return updatedPreferencesList
    }

    private fun retrieveAllContactsFromSharedPreferences(context: Context): ArrayList<Contact> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val contactsString = sharedPreferences.getString("contacts", "")

        val type: Type = object : TypeToken<ArrayList<Contact?>?>() {}.type

        return Gson().fromJson(contactsString, type) ?: ArrayList()
    }


}