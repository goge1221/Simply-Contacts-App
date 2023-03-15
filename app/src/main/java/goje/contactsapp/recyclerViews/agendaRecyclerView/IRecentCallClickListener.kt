package goje.contactsapp.recyclerViews.agendaRecyclerView

import goje.contactsapp.entity.RecentCall

interface IRecentCallClickListener {
    fun openRecentCall(call: RecentCall)
}