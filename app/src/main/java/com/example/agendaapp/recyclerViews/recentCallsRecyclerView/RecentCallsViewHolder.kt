package com.example.agendaapp.recyclerViews.recentCallsRecyclerView

import android.provider.CallLog
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.R
import com.example.agendaapp.databinding.SingleRecentCallItemBinding
import com.example.agendaapp.entity.RecentCall
import java.text.SimpleDateFormat
import java.util.*

class RecentCallsViewHolder(
    private val binding: SingleRecentCallItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recentCall: RecentCall) {
        binding.callerName.text = recentCall.name
        determineCallType(recentCall.type)
        setTime(recentCall.duration)
        setDate(recentCall.date)
    }

    private fun setTime(timeInMinutes: Int) {
        val callDuration: String = binding.root.resources.getString(
            R.string.call_time, timeInMinutes.toString()
        )
        binding.duration.text = callDuration
    }

    private fun setDate(receivedDate: Date) {
        val simpleDateFormat = SimpleDateFormat.getDateInstance()
        val date: String = simpleDateFormat.format(receivedDate)
        binding.date.text = date
    }


    private fun determineCallType(callType: Int) {
        when (callType) {
            CallLog.Calls.OUTGOING_TYPE -> setIconWithDrawable(R.drawable.baseline_call_made_24)
            CallLog.Calls.INCOMING_TYPE -> setIconWithDrawable(R.drawable.baseline_call_received_24)
            CallLog.Calls.MISSED_TYPE -> setIconWithDrawable(R.drawable.baseline_call_missed_24)
            CallLog.Calls.REJECTED_TYPE -> setIconWithDrawable(R.drawable.baseline_call_missed_outgoing_24)
        }
    }

    private fun setIconWithDrawable(drawableInt: Int) {
        binding.callTypeImageView.foreground = binding.root.resources.getDrawable(drawableInt)
    }


}