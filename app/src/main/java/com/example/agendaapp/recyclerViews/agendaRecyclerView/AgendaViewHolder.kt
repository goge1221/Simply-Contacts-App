package com.example.agendaapp.recyclerViews.agendaRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleAgendaItemBinding
import com.example.agendaapp.entity.Contact
import com.example.agendaapp.ui.agenda.OnContactClickedListener

class AgendaViewHolder(
    private val binding: SingleAgendaItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact, clickListener: OnContactClickedListener) {
        binding.callerName.text = contact.name

        binding.root.setOnClickListener {
            clickListener.selectContact(contact)
        }
    }
}