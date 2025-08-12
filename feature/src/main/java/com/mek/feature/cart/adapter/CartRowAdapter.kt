package com.mek.feature.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mek.feature.cart.model.CartItemUi
import com.mek.feature.databinding.ItemCartBinding

class CartRowAdapter(
    private val onInc: (CartItemUi) -> Unit,
    private val onDec: (CartItemUi) -> Unit,
    private val onClick: ((CartItemUi) -> Unit)? = null
) : ListAdapter<CartItemUi, CartRowAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<CartItemUi>() {
        private const val PAYLOAD_QTY = "qty"

        override fun areItemsTheSame(o: CartItemUi, n: CartItemUi) = o.id == n.id
        override fun areContentsTheSame(o: CartItemUi, n: CartItemUi) = o == n
        override fun getChangePayload(o: CartItemUi, n: CartItemUi): Any? =
            if (o.quantity != n.quantity) PAYLOAD_QTY else null
    }

    inner class VH(private val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: CartItemUi) = with(b) {
            txtProductTitle.text = item.name
            txtProductPrice.text = item.priceText
            txtQuantity.text = item.quantity.toString()

            imgMinus.isEnabled = item.quantity > 0

            imgMinus.setOnClickListener { onDec(item) }
            imgPlus.setOnClickListener { onInc(item) }
            root.setOnClickListener { onClick?.invoke(item) }
        }

        fun bindQtyOnly(item: CartItemUi) = with(b) {
            txtQuantity.text = item.quantity.toString()
            imgMinus.isEnabled = item.quantity > 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == "qty") {
            holder.bindQtyOnly(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}