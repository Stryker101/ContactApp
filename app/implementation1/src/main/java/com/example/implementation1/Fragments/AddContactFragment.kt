@file:JvmName("AddedContactFragmentKt")

package com.example.implementation1.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.implementation1.R
import com.example.implementation1.Viewmodel.ViewModel
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.FragmentAddContactBinding

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class AddContactFragment : Fragment() {
//    private var param1: String? = null
//    private var param2: String? = null

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
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
            }
        )

        binding!!.newContactFragmentAddBtn.setOnClickListener {
            val fullName = binding!!.newContactFragmentTvName.text.toString().trim()
            val contactNumber = binding!!.newContactFragmentTvPhone.text.toString().trim()
            val email = binding!!.newContactFragmentTvEmail.text.toString().trim()

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
            contact.email = email

            viewModel.addContact(contact)
            findNavController().navigate(R.id.action_addContactFragment_to_contactList)
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AddContactFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}
