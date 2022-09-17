package com.example.agendaapp.contactRecyclerView

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.R
import com.example.agendaapp.databinding.SingleContactItemBinding
import com.example.agendaapp.interfaces.OnContactClickedListener
import com.example.agendaapp.objects.Contact

class ContactsViewHolder(
    private val binding: SingleContactItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact, listener: OnContactClickedListener){
        binding.letterInsideCircle.text = contact.name.take(1)
        binding.callerName.text = contact.name

        binding.root.setOnClickListener {
            listener.contactClicked(contact)
        }
    }
}