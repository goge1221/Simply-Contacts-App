package com.example.agendaapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaapp.databinding.FragmentDetailCallBinding
import com.example.agendaapp.objects.Contact


class DetailCallFragment: Fragment() {

    private var binding: FragmentDetailCallBinding? = null

    private var contact : Contact =

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentInflater =  FragmentDetailCallBinding.inflate(inflater, container, false)
        binding = fragmentInflater
        return fragmentInflater.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragmentView = this@DetailCallFragment
        }
    }

    fun initiateCall(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phoneNumber))
        startActivity(intent)
    }

}