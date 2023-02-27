package com.example.agendaapp.ui.agenda

import com.example.agendaapp.entity.Contact

interface IContactGetById {
    fun getContactById(contactId: String) : Contact
}