package com.mek.feature.home.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mek.core_ui.extension.launchAndRepeatWithViewLifecycle
import com.mek.core_ui.foundation.BaseFragment
import com.mek.domain.repository.Sort
import com.mek.feature.databinding.FragmentHomeBinding
import com.mek.feature.filter.FilterBottomSheet
import com.mek.feature.filter.model.FilterParams
import com.mek.feature.filter.model.toFilterParams
import com.mek.feature.home.adapter.ProductAdapter
import com.mek.feature.home.effect.HomeEffect
import com.mek.feature.home.viewmodel.HomeViewModel
import com.mek.feature.productdetail.ProductDetailFragment
import com.mek.feature.sorting.SortBottomSheet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collectLatest { paging ->
                    adapter.submitData(viewLifecycleOwner.lifecycle, paging)
                }
            }
        }

        launchAndRepeatWithViewLifecycle(viewModel.state) { s ->
            binding.progressBar.isVisible = s.isLoading
            binding.containerEmptyView.isVisible = s.isEmpty
            binding.rvProducts.isVisible = !s.isEmpty
            if (s.errorMessage != null) {
                showError(s.errorMessage) {
                    adapter.retry()
                }
            }
        }

        // Effects
        launchAndRepeatWithViewLifecycle(viewModel.effect) {
            when (it) {
                is HomeEffect.NavigateToDetail -> {
                    appNavigator.start(ProductDetailFragment.Companion.newInstance(it.productId))
                }

                HomeEffect.OpenFilterBottomSheet -> {
                    FilterBottomSheet.Companion.new(
                        allBrands = viewModel.allBrands,
                        allModels = viewModel.allModels,
                        selected = viewModel.currentFilters.toFilterParams()
                    ).show(parentFragmentManager, "FilterSheet")
                }

                HomeEffect.OpenSortBottomSheet -> {
                    val current = viewModel.currentSort
                    SortBottomSheet.Companion.new(current)
                        .show(parentFragmentManager, "SortSheet")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductAdapter(
            onClick = {
                viewModel.onProductClick(it.id)
            },
            onFavClick = {
                viewModel.onFavoriteClick(it.id)
            },
            addToCartClick = {
                viewModel.addToCart(it)
            }
        )

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY



        with(binding) {
            rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
            rvProducts.adapter = adapter
            binding.rvProducts.itemAnimator = null
            (binding.rvProducts.layoutManager as? GridLayoutManager)?.apply {
                initialPrefetchItemCount = 6
            }

            // Search
            searchView.onTextChanged = { q -> viewModel.onQueryChanged(q) }
            searchView.onSearchAction = { q -> viewModel.onQueryChanged(q) }

            // Filter / Sort triggers
            cardFilterContainer.setOnClickListener { viewModel.onOpenFilterClick() }
            cardSortContainer.setOnClickListener { viewModel.onOpenSortClick() }


            // LoadState
            adapter.addLoadStateListener { ls ->
                viewModel.onLoadStates(ls)
                val isEmpty = ls.refresh is LoadState.NotLoading &&
                        adapter.itemCount == 0 &&
                        (ls.append.endOfPaginationReached)
                viewModel.markEmpty(isEmpty)
            }

            parentFragmentManager.setFragmentResultListener(
                SortBottomSheet.Companion.REQ_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val sort = Sort.valueOf(bundle.getString(SortBottomSheet.Companion.RES_SORT, ""))
                viewModel.onSortSelected(sort)
            }

            parentFragmentManager.setFragmentResultListener(
                FilterBottomSheet.Companion.REQ_KEY,
                viewLifecycleOwner
            ) { _, b ->
                val brands =
                    b.getStringArrayList(FilterBottomSheet.Companion.RES_BRANDS)?.toSet().orEmpty()
                val models =
                    b.getStringArrayList(FilterBottomSheet.Companion.RES_MODELS)?.toSet().orEmpty()
                viewModel.onFiltersChanged(FilterParams(brands, models))
            }

        }


    }
}