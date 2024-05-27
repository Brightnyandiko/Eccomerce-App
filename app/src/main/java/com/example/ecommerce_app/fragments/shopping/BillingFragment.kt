package com.example.ecommerce_app.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.AddressAdapter
import com.example.ecommerce_app.adapters.BillingAdapter
import com.example.ecommerce_app.data.Address
import com.example.ecommerce_app.data.CartProduct
import com.example.ecommerce_app.data.order.Order
import com.example.ecommerce_app.data.order.OrderStatus
import com.example.ecommerce_app.databinding.FragmentBillingBinding
import com.example.ecommerce_app.util.HorizontalItemDecoration
import com.example.ecommerce_app.util.Resource
import com.example.ecommerce_app.viewmodel.BillingViewModel
import com.example.ecommerce_app.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class BillingFragment: Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingAdapter by lazy { BillingAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingRv()
        setupAddressRv()

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressfragment)
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                        addressAdapter.differ.submitList(it.data)
                        Log.d("cliff", "info ${it.data}")
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "Your Order was placed successfully", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        billingAdapter.differ.submitList(products)

        binding.tvTotalPrice.text = "$ ${totalPrice}"

        addressAdapter.onClick = {
            selectedAddress = it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null){
                Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){ dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }
}