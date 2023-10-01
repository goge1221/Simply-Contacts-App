package goje.contactsapp.entity

/**
 * Represents a base class for elements related to contacts.
 *
 * [StartingCharacter] and [Contact], inherit from
 * this class to specialize functionality for different contact-related
 * components.
 *
 * @param id A unique identifier for the contact element.
 */
open class ContactElement(val id: String)