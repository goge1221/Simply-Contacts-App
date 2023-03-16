package goje.contactsapp.ui.detailedView

import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.RawContacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import goje.contactsapp.R
import goje.contactsapp.databinding.FragmentAddNewContactBinding
import goje.contactsapp.utils.Constants


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

        if(Constants.USER_ENABLED_BIG_FONT_SIZE){
            binding.image.visibility = View.GONE
        }

    }

    private fun addNewContactClickListener() {
        binding.updateContactButton.setOnClickListener {
            if (nameIsOk() && numberIsOk()) {
                insertContact()
                showNavAndToolBar()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun nameIsOk(): Boolean {
        if (binding.callerName.text.toString().isEmpty()) {
            Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun numberIsOk(): Boolean {
        if (binding.callerName.text.toString().isEmpty()) {
            Toast.makeText(context, "Please enter a number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun insertContact() {


        val ops = ArrayList<ContentProviderOperation>()

        ops.add(
            ContentProviderOperation
                .newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        //Add name
        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                    ContactsContract.Data.RAW_CONTACT_ID, 0
                )
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(
                    StructuredName.DISPLAY_NAME,
                    binding.callerName.text.toString()
                ).build()
        )

        //Add mobile number
        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                    ContactsContract.Data.RAW_CONTACT_ID, 0
                )
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    binding.callerNumber.text.toString()
                )
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        //Add contact raw id
        val rawContactInsertIndex = ops.size

        ops.add(
            ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null).withValue(RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(RawContacts.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(RawContacts.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, binding.callerName.text.toString()).build()
        )

        requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)

        Toast.makeText(
            context,
            binding.root.resources.getString(
                R.string.person_added_toast, binding.callerName.text.toString()
            ), Toast.LENGTH_SHORT
        ).show()
    }


    private fun showNavAndToolBar() {
        val toolBar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolBar.visibility = View.VISIBLE

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar.visibility = View.VISIBLE
    }

}