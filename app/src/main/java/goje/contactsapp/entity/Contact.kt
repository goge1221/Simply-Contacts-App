package goje.contactsapp.entity

data class Contact(
    val phoneNumber: String,
    val name: String,
    val contactId: String
) : java.io.Serializable, ContactElement(contactId){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        other as Contact

        // Check if contactId is the same for both contacts
        return this.contactId == other.contactId
    }
    override fun hashCode(): Int {
        var result = phoneNumber.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + contactId.hashCode()
        return result
    }

}
