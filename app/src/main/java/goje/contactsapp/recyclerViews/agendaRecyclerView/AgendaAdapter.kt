package goje.contactsapp.recyclerViews.agendaRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleAgendaItemBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.entity.ContactElement
import goje.contactsapp.entity.StartingCharacter
import goje.contactsapp.ui.agenda.AgendaObserver

class AgendaAdapter(
    private var contactsList: List<ContactElement>,
    private val clickListener: OnContactClickedListener
) : RecyclerView.Adapter<AgendaViewHolder>(), AgendaObserver {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        return AgendaViewHolder(
            SingleAgendaItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        if (contactsList[position] is Contact) {
            holder.bind(contactsList[position] as Contact, clickListener)
            return
        }
        holder.bindCharacter(contactsList[position] as StartingCharacter)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun updateContactsList(updatedList: List<ContactElement>) {
        this.contactsList = updatedList
        notifyDataSetChanged()
    }

}