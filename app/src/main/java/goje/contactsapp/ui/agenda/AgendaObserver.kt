package goje.contactsapp.ui.agenda

import goje.contactsapp.entity.ContactElement

interface AgendaObserver {
    fun updateContactsList(updatedList: List<ContactElement>)
}