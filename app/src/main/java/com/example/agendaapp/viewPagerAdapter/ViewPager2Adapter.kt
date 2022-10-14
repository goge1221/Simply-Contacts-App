package com.example.agendaapp.viewPagerAdapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.agendaapp.fragments.contacts.AgendaFragment
import com.example.agendaapp.fragments.recentCalls.RecentCallsFragment

class ViewPager2Adapter(
    activity: AppCompatActivity,
    private val itemsCount : Int
)  : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->  AgendaFragment()
            1 ->  RecentCallsFragment()
            else -> AgendaFragment()
        }
    }
}