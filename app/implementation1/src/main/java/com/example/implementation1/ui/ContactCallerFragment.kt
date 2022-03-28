package com.example.implementation1.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.implementation1.databinding.ContactCallerFragementBinding

private const val NAME = "name"
private const val PHONE = "phone"

class ContactCallerFragment : DialogFragment() {

    private var _binding: ContactCallerFragementBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ViewModel
    var item_pos: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
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

        val name = arguments?.getString(NAME)
        val phone = arguments?.getString(PHONE)

        binding?.contactCallerFragmentTvName!!.text = name
        binding?.contactCallerFragmentTvPhone!!.text = phone

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        binding?.contactCallerFragmentIvCall?.setOnClickListener(
            View.OnClickListener {
                if (PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 101)
                } else {
                    makeCall()
                }
            }
        )
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
    companion object {
        fun newInstance(param1: String, param2: String) =
            ContactCallerFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, param1)
                    putString(PHONE, param2)
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
