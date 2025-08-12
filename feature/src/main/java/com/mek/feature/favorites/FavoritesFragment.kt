package com.mek.feature.favorites

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.mek.core_ui.foundation.BaseFragment
import com.mek.feature.databinding.FragmentFavoritesBinding

class FavoritesFragment: BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {
    override val viewModel: FavoritesViewModel by viewModels()

    override fun observeViewModel() {

    }
}