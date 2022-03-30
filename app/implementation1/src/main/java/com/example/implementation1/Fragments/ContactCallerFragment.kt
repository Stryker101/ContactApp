package com.example.implementation1.Fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.implementation1.R
import com.example.implementation1.Viewmodel.ViewModel
import com.example.implementation1.data.Contact
import com.example.implementation1.databinding.ContactCallerFragementBinding

class ContactCallerFragment() : Fragment() {

    private var _binding: ContactCallerFragementBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel
    private val args by navArgs<ContactCallerFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContactCallerFragementBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        var id = args.currentUser.id
        binding?.contactCallerFragmentTvName!!.setText(args.currentUser.fullName)
        binding?.contactCallerFragmentTvPhone!!.setText(args.currentUser.contactNumber)
        binding?.contactCallerFragmentTvEmail!!.setText(args.currentUser.email)
        binding?.contactCallerFragmentIvCall?.setOnClickListener(
            View.OnClickListener {
                if (PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 101)
                } else {
                    makeCall()
                }
            }
        )
        binding!!.contactCallerFragmentIvEdit.setOnClickListener {
            val details = getContact()
            val action = ContactCallerFragmentDirections.actionContactCallerFragmentToEditContactFragment(details)
            findNavController().navigate(action)
        }
        binding!!.contactCallerFragmentIvDelete.setOnClickListener {

            AlertDialog.Builder(requireContext()).also {
                it.setTitle("Do you want to delete this contact?")
                it.setPositiveButton("Yes") { dialog, which ->
                    viewModel.deleteContact(args.currentUser)
                    findNavController().navigate(R.id.action_contactCallerFragment_to_contactList)
                }
            }.create().show()
        }
        binding!!.contactCallerFragmentIvShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type
        }
        binding!!.contactCallerFragmentIvShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "")
            intent.type = "text/plaint"
            startActivity(Intent.createChooser(intent, "Share to: "))

        }
        binding!!.contactCallerFragmentIvBack.setOnClickListener {
            fragmentManager?.popBackStack()
            findNavController().navigate(R.id.action_contactCallerFragment_to_contactList)
        }
    }

    private fun getContact(): Contact {
        return Contact(args.currentUser.id, args.currentUser.contactImage, args.currentUser.fullName, args.currentUser.contactNumber, args.currentUser.email)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED)) {
                    makeCall()
                } else {
                    shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)
                }
            }
        }
    }

    private fun makeCall() {
        var number = binding!!.contactCallerFragmentTvPhone.text
        var intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$number"))
        startActivity(intent)
    }
}
