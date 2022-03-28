package com.example.implementation1.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.ContactItemLayoutBinding

class Adapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContactItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.contactItemTvName.text = contacts[position].fullName
        holder.binding.contactItemTvPhoneNumber.text = contacts[position].contactNumber
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun addContact(contact: Contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ContactItemLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val name = contacts[position].fullName.toString()
                val number = contacts[position].contactNumber.toString()
                listener.onItemClick(position, name, number)
            }
//            val position = contacts[bindingAdapterPosition]
//            clicker.clickedRecyclerView(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, name: String, number: String)
    }
}
