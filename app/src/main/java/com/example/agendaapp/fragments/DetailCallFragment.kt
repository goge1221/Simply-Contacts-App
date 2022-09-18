package com.example.agendaapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.agendaapp.databinding.FragmentDetailCallBinding
import com.example.agendaapp.objects.Contact


class DetailCallFragment : Fragment() {

    private var binding: FragmentDetailCallBinding? = null

    private val sharedViewModel: AgendaViewModel by activityViewModels()

    var contact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentInflater = FragmentDetailCallBinding.inflate(inflater, container, false)
        binding = fragmentInflater
        return fragmentInflater.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragmentView = this@DetailCallFragment
        }
        contact = sharedViewModel.singleContact.value
        binding?.personToCall?.text = contact?.name
    }

    fun initiateCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact?.phoneNumber))
        startActivity(intent)
    }

}