package com.example.implementation1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.implementation1.R
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.FragmentNewContactBinding

class AddContactFragment : DialogFragment() {
    private var _binding: FragmentNewContactBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewContactBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.result.observe(
            viewLifecycleOwner,
            Observer {
                val message = if (it == null) {
                    getString(R.string.contact_added)
                } else {
                    getString(R.string.error, it.message)
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        )

        binding!!.newContactFragmentAddBtn.setOnClickListener {
            val fullName = binding!!.newContactFragmentTvName.text.toString().trim()
            val contactNumber = binding!!.newContactFragmentTvPhone.text.toString().trim()

            if (fullName.isEmpty()) {
                binding!!.newContactFragmentTvName.error = "Pls enter a name"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                binding!!.newContactFragmentTvPhone.error = "Phone Number is required"
                return@setOnClickListener
            }

            val contact = Contact()
            contact.fullName = fullName
            contact.contactNumber = contactNumber

            viewModel.addContact(contact)
        }
    }
}
