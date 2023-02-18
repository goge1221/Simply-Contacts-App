package com.example.agendaapp.ui.agenda

import com.example.agendaapp.entity.Contact

interface AgendaObserver {
    fun updateContactsList(updatedList : List<Contact>)
}