package com.example.agendaapp.ui.recentCalls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agendaapp.databinding.FragmentRecentCallsBinding
import com.example.agendaapp.recyclerViews.recentCallsRecyclerView.RecentCallsAdapter


class RecentCallsFragment : Fragment() {

    private var _binding: FragmentRecentCallsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var notificationsViewModel : RecentCallsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel = ViewModelProvider(this)[RecentCallsViewModel::class.java]
        _binding = FragmentRecentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }


    private fun initializeRecyclerView(){
        binding.agendaRecyclerView.adapter = notificationsViewModel.recentCallsList.value?.let {
            RecentCallsAdapter(
                it
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}