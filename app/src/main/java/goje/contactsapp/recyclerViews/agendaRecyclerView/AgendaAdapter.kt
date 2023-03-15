package goje.contactsapp.recyclerViews.agendaRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleAgendaItemBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.ui.agenda.AgendaObserver
import goje.contactsapp.ui.agenda.OnContactClickedListener

class AgendaAdapter(
    private var contactsList: List<Contact>,
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
        holder.bind(contactsList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun updateContactsList(updatedList: List<Contact>) {
        this.contactsList = updatedList
        notifyDataSetChanged()
    }

}