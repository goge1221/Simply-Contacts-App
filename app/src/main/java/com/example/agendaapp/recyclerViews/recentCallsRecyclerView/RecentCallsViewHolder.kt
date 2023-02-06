package com.example.agendaapp.recyclerViews.recentCallsRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.databinding.SingleRecentCallItemBinding
import com.example.agendaapp.entity.RecentCall

class RecentCallsViewHolder(
    private val binding: SingleRecentCallItemBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(recentCall: RecentCall) {
        binding.callerName.text = recentCall.name
    }

}