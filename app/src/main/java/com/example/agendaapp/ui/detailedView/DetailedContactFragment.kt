package com.example.agendaapp.ui.detailedView

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaapp.R
import com.example.agendaapp.databinding.FragmentDetailedContactBinding
import com.example.agendaapp.entity.Contact
import com.example.agendaapp.utils.Constants
import com.example.agendaapp.utils.PermissionChecker


class DetailedContactFragment(private val contact: Contact) : Fragment() {

    private var _binding: FragmentDetailedContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedContactBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButtonListeners()
        initializeViewWithInformation()
    }

    private fun initializeViewWithInformation(){
        binding.callerName.text = contact.name
        binding.callerNumber.text = contact.phoneNumber
    }


    private fun addButtonListeners(){
        addEditButtonListener()
        addCallButtonClickListener()
    }


    private fun addCallButtonClickListener(){
        binding.callButton.setOnClickListener {
            if (PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.CALL_PHONE)){
                //open intent with edit
                initiateCall()
            }
            else{
                requestPermissionToCall()
            }
        }
    }

    private fun addEditButtonListener(){
        binding.settingsButton.setOnClickListener{
            if (PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.WRITE_CONTACTS)){
                //open intent with edit
                openModifyContactFragment()
            }
            else{
                requestPermissionToWriteContacts()
            }
        }
    }

    private fun initiateCall(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phoneNumber))
        startActivity(intent)
    }

    private fun openModifyContactFragment(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main2, EditContactFragment(contact), "EDIT_CONTACT_FRAGMENT")
            .addToBackStack(tag)
            .commit()
    }

    private fun requestPermissionToWriteContacts() {
        if (!PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.WRITE_CONTACTS)) {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_CONTACTS),
                Constants.PERMISSION_TO_WRITE_CONTACTS
            )
        }
    }


    private fun requestPermissionToCall(){
        if (!PermissionChecker.userHasSpecifiedPermission(context, android.Manifest.permission.CALL_PHONE)){
            requestPermissions(
                arrayOf(android.Manifest.permission.CALL_PHONE),
                Constants.PERMISSION_TO_CALL
            )
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.PERMISSION_TO_WRITE_CONTACTS && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Permission to write contacts was granted
                    openModifyContactFragment()
                    break
                }
            }
        }
        if (requestCode == Constants.PERMISSION_TO_CALL && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Permission to write contacts was granted
                    initiateCall()
                    break
                }
            }
        }
       // Toast.makeText(context, "Please grant the permission in order to use this functionality", Toast.LENGTH_LONG).show()
    }

}