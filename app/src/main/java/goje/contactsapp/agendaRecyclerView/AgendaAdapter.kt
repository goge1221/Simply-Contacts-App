package goje.contactsapp.agendaRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleAgendaItemBinding
import goje.contactsapp.databinding.StartingCharacterViewHolderBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.entity.ContactElement
import goje.contactsapp.entity.ContactPreferences
import goje.contactsapp.entity.StartingCharacter
import goje.contactsapp.ui.agenda.AgendaObserver

class AgendaAdapter(
    private var contactsList: List<ContactElement>,
    private val clickListener: OnContactClickedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AgendaObserver {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            return ContactViewHolder(
                SingleAgendaItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
        return StartingCharacterViewHolder(
            StartingCharacterViewHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemViewType(position: Int): Int {
        // Return the appropriate view type based on the position or data
        return if (contactsList[position] is Contact) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ContactViewHolder -> {
                // Bind data for ViewHolderTypeOne
                holder.bind(contactsList[position] as Contact, clickListener)
            }

            is StartingCharacterViewHolder -> {
                // Bind data for ViewHolderTypeTwo
                holder.bindCharacter(contactsList[position] as StartingCharacter)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun updateContactsList(context : Context, updatedList: List<ContactElement>) {

        val mostContactedPersons : ArrayList<Contact> = ContactPreferences.retrieveMostContactedPersons(context)
        val contactElements: ArrayList<ContactElement> = ArrayList(mostContactedPersons.toList())
        contactElements.add(0, StartingCharacter("11", "Favorites"))
        this.contactsList = contactElements + updatedList
        notifyDataSetChanged()
    }

}