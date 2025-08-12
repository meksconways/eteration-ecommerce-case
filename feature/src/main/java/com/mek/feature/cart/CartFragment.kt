package com.mek.feature.cart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mek.core_ui.extension.launchAndRepeatWithViewLifecycle
import com.mek.core_ui.foundation.BaseFragment
import com.mek.feature.cart.adapter.CartRowAdapter
import com.mek.feature.databinding.FragmentCartBinding

class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    override val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartRowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartRowAdapter(
            onInc = { viewModel.inc(it) },
            onDec = { viewModel.dec(it) }
        )

        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter

        binding.btnComplete.setOnClickListener {
            Toast.makeText(requireContext(), "Complete!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun observeViewModel() {
        launchAndRepeatWithViewLifecycle(viewModel.state) { s ->
            adapter.submitList(s.items)
            binding.txtProductPrice.text = s.totalText
            binding.containerEmptyView.isVisible = s.isEmpty
            binding.containerCompleteOrder.isVisible = !s.isEmpty
            binding.rvCart.isVisible = !s.isEmpty
        }
    }

}