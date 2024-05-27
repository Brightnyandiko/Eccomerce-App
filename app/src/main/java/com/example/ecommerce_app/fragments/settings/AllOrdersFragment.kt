package com.example.ecommerce_app.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.adapters.AllOrdersAdapter
import com.example.ecommerce_app.databinding.FragmentOrdersBinding
import com.example.ecommerce_app.util.Resource
import com.example.ecommerce_app.viewmodel.AllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersFragment: Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private val allOrdersAdapter by lazy { AllOrdersAdapter() }
    private val viewModel by viewModels<AllOrdersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRv()

        lifecycleScope.launchWhenStarted {
            viewModel.allOrders.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        allOrdersAdapter.differ.submitList(it.data)
                        if (it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter = allOrdersAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }
}