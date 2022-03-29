package com.example.implementation1.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.implementation1.Adapter.Adapter
import com.example.implementation1.R
import com.example.implementation1.Viewmodel.ViewModel
import com.example.implementation1.databinding.FragmentContactListBinding

class ContactListFragment : Fragment() {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private val adapter = Adapter()
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        binding.contactListFragmentRecyclerView.adapter = adapter

        binding.contactListFragmentAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contactList_to_addContactFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contact.observe(viewLifecycleOwner) {
            adapter.addContact(it)
        }

        viewModel.getUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    override fun onItemClick(position: Int, name: String, number: String) {
//        findNavController().navigate(R.id.action_contactList_to_contactCallerFragment)
////        ContactCallerFragment.newInstance(name, number).show(childFragmentManager, "")
//    }
}
