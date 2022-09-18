package com.example.agendaapp.contactRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleContactItemBinding
import com.example.agendaapp.interfaces.OnContactClickedListener
import com.example.agendaapp.objects.Contact

class ContactsAdapter(
    private val contactsList: ArrayList<Contact>,
    private val listener: OnContactClickedListener
): RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            SingleContactItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contactsList[position], listener)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}