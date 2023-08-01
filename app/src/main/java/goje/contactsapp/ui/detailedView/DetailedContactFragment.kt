package goje.contactsapp.ui.detailedView

import android.app.role.RoleManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentDetailedContactBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.ui.agenda.IContactDelete
import goje.contactsapp.ui.agenda.IContactGetById
import goje.contactsapp.utils.Constants
import goje.contactsapp.utils.PermissionChecker


class DetailedContactFragment(
    private var contact: Contact,
    private val deleteListener: IContactDelete,
    private val contactRetriever: IContactGetById
) : Fragment(), IReturnFromDialogToMainFragment {

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

        if (Constants.USER_ENABLED_BIG_FONT_SIZE) {
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
        val updatedContact = contactRetriever.getContactById(contact.contactId)
        if (updatedContact.name != contact.name) {
            Toast.makeText(context, "Name updated", Toast.LENGTH_SHORT).show()
            binding.callerName.text = updatedContact.name
            contact = updatedContact
        } else
            binding.callerName.text = contact.name

        if (updatedContact.phoneNumber != contact.phoneNumber) {
            Toast.makeText(context, "Number updated", Toast.LENGTH_SHORT).show()
            binding.callerNumber.text = updatedContact.phoneNumber
            contact = updatedContact
        } else
            binding.callerNumber.text = contact.phoneNumber
    }


    private fun addButtonListeners() {
        addEditButtonListener()
        addCallButtonClickListener()
        addSmsButtonClickListener()
        addDeleteButtonListener()
    }

    //FIXME After editing a contact the deletion does not work
    private fun addDeleteButtonListener() {
        binding.deleteButton.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val confirmationDialog = ConfirmDeletionDialog(
            requireContext(),
            R.style.DialogTheme,
            binding.root,
            contact,
            deleteListener,
            this
        )
        confirmationDialog.show()
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
                ) == true && appIsDefaultHandler()
            ) {
                //open intent with edit
                initiateCall()
            } else {
                if (appIsDefaultHandler()) {
                    requestPermissionToCall()
                } else {
                    makeAppDefaultCallerApp()
                    if (appIsDefaultHandler())
                        requestPermissionToCall()
                    //else
                    //makeAppDefaultCallerApp()
                }
            }
        }
    }

    private fun makeAppDefaultCallerApp() {
        Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
            TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
            requireActivity().packageName
        ).apply {
            if (resolveActivity(requireActivity().packageManager) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val rm: RoleManager? =
                        requireActivity().getSystemService(RoleManager::class.java)
                    if (rm?.isRoleAvailable(RoleManager.ROLE_DIALER) == true) {
                        @Suppress("DEPRECATION")
                        requireActivity().startActivityForResult(
                            rm.createRequestRoleIntent(RoleManager.ROLE_DIALER),
                            Constants.REQUEST_CODE_SET_DEFAULT_DIALER
                        )
                    }
                } else {
                    @Suppress("DEPRECATION")
                    requireActivity().startActivityForResult(
                        this,
                        Constants.REQUEST_CODE_SET_DEFAULT_DIALER
                    )
                }
            }
        }
    }

    private fun appIsDefaultHandler(): Boolean {
        val telecomManager =
            requireActivity().getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
        return requireActivity().packageName == telecomManager.defaultDialerPackage
    }

    private fun addEditButtonListener() {
        binding.settingsButton.setOnClickListener {
            if (PermissionChecker.userHasSpecifiedPermission(
                    context,
                    android.Manifest.permission.WRITE_CONTACTS
                ) == true
            ) {
                //open intent with edit
                openModifyContactFragment()
            } else {
                requestPermissionToWriteContacts()
            }
        }
    }

    private fun initiateCall() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.phoneNumber))
        returnToLastFragment()
        startActivity(intent)
    }

    private fun initiateSMSSending() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact.phoneNumber))
        returnToLastFragment()
        startActivity(intent)
    }

    private fun returnToLastFragment() {
        showNavAndToolBar()
        parentFragmentManager.popBackStack()
    }

    private fun openModifyContactFragment() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_fragment_activity_main2,
                EditContactFragment(contact),
                "EDIT_CONTACT_FRAGMENT"
            )
            .addToBackStack("DETAILED_CONTACT_FRAGMENT")
            .commit()
    }

    private fun requestPermissionToWriteContacts() {
        if (!PermissionChecker.userHasSpecifiedPermission(
                context,
                android.Manifest.permission.WRITE_CONTACTS
            )!!
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_CONTACTS),
                Constants.PERMISSION_TO_WRITE_CONTACTS
            )
        }
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
                    val sharedPreferences: SharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(requireContext())


                    // Get the SharedPreferences.Editor to make changes
                    val editor = sharedPreferences.edit()

                    // Save the boolean value
                    editor.putBoolean("permission_to_call_granted", true)

                    // Apply the changes
                    editor.apply()


                    initiateCall()
                    break
                }
            }
        }
        // Toast.makeText(context, "Please grant the permission in order to use this functionality", Toast.LENGTH_LONG).show()
    }

    override fun returnBackToDisplayFragments() {
        returnToLastFragment()
    }

}