package goje.contactsapp.ui.agenda

import android.content.Context
import goje.contactsapp.entity.ContactElement

/**
 * An interface for observing changes to the contacts list in the Agenda.
 *
 * Implementing classes can use this interface to observe and update the contacts list
 * when changes occur, such as when a contact is added
 */
interface AgendaObserver {
    /**
     * This method is called when the entire contacts list needs to be updated
     *
     * @param context The application context.
     * @param updatedList The updated list of contact elements.
     */
    fun updateContactsList(context: Context, updatedList: List<ContactElement>)

    /**
     * Updates the contacts list with a filtered list of contact elements.
     *
     * This method is called when a filter is applied to the contacts list,
     * and it should update the UI with the filtered results.
     * The main difference is that "Favorites" are not displayed when a filter is applied.
     *
     * @param updatedList The filtered list of contact elements.
     */
    fun updateContactsListFromFilter(updatedList: List<ContactElement>)
}
