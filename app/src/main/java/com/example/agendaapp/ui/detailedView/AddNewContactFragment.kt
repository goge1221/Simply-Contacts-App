package com.example.agendaapp.ui.detailedView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaapp.databinding.FragmentAddNewContactBinding


class AddNewContactFragment : Fragment() {

    private lateinit var binding: FragmentAddNewContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}