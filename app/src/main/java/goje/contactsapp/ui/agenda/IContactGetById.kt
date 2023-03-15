package goje.contactsapp.ui.agenda

import goje.contactsapp.entity.Contact

interface IContactGetById {
    fun getContactById(contactId: String) : Contact
}