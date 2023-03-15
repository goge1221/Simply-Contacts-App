package goje.contactsapp.ui.agenda

import goje.contactsapp.entity.Contact

interface OnContactClickedListener {
    fun selectContact(contact: Contact)
}