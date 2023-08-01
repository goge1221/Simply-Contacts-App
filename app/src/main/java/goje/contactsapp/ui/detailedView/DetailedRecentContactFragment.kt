package goje.contactsapp.ui.detailedView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentDetailedRecentCallBinding
import goje.contactsapp.entity.RecentCall
import goje.contactsapp.utils.Constants
import goje.contactsapp.utils.PermissionChecker

class DetailedRecentContactFragment(
    private var call: RecentCall,
) : Fragment() {

    private var _binding: FragmentDetailedRecentCallBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedRecentCallBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButtonListeners()
        initializeViewWithInformation()

        if(Constants.USER_ENABLED_BIG_FONT_SIZE){
            binding.personSampleImage.visibility = View.GONE
            binding.callerName.setPadding(
                binding.callerName.paddingLeft,
                200,
                binding.callerName.paddingRight,
                binding.callerName.paddingBottom
            )
        }
    }

    private fun initializeViewWithInformation() {
        binding.callerName.text = call.name
        binding.callerNumber.text = call.phoneNumber
    }


    private fun addButtonListeners() {
        addCallButtonClickListener()
        addSmsButtonClickListener()
    }

    private fun addSmsButtonClickListener() {
        binding.messageButton.setOnClickListener {
            initiateSMSSending()
        }
    }

    private fun addCallButtonClickListener() {
        binding.callButton.setOnClickListener {
            if (PermissionChecker.userHasSpecifiedPermission(
                    context,
                    android.Manifest.permission.CALL_PHONE
                ) == true
            ) {
                //open intent with edit
                initiateCall()
            } else {
                requestPermissionToCall()
            }
        }
    }

    private fun initiateCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call.phoneNumber))
        returnToLastFragment()
        startActivity(intent)
    }

    private fun initiateSMSSending() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + call.phoneNumber))
        returnToLastFragment()
        startActivity(intent)
    }

    private fun returnToLastFragment() {
        showNavAndToolBar()
        parentFragmentManager.popBackStack()
    }

    private fun requestPermissionToCall() {
        if (!PermissionChecker.userHasSpecifiedPermission(
                context,
                android.Manifest.permission.CALL_PHONE
            )!!
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.CALL_PHONE),
                Constants.PERMISSION_TO_CALL
            )
        }
    }

    private fun showNavAndToolBar() {
        val toolBar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.VISIBLE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.VISIBLE
    }

}