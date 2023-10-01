package goje.contactsapp.entity

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import goje.contactsapp.utils.Constants
import java.lang.reflect.Type

object ContactPreferences {

    /**
     * Increases the call count of a specific contact identified by [contactId].
     *
     * @param context The application context.
     * @param contactId The unique identifier of the contact.
     */
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

    /**
     * Updates the list of contacts in SharedPreferences.
     *
     * @param context The application context.
     * @param contactLists The updated list of contacts.
     */
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

    /**
     * Retrieves the list of most contacted persons.
     *
     * @param context The application context.
     * @return The list of most contacted persons.
     */
    fun retrieveMostContactedPersons(context: Context): ArrayList<Contact> {

        val contactsList = retrieveAllContactsFromSharedPreferences(context)

        val contactsListWithoutZeroCalls = contactsList.filter { it.second != 0 }.sortedByDescending { it.second }
        val topContacts = contactsListWithoutZeroCalls.take(5.coerceAtMost(contactsListWithoutZeroCalls.size))

        return topContacts.map { it.first } as ArrayList<Contact>
    }

    /**
     * Initializes a list of contact pairs with zero counts.
     *
     * @param contactsList The list of contacts to initialize.
     * @return The list of initialized contact pairs.
     */
    private fun getInitialPairs(contactsList: ArrayList<Contact>): ArrayList<Pair<Contact, Int>> {

        val initializedContactLists = ArrayList<Pair<Contact, Int>>()

        for (contact in contactsList)
            initializedContactLists.add(Pair(contact, 0))

        return initializedContactLists
    }

    /**
     * Checks if the contacts list has changed and updates it.
     *
     * @param contactsList The updated list of contacts.
     * @param contactsFromPreferences The list of contacts stored in SharedPreferences.
     * @return The updated list of contacts.
     */
    private fun checkIfContactsListChanged(
        contactsList: ArrayList<Contact>,
        contactsFromPreferences: ArrayList<Pair<Contact, Int>>
    ): ArrayList<Pair<Contact, Int>> {

        val updatedPreferencesList = ArrayList<Pair<Contact, Int>>()

        for (contact in contactsList) {

            for (contactInPreference in contactsFromPreferences) {
                if (contact == contactInPreference.first) {
                    // Add the value from contact in preference
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

    /**
     * Retrieves all contacts from SharedPreferences.
     *
     * @param context The application context.
     * @return The list of contacts stored in SharedPreferences.
     */
    private fun retrieveAllContactsFromSharedPreferences(context: Context): ArrayList<Pair<Contact, Int>> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        val contactsString = sharedPreferences.getString(Constants.CONTACTS, "")

        val type: Type = object : TypeToken<ArrayList<Pair<Contact, Int>?>?>() {}.type

        return Gson().fromJson(contactsString, type) ?: ArrayList()
    }
}
