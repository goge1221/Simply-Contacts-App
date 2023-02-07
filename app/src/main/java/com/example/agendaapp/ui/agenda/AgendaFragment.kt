package com.example.agendaapp.ui.agenda

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
        _binding = FragmentAgendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasPermissionToReadContacts()) {
            initializeViewModel()
            changeLayoutToPermissionsGranted()
        } else {
            requestPermissionToReadContacts()
            setGrantPermissionsButtonListener()
        }

    }

    private fun initializeViewModel() {
        agendaViewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
    }

    private fun initializePermissionsNotGrantedLayout() {
        binding.linearLayout.visibility = View.VISIBLE
    }

    private fun changeLayoutToPermissionsGranted() {
        binding.linearLayout.visibility = View.GONE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        binding.agendaRecyclerView.adapter = AgendaAdapter(agendaViewModel.contactsList.value!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setGrantPermissionsButtonListener() {
        binding.grantPermissionsButton.setOnClickListener {
            requestPermissionToReadContacts()
        }
    }

    private fun hasPermissionToReadContacts() = ActivityCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED


    private fun requestPermissionToReadContacts() {
        if (!hasPermissionToReadContacts()) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISSION_TO_READ_CONTACTS
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_TO_READ_CONTACTS && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Permission to read contacts was granted
                    initializeViewModel()
                    changeLayoutToPermissionsGranted()
                    return
                }
            }
        }
        initializePermissionsNotGrantedLayout()
    }

    companion object {
        private const val PERMISSION_TO_READ_CONTACTS = 0
    }

}