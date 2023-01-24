package com.example.agendaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.agendaapp.data.ContactsListViewModel
import com.example.agendaapp.databinding.FragmentBlankBinding


class BlankFragment : Fragment() {

    private lateinit var binding : FragmentBlankBinding
    private val sharedViewModel: ContactsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlankBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, sharedViewModel.contactsList.value.toString(), Toast.LENGTH_SHORT).show()
    }


}