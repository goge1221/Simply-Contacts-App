package goje.contactsapp.recyclerViews.agendaRecyclerView

import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.StartingCharacterViewHolderBinding
import goje.contactsapp.entity.StartingCharacter

class StartingCharacterViewHolder(
    private val binding: StartingCharacterViewHolderBinding
) : RecyclerView.ViewHolder(binding.root), ContactElementViewHolder {

    fun bindCharacter(startingCharacter: StartingCharacter){
        binding.startingCharacter.text = startingCharacter.character.toString()
    }
}