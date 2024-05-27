package com.example.ecommerce_app.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerce_app.R
import com.example.ecommerce_app.data.order.Order
import com.example.ecommerce_app.data.order.OrderStatus
import com.example.ecommerce_app.data.order.getOrderStatus
import com.example.ecommerce_app.databinding.OrderItemBinding

class AllOrdersAdapter: Adapter<AllOrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(private val binding: OrderItemBinding): ViewHolder(binding.root){
        fun bind(order: Order){
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources

                val colorDrawable = when (getOrderStatus(order.orderStatus)){
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val diffUtil = object  : DiffUtil.ItemCallback<Order>() {
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrdersAdapter.OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AllOrdersAdapter.OrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    var onClick: ((Order) -> Unit)? = null
}