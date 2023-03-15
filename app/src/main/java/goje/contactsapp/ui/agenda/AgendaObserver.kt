package goje.contactsapp.ui.agenda

import goje.contactsapp.entity.Contact

interface AgendaObserver {
    fun updateContactsList(updatedList : List<Contact>)
}