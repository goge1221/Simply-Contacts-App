package com.example.agendaapp.ui.detailedView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaapp.R
import com.example.agendaapp.databinding.FragmentAddNewContactBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddNewContactFragment : Fragment() {

    private lateinit var binding: FragmentAddNewContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewContactBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNewContactClickListener()
    }

    private fun addNewContactClickListener() {
        binding.updateContactButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            showNavAndToolBar()
        }
    }

    private fun showNavAndToolBar(){
        val toolBar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.VISIBLE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.VISIBLE
    }

}