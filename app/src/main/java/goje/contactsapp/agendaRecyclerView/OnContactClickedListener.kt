package goje.contactsapp.agendaRecyclerView

import goje.contactsapp.entity.Contact

interface OnContactClickedListener {
    fun selectContact(contact: Contact)
}