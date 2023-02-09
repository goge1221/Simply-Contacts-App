package com.example.agendaapp.ui.agenda

import com.example.agendaapp.entity.Contact

interface OnContactClickedListener {
    fun selectContact(contact: Contact)
}