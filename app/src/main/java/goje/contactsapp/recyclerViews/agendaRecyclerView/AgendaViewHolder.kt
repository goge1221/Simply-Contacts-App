package goje.contactsapp.recyclerViews.agendaRecyclerView

import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleAgendaItemBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.entity.StartingCharacter

class AgendaViewHolder(
    private val binding: SingleAgendaItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        contact: Contact,
        clickListener: OnContactClickedListener,
    ) {
        binding.callerName.text = contact.name

        binding.root.setOnClickListener {
            clickListener.selectContact(contact)
        }

    }

    fun bindCharacter(startingCharacter: StartingCharacter){
        binding.callerName.text = startingCharacter.character.toString()
    }
}