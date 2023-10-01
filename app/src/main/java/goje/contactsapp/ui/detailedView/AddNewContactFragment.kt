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
        if (Constants.USER_ENABLED_BIG_FONT_SIZE) {
            binding.image.visibility = View.GONE
        }
    }

    private fun addNewContactClickListener() {
        binding.updateContactButton.setOnClickListener {
            if (nameIsOk() && numberIsOk()) {
                insertContact()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun nameIsOk(): Boolean {
        if (binding.callerName.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().resources.getString(R.string.enter_name_error),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun numberIsOk(): Boolean {
        if (binding.callerName.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().resources.getString(R.string.enter_number_error),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }


    private fun insertContact() {
        val ops = ArrayList<ContentProviderOperation>()
        ops.add(createRawContactOperation())
        ops.add(createStructuredNameOperation(binding.callerName.text.toString()))
        ops.add(createPhoneNumberOperation(binding.callerNumber.text.toString()))

        requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)

        Toast.makeText(
            requireContext(),
            requireContext().resources.getString(
                R.string.person_added_toast,
                binding.callerName.text.toString()
            ),
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun createRawContactOperation(): ContentProviderOperation {
        return ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
            .withValue(RawContacts.ACCOUNT_TYPE, null)
            .withValue(RawContacts.ACCOUNT_NAME, null)
            .build()
    }

    private fun createStructuredNameOperation(displayName: String): ContentProviderOperation {
        return ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
            .withValue(StructuredName.DISPLAY_NAME, displayName)
            .build()
    }

    private fun createPhoneNumberOperation(phoneNumber: String): ContentProviderOperation {
        return ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
            .withValue(
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            )
            .build()
    }

}