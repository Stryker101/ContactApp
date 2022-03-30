package com.example.implementation1.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.implementation1.Fragments.ContactListFragmentDirections
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.ContactItemLayoutBinding

class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContactItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.contactItemTvName.text = contacts[position].fullName
        holder.binding.contactItemTvPhoneNumber.text = contacts[position].contactNumber
        /**
         * onClicklisterner for each item on the recyclerview that takes the details of the clicked item as a object
         * of contact class and passes the object through the navigation
         * controller action function that will pass the contact to its destination.
         * and then finally, navigates to the intended destination
         */
        holder.binding.contactItemLayout.setOnClickListener {
            val action = ContactListFragmentDirections.actionContactListToContactCallerFragment(contacts[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
    /**
     * addContact function that adds new contacts to the arrayList of contacts to be displayed in the recyclerView
     */
    fun addContact(contact: Contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        } else {
            val index = contacts.indexOf(contact)
            if (contact.isDeleted) {
                contacts.removeAt(index)
            } else {
                contacts[index] = contact
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ContactItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
