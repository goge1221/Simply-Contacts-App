package com.example.agendaapp.recyclerViews.agendaRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleAgendaItemBinding
import com.example.agendaapp.entity.Contact
import com.example.agendaapp.ui.agenda.OnContactClickedListener

class AgendaAdapter(
    private var contactsList: List<Contact>,
    private val clickListener: OnContactClickedListener
) : RecyclerView.Adapter<AgendaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        return AgendaViewHolder(
            SingleAgendaItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        holder.bind(contactsList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

}