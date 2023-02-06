package com.example.agendaapp.recyclerViews.recentCallsRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleRecentCallItemBinding
import com.example.agendaapp.entity.RecentCall

class RecentCallsAdapter(
    private val recentCalls: List<RecentCall>
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
        holder.bind(recentCalls[position])
    }
}