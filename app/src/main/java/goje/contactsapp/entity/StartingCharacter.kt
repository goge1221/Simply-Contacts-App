package goje.contactsapp.entity

/**
 * This data class holds information about a starting character used for organizing contacts.
 * It includes an identifier [mId] and the actual character [character].
 *
 * @param mId A unique identifier for the starting character.
 * @param character The character used for organizing contacts (e.g. 'A').
 */
data class StartingCharacter(
    val mId: String,
    val character: String
) : ContactElement(mId)
