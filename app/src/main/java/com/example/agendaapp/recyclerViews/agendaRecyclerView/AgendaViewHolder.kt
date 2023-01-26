package com.example.agendaapp.recyclerViews.agendaRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleAgendaItemBinding
import com.example.agendaapp.entity.Contact

class AgendaViewHolder(
    private val binding: SingleAgendaItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact){
        binding.callerName.text = contact.name
    }
}