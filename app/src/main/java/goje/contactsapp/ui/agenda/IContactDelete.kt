package goje.contactsapp.ui.agenda

import goje.contactsapp.entity.Contact

interface IContactDelete {
    fun deleteContact(contact: Contact)
}