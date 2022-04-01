package com.example.implementation1.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.implementation1.data.Contact
import com.example.implementation1.data.NODE_CONTACTS
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class ViewModel : ViewModel() {
    /**
     * dbcontact is reference to the firebase database
     */
    private val dbcontacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> get() = _contact

    /**
     *addContact function to add new contacts to the database.
     * the contacts id, is generated automatically but not added to the database,
     * its only used as a key that can be used identify each contacts.
     * The addOnComplete listener listens for the successful completion of the add function everytime it is called
     */
    fun addContact(contact: Contact) {
        contact.id = dbcontacts.push().key
        dbcontacts.child(contact.id!!).setValue(contact).addOnCompleteListener {
            if (it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    /**
     * ChildEventLister listens for every change made to database, it takes a snapshot of the current
     * state of the database, this snapshot is then pass to the get updates function that is used to
     * update the view where the contacts are displayed
     */
    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            contact?.isDeleted = true
            _contact.value = contact!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    /**
     * getUpdates function updates the view where the contacts are displayed
     */
    fun getUpdate() {
        dbcontacts.addChildEventListener(childEventListener)
    }

    /**
     * function for editing contacts already existing in the database.
     * it takes in edited text as an object of contact class and sets the new values os values
     * of the contact to be edited, using the id of the contact to be edited.
     */
    fun updateContact(contact: Contact) {
        dbcontacts.child(contact.id!!).setValue(contact)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    /**
     * Deletes contact from the database by taking the id of the contact in question and setting
     * the corresponding values to null
     */
    fun deleteContact(contact: Contact) {
        dbcontacts.child(contact.id!!).setValue(null).addOnCompleteListener {
            if (it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    /**
     *shuts down the childEventlister after it has been called, to save memory
     */
    override fun onCleared() {
        super.onCleared()
        dbcontacts.removeEventListener(childEventListener)
    }
}
