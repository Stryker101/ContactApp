package com.example.implementation1.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.implementation1.R
import com.example.implementation1.Viewmodel.ViewModel
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.FragmentEditContactBinding

class EditContactFragment() : Fragment() {

    private var _binding: FragmentEditContactBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel
    private val args by navArgs<EditContactFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditContactBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return
        val args = EditContactFragmentArgs.fromBundle(bundle)

        binding?.editContactFragmentTvName?.setText(args.currentUser.fullName)
        binding?.editContactFragmentTvPhone?.setText(args.currentUser.contactNumber)
        binding?.editContactFragmentTvEmail?.setText(args.currentUser.email)
//        binding?.editContactFragmentIvContactImage.setIM

        binding!!.editContactFragmentUpdateBtn.setOnClickListener {
            val fullName = binding!!.editContactFragmentTvName.text.toString()
            val contactNumber = binding!!.editContactFragmentTvPhone.text.toString()
            val email = binding!!.editContactFragmentTvEmail.text.toString()

            if (fullName.isEmpty()) {
                binding!!.editContactFragmentTvName.error = "Pls enter a name"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                binding!!.editContactFragmentTvPhone.error = "Phone Number is required"
                return@setOnClickListener
            }

            val updatedContact = Contact(args.currentUser.id, args.currentUser.contactImage, fullName, contactNumber, email)

            viewModel.updateContact(updatedContact)
            findNavController().navigate(R.id.action_editContactFragment_to_contactList)
            Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
        }
    }
}
