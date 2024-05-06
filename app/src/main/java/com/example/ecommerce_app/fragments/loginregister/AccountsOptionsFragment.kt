package com.example.ecommerce_app.fragments.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.FragmentAccountOptionsBinding

class AccountsOptionsFragment: Fragment(R.layout.fragment_account_options) {

    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegisterAccountOptions.setOnClickListener{
            findNavController().navigate(R.id.action_accountsOptionsFragment_to_registerFragment)
        }
        binding.buttonLoginAccountOptions.setOnClickListener{
            findNavController().navigate(R.id.action_accountsOptionsFragment_to_loginFragment)
        }
    }
}