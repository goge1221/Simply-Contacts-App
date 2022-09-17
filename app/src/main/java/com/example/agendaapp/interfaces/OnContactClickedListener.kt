package com.example.agendaapp.interfaces

import com.example.agendaapp.objects.Contact

interface OnContactClickedListener {
    fun contactClicked(contact: Contact)
}