package com.example.agendaapp.ui.recentCalls

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
        _binding = FragmentRecentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasPermissionToReadCallLog()) {
            initializeViewModel()
            changeLayoutToPermissionsGranted()
        } else {
            requestPermissionToReadCallLog()
            setGrantPermissionsButtonListener()
        }
    }

    private fun initializePermissionsNotGrantedLayout(){
        binding.linearLayout.visibility = View.VISIBLE
    }

    private fun setGrantPermissionsButtonListener() {
        binding.grantPermissionsButton.setOnClickListener {
            requestPermissionToReadCallLog()
        }
    }
    private fun changeLayoutToPermissionsGranted(){
        binding.linearLayout.visibility = View.GONE
        binding.agendaRecyclerView.visibility = View.VISIBLE
        binding.agendaRecyclerView.adapter = RecentCallsAdapter(notificationsViewModel.recentCallsList.value!!)
    }

    private fun initializeViewModel(){
        notificationsViewModel = ViewModelProvider(this)[RecentCallsViewModel::class.java]
    }

    private fun hasPermissionToReadCallLog() = ActivityCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_CALL_LOG
    ) == PackageManager.PERMISSION_GRANTED


    private fun requestPermissionToReadCallLog() {
        if (!hasPermissionToReadCallLog()) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CALL_LOG),
                PERMISSION_TO_READ_CALL_LOG
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_TO_READ_CALL_LOG && grantResults.isNotEmpty()) {
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
        private const val PERMISSION_TO_READ_CALL_LOG = 1
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}