package goje.contactsapp.recyclerViews.recentCallsRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import goje.contactsapp.databinding.SingleRecentCallItemBinding
import goje.contactsapp.entity.RecentCall
import goje.contactsapp.recyclerViews.agendaRecyclerView.IRecentCallClickListener

class RecentCallsAdapter(
    private val recentCalls: List<RecentCall>,
    private val recentCallClickListener: IRecentCallClickListener
) : RecyclerView.Adapter<RecentCallsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCallsViewHolder {
        return RecentCallsViewHolder(
            SingleRecentCallItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return recentCalls.size
    }

    override fun onBindViewHolder(holder: RecentCallsViewHolder, position: Int) {
        holder.bind(recentCalls[position], recentCallClickListener)
    }
}