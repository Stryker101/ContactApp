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

class ContactCallerFragment : Fragment() {

    private var _binding: ContactCallerFragementBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel

    /**
     * args receives the arguments passed from particular contact that was clicked on the contact list
     */
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
        /**
         * Each view in the ContactCallerFragment is bound to its corresponding
         * text in the arguments that was passed to the fragment
         */
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        var id = args.currentUser.id
        binding?.contactCallerFragmentTvName!!.setText(args.currentUser.fullName)
        binding?.contactCallerFragmentTvPhone!!.setText(args.currentUser.contactNumber)
        binding?.contactCallerFragmentTvEmail!!.setText(args.currentUser.email)

        /**
         * onClickListener for the call button on the fragment that checks if permission has been granted to make calls,
         * if permission has been granted, it goes ahead to makes the call.
         * if permission hasn't been granted it, it asks the user for permission
         */
        binding?.contactCallerFragmentIvCall?.setOnClickListener(
            View.OnClickListener {
                if (PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 101)
                } else {
                    makeCall()
                }
            }
        )

        /**
         * onClickListener for the Edit button that calls the getContact function, and passes the contact that returns
         * through the a navigation controller action function that will pass the contact to its destination.
         * and then finally, navigates to the intended destination
         */
        binding!!.contactCallerFragmentIvEdit.setOnClickListener {
            val details = getContact()
            val action = ContactCallerFragmentDirections.actionContactCallerFragmentToEditContactFragment(details)
            findNavController().navigate(action)
        }

        /**
         * onClickListener for the delete button that prompts the user to confirm if they choose to delete, and on confirmation
         * called the deleteContact function from the viewModel and passes the current contact in context as an argument,
         * after which it navigates back to the contact list
         */
        binding!!.contactCallerFragmentIvDelete.setOnClickListener {
            AlertDialog.Builder(requireContext()).also {
                it.setTitle("Do you want to delete this contact?")
                it.setPositiveButton("Yes") { dialog, which ->
                    viewModel.deleteContact(args.currentUser)
                    findNavController().navigate(R.id.action_contactCallerFragment_to_contactList)
                }
            }.create().show()
        }

        /**
         * onClickListener for the share button that uses implicit intent to call other apps that can facilitate
         * sharing.
         */
        binding!!.contactCallerFragmentIvShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Share Contact")
            intent.type = "text/plaint"
            startActivity(Intent.createChooser(intent, "Share Via: "))
        }

        /**
         * onClicklistener for the back button to return to the contact list fragment after poping the
         * contactCallerFragment off the stack
         */
        binding!!.contactCallerFragmentIvBack.setOnClickListener {
            fragmentManager?.popBackStack()
            findNavController().navigate(R.id.action_contactCallerFragment_to_contactList)
        }
    }

    /**
     * getContacts function that takes the arguments and bundles them into an object of the contact class
     */
    private fun getContact(): Contact {
        return Contact(args.currentUser.id, args.currentUser.contactImage, args.currentUser.fullName, args.currentUser.contactNumber, args.currentUser.email)
    }

    /**
     * overridden onRequestPermission function that checks the result of the previous permission request
     * according to the request code that was assigned to it.
     */
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

    /**
     * function that takes the number displayed in the phone number textview
     * and initiates the call using an intent
     */
    private fun makeCall() {
        val number = binding!!.contactCallerFragmentTvPhone.text
        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$number"))
        startActivity(intent)
    }
}
