package com.example.agendaapp.ui.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agendaapp.databinding.FragmentAgendaBinding
import com.example.agendaapp.recyclerViews.agendaRecyclerView.AgendaAdapter

class AgendaFragment : Fragment() {

    private var _binding: FragmentAgendaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var agendaViewModel: AgendaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView(){
        binding.agendaRecyclerView.adapter = AgendaAdapter(agendaViewModel.contactsList.value!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}