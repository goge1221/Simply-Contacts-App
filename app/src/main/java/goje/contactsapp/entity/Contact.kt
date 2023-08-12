package goje.contactsapp.entity

data class Contact(
    val phoneNumber: String,
    val name: String,
    val contactId: String
) : java.io.Serializable, ContactElement(contactId)
