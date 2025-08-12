package com.mek.feature.profile

import androidx.fragment.app.viewModels
import com.mek.core_ui.foundation.BaseFragment
import com.mek.feature.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override val viewModel: ProfileViewModel by viewModels()

    override fun observeViewModel() {

    }
}