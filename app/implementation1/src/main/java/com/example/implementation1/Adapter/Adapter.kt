package com.example.implementation1.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.ContactItemLayoutBinding
import com.example.implementation1.Fragments.ContactListFragmentDirections

class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContactItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.contactItemTvName.text = contacts[position].fullName
        holder.binding.contactItemTvPhoneNumber.text = contacts[position].contactNumber
        holder.binding.contactItemLayout.setOnClickListener {
            val action = ContactListFragmentDirections.actionContactListToContactCallerFragment(contacts[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

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
