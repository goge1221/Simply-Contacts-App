package goje.contactsapp.ui.detailedView

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import goje.contactsapp.R
import goje.contactsapp.databinding.ConfirmDeleteDialogBinding
import goje.contactsapp.entity.Contact
import goje.contactsapp.ui.agenda.IContactDelete


class ConfirmDeletionDialog(
    context: Context, themeResId: Int,
    private val container: ViewGroup?,
    private val contact: Contact,
    private val deleteListener: IContactDelete,
    private val returnBackListener: IReturnFromDialogToMainFragment
) : Dialog(context, themeResId) {

    private lateinit var binding: ConfirmDeleteDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfirmDeleteDialogBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        setContentView(binding.root)
        updateDeletionName()
        setOnClickListeners()
    }

    private fun setOnClickListeners(){
        binding.noButton.setOnClickListener {
            dismiss()
        }

        binding.confirmButton.setOnClickListener {
            deleteListener.deleteContact(contact)
            returnBackListener.returnBackToDisplayFragments()
            dismiss()
        }
    }


    private fun updateDeletionName(){
        val deleteText: String = binding.root.resources.getString(
            R.string.are_you_sure_you_want_to_delete, contact.name
        )
        binding.confirmationDeleteText.text = deleteText
    }



}
