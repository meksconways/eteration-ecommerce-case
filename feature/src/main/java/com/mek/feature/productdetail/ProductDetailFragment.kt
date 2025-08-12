package com.mek.feature.productdetail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mek.core_ui.extension.launchAndRepeatWithViewLifecycle
import com.mek.core_ui.foundation.BaseFragment
import com.mek.feature.R
import com.mek.feature.databinding.FragmentProductDetailBinding

class ProductDetailFragment :
    BaseFragment<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {
    override val viewModel: ProductDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.onBackClick = {
            appNavigator.back()
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart()
        }

        binding.imgFavorite.setOnClickListener {
            viewModel.onToggleFavorite()
        }
    }

    override fun observeViewModel() {
        launchAndRepeatWithViewLifecycle(viewModel.state) {
            if (it.product != null) {
                with(binding) {
                    toolbar.setTitleText(it.product.name)
                    Glide.with(requireContext())
                        .load(it.product.imageUrl)
                        .into(imgProduct)
                    txtProductName.text = it.product.name
                    txtProductDescription.text = it.product.description
                    txtProductPrice.text = it.product.priceText
                    imgFavorite.setImageResource(
                        if (it.product.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
                    )
                    imgFavorite.imageTintList = if (it.product.isFavorite) {
                        root.context.getColorStateList(R.color.favorite_yellow)
                    } else {
                        root.context.getColorStateList(com.mek.core_ui.R.color.core_ui_light_gray)
                    }
                }
            }
            binding.progressBar.isVisible = it.isLoading

            if (it.errorMessage != null) {
                showError(it.errorMessage) {
                    appNavigator.back()
                }
            }
        }


    }

    companion object {
        private const val ARG_ID = "productId"
        fun newInstance(id: String) = ProductDetailFragment().apply {
            arguments = bundleOf(ARG_ID to id)
        }
    }
}