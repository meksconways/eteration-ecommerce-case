package com.mek.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mek.feature.R
import com.mek.feature.databinding.ItemProductBinding
import com.mek.feature.home.model.ProductItem

class ProductAdapter(
    private val onClick: (ProductItem) -> Unit,
    private val onFavClick: (ProductItem) -> Unit,
    private val addToCartClick: (ProductItem) -> Unit
) : PagingDataAdapter<ProductItem, ProductVH>(DIFF) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductVH(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick, onFavClick, addToCartClick
        )

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        getItem(position)?.let {holder.bind(it) }
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int, payloads: List<Any?>) {
        if (payloads.any { it == PAYLOAD_FAVORITE }) {
            getItem(position)?.let { holder.bindFavorite(it) }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    companion object {
        private const val PAYLOAD_FAVORITE = "favorite"

        private val DIFF = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(old: ProductItem, new: ProductItem) = old.id == new.id
            override fun areContentsTheSame(old: ProductItem, new: ProductItem) =
                old.name == new.name &&
                        old.price == new.price &&
                        old.imageUrl == new.imageUrl &&
                        old.isFavorite == new.isFavorite

            override fun getChangePayload(oldItem: ProductItem, newItem: ProductItem): Any? = {
                if (oldItem.isFavorite != newItem.isFavorite) PAYLOAD_FAVORITE else null
            }
        }
    }
}

class ProductVH(
    private val binding: ItemProductBinding,
    private val onClick: (ProductItem) -> Unit,
    private val onFavClick: (ProductItem) -> Unit,
    private val onAddToCartClick: (ProductItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProductItem) = with(binding) {
        txtProductName.text = item.name
        txtProductPrice.text = item.priceText
        Glide.with(root)
            .load(item.imageUrl)
            .into(imgProduct)
        root.setOnClickListener { onClick(item) }
        bindFavorite(item)
        imgFavorite.setOnClickListener { onFavClick(item) }
        btnAddToCart.setOnClickListener { onAddToCartClick(item) }
    }

    fun bindFavorite(item: ProductItem) {
        with(binding) {
            imgFavorite.setImageResource(
                if (item.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
            )

            imgFavorite.imageTintList = if (item.isFavorite) {
                root.context.getColorStateList(R.color.favorite_yellow)
            } else {
                root.context.getColorStateList(com.mek.core_ui.R.color.core_ui_light_gray)
            }
        }

    }
}