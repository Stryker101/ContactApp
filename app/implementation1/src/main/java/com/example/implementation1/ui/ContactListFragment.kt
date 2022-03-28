package com.example.implementation1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.implementation1.databinding.FragmentContactListBinding

class ContactListFragment : Fragment(), Adapter.OnItemClickListener {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private val adapter = Adapter(this)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contactListFragmentRecyclerView.adapter = adapter

        binding.contactListFragmentAddBtn.setOnClickListener {
            AddContactFragment().show(childFragmentManager, "")
        }

        viewModel.contact.observe(viewLifecycleOwner) {
            adapter.addContact(it)
        }

        viewModel.getUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, name: String, number: String) {
//        val bundle = Bundle()
//        bundle.putString("item_name", name)
//        bundle.putString("item_phone", number)

        // val transaction = this.parentFragmentManager.beginTransaction()
        // ContactCallerFragment().show(childFragmentManager, "")
        ContactCallerFragment.newInstance(name, number).show(childFragmentManager, "")
//        transaction.apply {
//            ContactCallerFragmennewInstance(name, number).show(childFragmentManager, "")
//            this.commit()

//        ContactCallerFragment().arguments = bundle
    }
}
