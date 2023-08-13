package goje.contactsapp.agendaRecyclerView

import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleAgendaItemBinding
import goje.contactsapp.entity.Contact

class ContactViewHolder(
    private val binding: SingleAgendaItemBinding
) : RecyclerView.ViewHolder(binding.root), ContactElementViewHolder {

    fun bind(
        contact: Contact,
        clickListener: OnContactClickedListener,
    ) {
        binding.callerName.text = contact.name

        binding.root.setOnClickListener {
            clickListener.selectContact(contact)
        }

    }
}